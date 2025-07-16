# PocketTranslator Android App

A basic Android application for LLM (Large Language Model) inference using MediaPipe.

## Features

- Clean, modern Material Design UI
- On-device LLM inference using MediaPipe
- Real-time text processing
- Responsive design with loading states
- Error handling and user feedback

## Architecture

- **MVVM Pattern**: Uses ViewModel and LiveData for reactive UI updates
- **Coroutines**: Asynchronous processing for smooth user experience
- **MediaPipe Integration**: Ready for LLM model integration
- **Material Design**: Modern Android UI components

## Setup Instructions

### Prerequisites

1. Android Studio Arctic Fox or later
2. Android SDK API level 24 or higher
3. Kotlin support enabled

### Getting Started

1. Clone or download this project
2. Open in Android Studio
3. Sync project with Gradle files
4. Build and run on device or emulator

### Adding a Real LLM Model

This demo app includes simulated responses. To use a real LLM model:

1. **Download a compatible model**:
   - Gemma 2B or 7B models
   - Phi-3 models
   - Other MediaPipe-compatible LLM models

2. **Add model to your app**:
   ```
   app/src/main/assets/your_model.bin
   ```

3. **Update model path** in `LLMInference.kt`:
   ```kotlin
   private fun getModelPath(context: Context): String {
       return "file:///android_asset/your_model.bin"
   }
   ```

4. **Configure model parameters**:
   ```kotlin
   val options = LlmInference.LlmInferenceOptions.builder()
       .setModelPath(getModelPath(context))
       .setMaxTokens(512)
       .setTemperature(0.8f)
       .setTopK(40)
       .build()
   ```

## Key Components

### MainActivity
- Main UI controller
- Handles user interactions
- Observes ViewModel state changes

### LLMViewModel
- Manages app state and business logic
- Handles LLM initialization and inference
- Provides reactive data to UI

### LLMInference
- Wrapper for MediaPipe LLM inference
- Handles model loading and text generation
- Manages model lifecycle

## Dependencies

- **MediaPipe Tasks**: `com.google.mediapipe:tasks-text:0.10.8`
- **AndroidX**: Core, AppCompat, Material Design
- **Kotlin Coroutines**: For asynchronous operations
- **Lifecycle Components**: ViewModel and LiveData

## Usage

1. Launch the app
2. Wait for "LLM Ready" status
3. Enter text in the input field
4. Tap "Generate Response"
5. View the generated response

## Customization

### UI Theming
- Modify colors in `res/values/colors.xml`
- Update themes in `res/values/themes.xml`
- Customize layouts in `res/layout/`

### Model Configuration
- Adjust inference parameters in `LLMInference.kt`
- Modify response processing logic
- Add custom prompt templates

## Performance Tips

1. **Model Size**: Use smaller models for better performance
2. **Memory Management**: Monitor memory usage with large models
3. **Background Processing**: Keep inference on background threads
4. **Caching**: Cache frequently used responses

## Troubleshooting

### Common Issues

1. **Model Loading Errors**:
   - Verify model file path and format
   - Check model compatibility with MediaPipe
   - Ensure sufficient device memory

2. **Performance Issues**:
   - Use smaller models for older devices
   - Implement response streaming for long outputs
   - Optimize model parameters

3. **Build Errors**:
   - Update Android Studio and SDK
   - Clean and rebuild project
   - Check dependency versions

## License

This project is provided as-is for educational and development purposes.

## Contributing

Feel free to submit issues, feature requests, and pull requests to improve this basic implementation.