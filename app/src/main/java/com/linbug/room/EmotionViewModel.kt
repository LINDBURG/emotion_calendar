package com.linbug.room

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class EmotionViewModel(private val repository: EmotionRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allEmotions: LiveData<List<Emotion>> = repository.allEmotions.asLiveData()

    fun getDateEmotion(date: Long): LiveData<Emotion> = repository.getDateEmotion(date).asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertEmotion(emotion: Emotion) = viewModelScope.launch {
        repository.insertEmotion(emotion)
    }
    fun deleteEmotion(emotion: Emotion) = viewModelScope.launch {
        repository.deleteEmotion(emotion)
    }
    fun updateEmotion(emotion: Emotion) = viewModelScope.launch {
        repository.updateEmotion(emotion)
    }
}

class EmotionViewModelFactory(private val repository: EmotionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmotionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmotionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}