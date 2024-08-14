package com.example.trivia_vivia

import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var selectedDifficulty: Int = 0
    var selectedAnswerType: String = "text"
}