package s3605807.vamshinath.agricultureadvisoryapp.mlhelper

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tflite.java.TfLite
import org.tensorflow.lite.DataType
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException

class PlantDiseaseClassifier(private val context: Context) {

    private val TAG = "ML_DEBUG"
    private val imageSize = 200 // 🟢 Double check if your model expects 224 or 200
    private var interpreter: InterpreterApi? = null

    var isInitialized = false
        private set

    private val labels: List<String> = try {
        val loadedLabels = FileUtil.loadLabels(context, "labels.txt")
        Log.d(TAG, "Labels loaded: ${loadedLabels.size} classes found.")
        loadedLabels
    } catch (e: IOException) {
        Log.e(TAG, "Error loading labels.txt from assets", e)
        emptyList()
    }

    init {
        TfLite.initialize(context).addOnSuccessListener {
            try {
                val model = FileUtil.loadMappedFile(context, "plant_identification_model.tflite")
                val options = InterpreterApi.Options()
                    .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
                    .setNumThreads(2)

                interpreter = InterpreterApi.create(model, options)
                isInitialized = true
                Log.i(TAG, "✅ TFLite Interpreter ready (GMS Runtime)")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Failed to create interpreter: ${e.message}")
            }
        }.addOnFailureListener { e ->
            Log.e(TAG, "❌ Google Play Services TFLite init failed: ${e.message}")
        }
    }

    fun classify(bitmap: Bitmap): Pair<String, Float> {
        if (!isInitialized || interpreter == null) {
            Log.w(TAG, "Classification ignored: Model not ready.")
            return "NOT_READY" to 0.0f
        }

        try {
            val startTime = System.currentTimeMillis()

            // 1. Preprocessing
            // 🟢 CRITICAL: Most TFLite models need NormalizeOp(0f, 255f) to scale 0-255 to 0.0-1.0
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(imageSize, imageSize, ResizeOp.ResizeMethod.BILINEAR))
                .add(NormalizeOp(0f, 255f))
                .build()

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            tensorImage = imageProcessor.process(tensorImage)

            // 2. Prepare Output
            val outputBuffer = TensorBuffer.createFixedSize(
                intArrayOf(1, labels.size),
                DataType.FLOAT32
            )

            // 3. Inference
            interpreter?.run(tensorImage.buffer, outputBuffer.buffer.rewind())

            // 4. Detailed Logging of Results
            val tensorLabel = TensorLabel(labels, outputBuffer)
            val resultMap = tensorLabel.mapWithFloatValue

            Log.d(TAG, "--- Inference Results ---")
            resultMap.forEach { (label, score) ->
                // This will show exactly why "Background" might be winning
                Log.d(TAG, String.format("Class: %-20s | Confidence: %.2f%%", label, score * 100))
            }

            val topResult = resultMap.maxByOrNull { it.value }
            val endTime = System.currentTimeMillis()

            Log.i(TAG, "Top Result: ${topResult?.key} (${topResult?.value}) | Time: ${endTime - startTime}ms")

            return if (topResult != null) {
                topResult.key to topResult.value
            } else {
                "No Detection" to 0.0f
            }

        } catch (e: Exception) {
            Log.e(TAG, "Inference error: ${e.message}", e)
            return "ERROR" to 0.0f
        }
    }

    fun close() {
        interpreter?.close()
        isInitialized = false
    }
}