package s3605807.vamshinath.agricultureadvisoryapp.mlhelper


import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class PlantIdentifier(context: Context) {

    private val interpreter: InterpreterApi?
    private val labels: List<String>

    init {
        interpreter = try {

            val modelBuffer = loadModelFile(context, "plant_identification_model.tflite")

            val options = InterpreterApi.Options().apply {
                setNumThreads(2) // ⚡ performance
            }

            InterpreterApi.create(modelBuffer, options)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        labels = try {
            context.assets.open("labels_plants.txt")
                .bufferedReader()
                .useLines { it.toList() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    val isInitialized: Boolean
        get() = interpreter != null && labels.isNotEmpty()

    fun classify(bitmap: Bitmap): Pair<String, Float> {

        if (!isInitialized) {
            return "Model not initialized" to 0f
        }

        // ✅ Preprocess properly
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(127.5f, 127.5f)) // ⚠️ important for most models
            .build()

        var tensorImage = TensorImage.fromBitmap(bitmap)
        tensorImage = imageProcessor.process(tensorImage)

        // ✅ Output buffer
        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, labels.size),
            DataType.FLOAT32
        )

        try {
            interpreter?.run(
                tensorImage.buffer,
                outputBuffer.buffer.rewind()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return "Inference Error" to 0f
        }

        val scores = outputBuffer.floatArray

        val maxIndex = scores.indices.maxByOrNull { scores[it] } ?: 0
        val confidence = scores[maxIndex]

        return labels.getOrElse(maxIndex) { "Unknown" } to confidence
    }

    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val afd = context.assets.openFd(modelPath)
        FileInputStream(afd.fileDescriptor).use { input ->
            val channel: FileChannel = input.channel
            return channel.map(
                FileChannel.MapMode.READ_ONLY,
                afd.startOffset,
                afd.declaredLength
            )
        }
    }
}

