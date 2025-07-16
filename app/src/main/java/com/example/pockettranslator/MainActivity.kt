package com.example.pockettranslator

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.pockettranslator.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LLMViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[LLMViewModel::class.java]
        
        setupUI()
        observeViewModel()
        
        // Initialize the LLM
        viewModel.initializeLLM(this)
    }
    
    private fun setupUI() {
        binding.btnSend.setOnClickListener {
            val inputText = binding.etInput.text.toString().trim()
            if (inputText.isNotEmpty()) {
                processInput(inputText)
            } else {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.btnClear.setOnClickListener {
            binding.etInput.text?.clear()
            binding.tvOutput.text = ""
        }
    }
    
    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSend.isEnabled = !isLoading
        }
        
        viewModel.result.observe(this) { result ->
            binding.tvOutput.text = result
        }
        
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        
        viewModel.isInitialized.observe(this) { isInitialized ->
            binding.btnSend.isEnabled = isInitialized
            if (isInitialized) {
                binding.tvStatus.text = "LLM Ready"
                binding.tvStatus.setTextColor(getColor(android.R.color.holo_green_dark))
            } else {
                binding.tvStatus.text = "Initializing LLM..."
                binding.tvStatus.setTextColor(getColor(android.R.color.holo_orange_dark))
            }
        }
    }
    
    private fun processInput(input: String) {
        lifecycleScope.launch {
            viewModel.generateResponse(input)
        }
    }
}