package com.example.pockettranslator

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LLMViewModel : ViewModel() {
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _isInitialized = MutableLiveData<Boolean>()
    val isInitialized: LiveData<Boolean> = _isInitialized
    
    private var llmInference: LLMInference? = null
    
    fun initializeLLM(context: Context) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isInitialized.value = false
                
                withContext(Dispatchers.IO) {
                    llmInference = LLMInference()
                    llmInference?.initialize(context)
                }
                
                _isInitialized.value = true
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to initialize LLM: ${e.message}"
                _isInitialized.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    suspend fun generateResponse(input: String) {
        if (llmInference == null) {
            _error.value = "LLM not initialized"
            return
        }
        
        try {
            _isLoading.value = true
            _error.value = null
            
            val response = withContext(Dispatchers.IO) {
                llmInference?.generateText(input) ?: "No response generated"
            }
            
            _result.value = response
        } catch (e: Exception) {
            _error.value = "Error generating response: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        llmInference?.close()
    }
}