package com.mkr.a2048assignment.viewModel

import androidx.lifecycle.ViewModel
import com.mkr.a2048assignment.model.Direction
import com.mkr.a2048assignment.model.Matrix

class MatrixViewModel : ViewModel() {

    var matrix: Matrix =
        Matrix()

    fun reset() {
        matrix = Matrix()
    }

    fun handleSwipe(dir: Direction): Pair<Int, Boolean> {
        val score: Int = matrix.swipe(dir)
        val gameOver: Boolean = matrix.isStuck
        matrix.generateNew()
        return score to gameOver
    }

    fun isMergeSpot(r: Int, c: Int): Boolean {
        return matrix.isMergeSpot(r, c)
    }
}