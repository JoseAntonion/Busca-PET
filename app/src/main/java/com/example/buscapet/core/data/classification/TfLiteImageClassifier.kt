package com.example.buscapet.core.data.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.buscapet.core.domain.classification.ClassificationResult
import com.example.buscapet.core.domain.classification.ImageClassifier
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.PriorityQueue
import javax.inject.Inject

class TfLiteImageClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageClassifier {

    // Ensure this matches your file in assets
    private val modelName: String = "mobilenet_v1_1.0_224.tflite"
    private val labelFileName: String = "labels.txt" // We will create this file

    private var interpreter: Interpreter? = null
    private var labels: List<String> = emptyList()

    init {
        setupClassifier()
    }

    private fun setupClassifier() {
        try {
            // Load labels
            val assets = context.assets.list("")
            if (assets?.contains(labelFileName) == true) {
                 labels = FileUtil.loadLabels(context, labelFileName)
            } else {
                 Log.w("TfLiteImageClassifier", "Labels file not found. Results will be indices.")
            }

            // Load model
            if (assets?.contains(modelName) == true) {
                val modelFile = FileUtil.loadMappedFile(context, modelName)
                val options = Interpreter.Options()
                interpreter = Interpreter(modelFile, options)
            } else {
                Log.e("TfLiteImageClassifier", "Model file not found!")
            }
        } catch (e: Exception) {
            Log.e("TfLiteImageClassifier", "Error initializing classifier", e)
        }
    }

    override suspend fun classify(bitmap: Bitmap, maxResults: Int): List<ClassificationResult> {
        return withContext(Dispatchers.Default) {
            val tflite = interpreter ?: return@withContext emptyList()

            try {
                // 1. Get Input/Output Tensor details to adapt to ANY model
                val inputTensor = tflite.getInputTensor(0)
                val inputShape = inputTensor.shape() // {1, 224, 224, 3} usually
                val inputDataType = inputTensor.dataType()

                val imageSizeX = inputShape[1]
                val imageSizeY = inputShape[2]

                // 2. Preprocess Image
                // Some models expect 0-1 (Float), others 0-255 (Quantized/Int)
                val imageProcessorBuilder = ImageProcessor.Builder()
                    .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.BILINEAR))

                // If model is Floating Point (not Int8), usually needs normalization
                if (inputDataType == DataType.FLOAT32) {
                    // Normalize [0, 255] -> [0, 1] or [-1, 1] depending on model. 
                    // MobileNet often uses [0, 1] (div by 255) or [-1, 1] ( (x-127.5)/127.5 )
                    // Let's assume standard [0, 1] for safety or use (127.5, 127.5) for [-1,1]
                    // Adjust these values if your specific model results are weird.
                    imageProcessorBuilder.add(NormalizeOp(127.5f, 127.5f))
                }

                val imageProcessor = imageProcessorBuilder.build()
                var tensorImage = TensorImage(inputDataType)
                tensorImage.load(bitmap)
                tensorImage = imageProcessor.process(tensorImage)

                // 3. Prepare Output Buffer
                val outputTensor = tflite.getOutputTensor(0)
                val outputShape = outputTensor.shape() 
                val outputDataType = outputTensor.dataType()
                
                // Handle different output shapes (1D or 3D)
                // We flatten the output to 1D array of probabilities/scores
                val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

                // 4. Run Inference
                tflite.run(tensorImage.buffer, outputBuffer.buffer.rewind())

                // 5. Parse Results
                val scores = outputBuffer.floatArray
                val pq = PriorityQueue<ClassificationResult>(maxResults) { o1, o2 ->
                    o2.score.compareTo(o1.score)
                }

                for (i in scores.indices) {
                    // Filter low confidence to optimize
                    if (scores[i] > 0.2f) { // 20% threshold
                        val label = if (labels.size > i) labels[i] else "Class $i"
                        pq.add(ClassificationResult(label, scores[i]))
                    }
                }

                val results = ArrayList<ClassificationResult>()
                val resultSize = minOf(pq.size, maxResults)
                for (i in 0 until resultSize) {
                    results.add(pq.poll())
                }
                results
            } catch (e: Exception) {
                Log.e("TfLiteImageClassifier", "Error during inference", e)
                emptyList()
            }
        }
    }
}