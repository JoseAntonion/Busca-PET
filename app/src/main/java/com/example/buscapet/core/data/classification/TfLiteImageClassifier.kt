package com.example.buscapet.core.data.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.buscapet.core.domain.classification.ClassificationResult
import com.example.buscapet.core.domain.classification.ImageClassifier
import com.example.buscapet.core.domain.classification.ModelType
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
import java.util.PriorityQueue
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class TfLiteImageClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageClassifier {

    private val modelFiles = mapOf(
        ModelType.BREED to "mobilenet_v1_1.0_224.tflite",
        ModelType.ANIMAL_TYPE to "animal_type_model.tflite"
    )

    private val labelFiles = mapOf(
        ModelType.BREED to "labels.txt",
        ModelType.ANIMAL_TYPE to "animal_labels.txt"
    )

    // Cache interpreters to avoid reloading
    private val interpreters = ConcurrentHashMap<ModelType, Interpreter>()
    private val labelsCache = ConcurrentHashMap<ModelType, List<String>>()

    override suspend fun classify(
        bitmap: Bitmap, 
        maxResults: Int, 
        modelType: ModelType
    ): List<ClassificationResult> {
        return withContext(Dispatchers.Default) {
            val tflite = getInterpreter(modelType) ?: return@withContext emptyList()
            val labels = getLabels(modelType)

            try {
                // 1. Get Input/Output Tensor details
                val inputTensor = tflite.getInputTensor(0)
                val inputShape = inputTensor.shape() 
                val inputDataType = inputTensor.dataType()

                val imageSizeX = inputShape[1]
                val imageSizeY = inputShape[2]

                // 2. Preprocess Image
                val imageProcessorBuilder = ImageProcessor.Builder()
                    .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.BILINEAR))

                if (inputDataType == DataType.FLOAT32) {
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
                
                val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

                // 4. Run Inference
                tflite.run(tensorImage.buffer, outputBuffer.buffer.rewind())

                // 5. Parse Results
                val scores = outputBuffer.floatArray
                val pq = PriorityQueue<ClassificationResult>(maxResults) { o1, o2 ->
                    o2.score.compareTo(o1.score)
                }

                for (i in scores.indices) {
                    if (scores[i] > 0.2f) { 
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
                Log.e("TfLiteImageClassifier", "Error during inference with $modelType", e)
                emptyList()
            }
        }
    }

    private fun getInterpreter(modelType: ModelType): Interpreter? {
        if (interpreters.containsKey(modelType)) return interpreters[modelType]

        val modelName = modelFiles[modelType] ?: return null
        return try {
            val assets = context.assets.list("")
            if (assets?.contains(modelName) == true) {
                val modelFile = FileUtil.loadMappedFile(context, modelName)
                val options = Interpreter.Options()
                val interpreter = Interpreter(modelFile, options)
                interpreters[modelType] = interpreter
                interpreter
            } else {
                Log.e("TfLiteImageClassifier", "Model file $modelName not found for $modelType")
                null
            }
        } catch (e: Exception) {
            Log.e("TfLiteImageClassifier", "Error initializing interpreter for $modelType", e)
            null
        }
    }

    private fun getLabels(modelType: ModelType): List<String> {
        if (labelsCache.containsKey(modelType)) return labelsCache[modelType]!!

        val labelFile = labelFiles[modelType] ?: return emptyList()
        return try {
            val assets = context.assets.list("")
            if (assets?.contains(labelFile) == true) {
                val loadedLabels = FileUtil.loadLabels(context, labelFile)
                labelsCache[modelType] = loadedLabels
                loadedLabels
            } else {
                 Log.w("TfLiteImageClassifier", "Labels file $labelFile not found.")
                 emptyList()
            }
        } catch (e: Exception) {
             Log.w("TfLiteImageClassifier", "Error loading labels for $modelType", e)
             emptyList()
        }
    }
}