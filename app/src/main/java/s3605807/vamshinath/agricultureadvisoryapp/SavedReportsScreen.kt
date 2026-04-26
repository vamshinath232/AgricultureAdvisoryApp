package s3605807.vamshinath.agricultureadvisoryapp

import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import s3605807.vamshinath.agricultureadvisoryapp.data.Report
import s3605807.vamshinath.agricultureadvisoryapp.data.ReportListViewModel
import s3605807.vamshinath.agricultureadvisoryapp.ui.theme.p1
import s3605807.vamshinath.agricultureadvisoryapp.ui.theme.p2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedReportsScreen(
    navController: NavHostController,
    viewModel: ReportListViewModel = viewModel()
) {

    val reports by viewModel.reports.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Saved Reports", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = p1),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->

        if (reports.isEmpty()) {
            EmptyReportsView()
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(reports) { report ->
                    ReportItemCard(
                        report = report,
                        p1 = p1,
                        p2 = p2,
                        onDelete = { viewModel.deleteReport(it) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


@Composable
fun EmptyReportsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text("No reports found", fontSize = 18.sp, color = Color.Gray)
        Text("Scan a plant leaf and save reports.", color = Color.Gray)
    }
}

@Composable
fun ReportItemCard(
    report: Report,
    p1: Color,
    p2: Color,
    onDelete: (Report) -> Unit
) {
    val context = LocalContext.current
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Delete Report") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirm = false
                    onDelete(report)
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(report.imageUri),
                    contentDescription = "Leaf Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = report.disease,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = p1,
                        modifier = Modifier.weight(1f),
                        maxLines = 2
                    )

                    Row {
                        IconButton(
                            onClick = {
                                shareReport(context, report)
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    p2.copy(alpha = 0.12f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = p2
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        IconButton(
                            onClick = { showConfirm = true },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Color.Red.copy(alpha = 0.08f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                InfoRow(
                    icon = Icons.Default.Eco,
                    text = "Plant: ${report.plant}"
                )

                InfoRow(
                    icon = Icons.Default.Analytics,
                    text = "Confidence: ${String.format("%.1f%%", report.confidence * 100)}",
                    highlightColor = p2
                )

                InfoRow(
                    icon = Icons.Default.CalendarToday,
                    text = "Date: ${report.date}",
                    textColor = Color.Gray
                )
            }
        }
    }
}

fun shareReport(
    context: Context,
    report: Report
) {
    val resolver = context.contentResolver
    val sourceUri = Uri.parse(report.imageUri)

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "plant_report_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(
            MediaStore.Images.Media.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES + "/PlantDiseaseReports"
        )
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val mediaUri = resolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    ) ?: return

    resolver.openInputStream(sourceUri)?.use { input ->
        resolver.openOutputStream(mediaUri)?.use { output ->
            input.copyTo(output)
        }
    }

    values.clear()
    values.put(MediaStore.Images.Media.IS_PENDING, 0)
    resolver.update(mediaUri, values, null, null)

    val shareText = """
🌿 Plant Disease Report

Plant: ${report.plant}
Disease: ${report.disease}
Confidence: ${String.format("%.1f%%", report.confidence * 100)}
Date: ${report.date}

Generated using Plant Disease Identifier App
""".trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, mediaUri)

        // 👇 THIS is the key fix
        clipData = ClipData.newPlainText("Plant Report", shareText)

        putExtra(Intent.EXTRA_TEXT, shareText)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Share Report"))
}


@Composable
fun InfoRow(
    icon: ImageVector,
    text: String,
    textColor: Color = Color.DarkGray,
    highlightColor: Color? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = highlightColor ?: Color.Gray,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = highlightColor ?: textColor,
            fontWeight = if (highlightColor != null) FontWeight.Medium else FontWeight.Normal
        )
    }
}


