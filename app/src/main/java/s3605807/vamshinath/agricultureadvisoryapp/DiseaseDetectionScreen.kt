package s3605807.vamshinath.agricultureadvisoryapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import s3605807.vamshinath.agricultureadvisoryapp.mlhelper.PlantDiseaseClassifier
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG_CAMERAX = "CameraX"
private const val TAG_ML = "ML"


@Composable
fun PlantScanScreen(navController: NavHostController) {
    var hasCameraPermission by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                hasCameraPermission = true
            } else {
                Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (hasCameraPermission) {
        CameraScreen(navController)
    } else {
        PermissionDeniedView {
            // This will open app settings so the user can grant the permission
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context.packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
    }
}

@Composable
fun PermissionDeniedView(onRequest: () -> Unit) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Camera permission is required to scan leaves.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRequest) {
            Text("Open Settings")
        }
    }
}


@Composable
fun CameraScreen(navController: NavHostController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val imageCapture = remember { ImageCapture.Builder().build() }
    val previewView = remember { PreviewView(context) }

    var isScanning by remember { mutableStateOf(false) }
    var loadingMessage by remember { mutableStateOf("") }

    val diseaseClassifier = remember { PlantDiseaseClassifier(context) }

    LaunchedEffect(Unit) {
        setupCamera(context, lifecycleOwner, previewView, imageCapture)
    }

    Box(Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        ScanAnimationOverlay()
        Box(
            Modifier.fillMaxSize().padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            FloatingActionButton(onClick = {
                if (isScanning) return@FloatingActionButton
                scope.launch {
                    isScanning = true
                    loadingMessage = "Capturing image..."
                    val capturedUri = captureImage(context, imageCapture)
                    if (capturedUri != null) {
                        loadingMessage = "Image captured, analyzing..."
                        classifyImageAndNavigate(context, capturedUri, navController, diseaseClassifier)                    }
                    isScanning = false
                }
            }) {
                Icon(Icons.Default.Camera, contentDescription = "Capture")
            }
        }
        if (isScanning) {
            LoadingOverlay(message = loadingMessage)
        }
    }
}


private fun setupCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    imageCapture: ImageCapture
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    cameraProviderFuture.addListener({
        try {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            Log.e(TAG_CAMERAX, "Camera binding failed", e)
            Toast.makeText(context, "Could not start camera: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }, ContextCompat.getMainExecutor(context))
}

private suspend fun captureImage(context: Context, imageCapture: ImageCapture): Uri? {
    return suspendCoroutine { continuation ->
        val file = File(context.cacheDir, "leaf_${System.currentTimeMillis()}.jpg")
        val options = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture.takePicture(
            options,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val uri = Uri.fromFile(file)
                    Log.d(TAG_CAMERAX, "Image captured successfully: $uri")
                    continuation.resume(uri)
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG_CAMERAX, "Image capture failed", exc)
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "Image capture failed: ${exc.message}", Toast.LENGTH_LONG).show()
                    }
                    continuation.resume(null)
                }
            }
        )
    }
}


private suspend fun classifyImageAndNavigate(
    context: Context,
    uri: Uri,
    navController: NavHostController,
    diseaseClassifier: PlantDiseaseClassifier
) {
    withContext(Dispatchers.IO) {
        try {
            var retryCount = 0
            while (!diseaseClassifier.isInitialized && retryCount < 20) {
                Log.d("ML_DEBUG", "Waiting for model to initialize... ${retryCount}")
                delay(200)
                retryCount++
            }


            if (diseaseClassifier.isInitialized) {
                val bitmap = decodeAndCropBitmap(context, uri)
                val (resultLabel, confidence) = diseaseClassifier.classify(bitmap)
                val (plant, disease) = parseResult(resultLabel)
                val encodedUri = URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8.toString())
                withContext(Dispatchers.Main) {
                    navController.popBackStack()
                    navController.navigate("scan_result/$plant/$confidence/$disease/$confidence/$encodedUri")
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "AI Engine taking too long. Try again.", Toast.LENGTH_SHORT).show()
                }
            }


        } catch (e: Exception) {
            Log.e(TAG_ML, "Error during classification", e)
            withContext(Dispatchers.Main) {
                Log.e(TAG_ML, "Failed to analyze leaf", e)
                Toast.makeText(context, "Failed to analyze leaf: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

private fun decodeAndCropBitmap(context: Context, uri: Uri): Bitmap {
    val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.createSource(context.contentResolver, uri)
    } else {
        @Suppress("DEPRECATION")
        ImageDecoder.createSource(context.contentResolver, uri)
    }
    val decodedBitmap = ImageDecoder.decodeBitmap(source)

    // Ensure the bitmap is in ARGB_8888 format
    val argbBitmap = if (decodedBitmap.config != Bitmap.Config.ARGB_8888) {
        decodedBitmap.copy(Bitmap.Config.ARGB_8888, true)
    } else {
        decodedBitmap
    }

    val size = minOf(argbBitmap.width, argbBitmap.height)
    val left = (argbBitmap.width - size) / 2
    val top = (argbBitmap.height - size) / 2
    return Bitmap.createBitmap(argbBitmap, left, top, size, size)
}

private fun parseResult(resultLabel: String): Pair<String, String> {
    val parts = resultLabel.split("___")
    val plant = parts.getOrElse(0) { "Unknown Plant" }
    val disease = parts.getOrElse(1) { resultLabel }
    return Pair(plant, disease)
}


@Composable
fun ScanAnimationOverlay() {
    val infinite = rememberInfiniteTransition()
    val pulse by infinite.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse)
    )
    val rotate by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Restart)
    )
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(Modifier.size((240 * pulse).dp)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationZ = rotate }
                    .border(4.dp, Color.Green.copy(alpha = 0.6f), CircleShape)
            )
            Box(
                Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
                    .background(Color.Green.copy(alpha = 0.15f), CircleShape)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text("Place leaf in the circle", color = Color.White, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun LoadingOverlay(message: String) {
    Box(
        Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.Green)
            Spacer(Modifier.height(16.dp))
            Text(message, color = Color.White)
        }
    }
}
