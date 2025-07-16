package com.example.pockettranslator

import android.content.Context
import android.util.Log
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LLMInference {
    
    private var llmInference: LlmInference? = null
    private val TAG = "LLMInference"
    
    // Change this to match your actual task file name in assets
    private val MODEL_FILE_NAME = "llm_model.task"
    
    suspend fun initialize(context: Context) {
        try {
            Log.d(TAG, "Initializing LLM with task file: $MODEL_FILE_NAME")
            
            // Create options for LLM inference using task file from assets
            val options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath(getModelPath())
                .setMaxTokens(512)
                .setTemperature(0.8f)
                .setTopK(40)
                .setRandomSeed(101)
                .build()
            
            // Initialize LLM inference on IO thread
            withContext(Dispatchers.IO) {
                llmInference = LlmInference.createFromOptions(context, options)
            }
            
            Log.d(TAG, "LLM initialized successfully")
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize LLM: ${e.message}", e)
            throw Exception("Failed to initialize LLM. Make sure '$MODEL_FILE_NAME' exists in assets folder. Error: ${e.message}")
        }
    }
    
    private fun getModelPath(): String {
        // Return the path to the task file in assets directory
        // Make sure to replace "llm_model.task" with your actual task file name
        return "file:///android_asset/$MODEL_FILE_NAME"
    }
    
    suspend fun generateText(prompt: String): String {
        return try {
            if (llmInference == null) {
                throw IllegalStateException("LLM not initialized")
            }
            
            Log.d(TAG, "Generating response for prompt: ${prompt.take(50)}...")
            
            // Use actual MediaPipe LLM inference
            val response = withContext(Dispatchers.IO) {
                llmInference?.generateResponse(prompt)
            }
            
            val generatedText = response?.text() ?: "No response generated"
            Log.d(TAG, "Generated response: ${generatedText.take(100)}...")
            
            generatedText
            
        } catch (e: Exception) {
            Log.e(TAG, "Error generating text: ${e.message}", e)
            
            // Provide helpful error message
            when {
                e.message?.contains("not found") == true || 
                e.message?.contains("asset") == true -> {
                    "Error: Model file '$MODEL_FILE_NAME' not found in assets folder.\n\nPlease:\n1. Add your .task file to app/src/main/assets/\n2. Update MODEL_FILE_NAME in LLMInference.kt\n3. Rebuild the app"
                }
                e.message?.contains("memory") == true -> {
                    "Error: Insufficient memory to load the model. Try using a smaller model or closing other apps."
                }
                e.message?.contains("format") == true -> {
                    "Error: Invalid model format. Make sure you're using a compatible MediaPipe LLM task file."
                }
                else -> {
                    "Error generating response: ${e.message}\n\nTroubleshooting:\n1. Verify model file exists in assets\n2. Check model compatibility\n3. Ensure sufficient device memory"
                }
            }
        }
    }
    
    fun close() {
        try {
            llmInference?.close()
            llmInference = null
            Log.d(TAG, "LLM inference closed")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing LLM inference", e)
        }
    }
    
    // Helper function to check if model is initialized
    fun isInitialized(): Boolean {
        return llmInference != null
    }
    
    // Helper function to get model info (for debugging)
    fun getModelInfo(): String {
        return "Model: $MODEL_FILE_NAME, Path: ${getModelPath()}, Initialized: ${isInitialized()}"
    }
}