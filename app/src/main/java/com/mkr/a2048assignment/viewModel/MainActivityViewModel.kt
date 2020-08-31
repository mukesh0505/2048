package com.mkr.a2048assignment.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    private var totalScore: Int = 0
        set(value) {
            field = value
            totalScoreLiveData.postValue(value)

            if (bestScore < value) {
                bestScore = value
            }
        }

    private var bestScore: Int = 0
        set(value) {
            field = value
            bestScoreLiveData.postValue(value)
        }

    private var isGameOver: Boolean = false
        set(value) {
            field = value
            isGameOverLiveData.postValue(value)
        }

    var totalScoreLiveData: MutableLiveData<Int> = MutableLiveData()
    var bestScoreLiveData: MutableLiveData<Int> = MutableLiveData()
    var isGameOverLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun addScore(score: Int) {
        totalScore += score
    }

    fun updateBestScore(score: Int) {
        bestScore = score
    }

    fun updateGameOverStatus(status: Boolean) {
        isGameOver = status
    }

    fun reset(callback: () -> Unit) {
        totalScore = 0
        isGameOver = false
        callback.invoke()
    }

}