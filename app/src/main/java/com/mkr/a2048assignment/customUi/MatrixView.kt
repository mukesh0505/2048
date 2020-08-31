package com.mkr.a2048assignment.customUi

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TableLayout
import com.mkr.a2048assignment.R
import com.mkr.a2048assignment.databinding.MatrixBinding
import com.mkr.a2048assignment.model.Direction
import com.mkr.a2048assignment.model.Matrix
import com.mkr.a2048assignment.viewModel.MatrixViewModel

class MatrixView(context: Context?, attrs: AttributeSet?) : TableLayout(context, attrs) {

    private val viewModel = MatrixViewModel()

    private val tiles: Array<Array<Button?>>
    private val appearingAnimation: Animation

    constructor(context: Context?) : this(context, null)

    var callback: ((Pair<Int, Boolean>) -> Unit)? = null

    private var binding: MatrixBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = MatrixBinding.inflate(inflater, this, true)

        tiles = Array<Array<Button?>>(
            N
        ) { arrayOfNulls<Button>(N) }

        binding.apply {
            tiles[0][0] = button00
            tiles[0][1] = button01
            tiles[0][2] = button02
            tiles[0][3] = button03
            tiles[1][0] = button10
            tiles[1][1] = button11
            tiles[1][2] = button12
            tiles[1][3] = button13
            tiles[2][0] = button20
            tiles[2][1] = button21
            tiles[2][2] = button22
            tiles[2][3] = button23
            tiles[3][0] = button30
            tiles[3][1] = button31
            tiles[3][2] = button32
            tiles[3][3] = button33
        }
        appearingAnimation = AnimationUtils.loadAnimation(context,
            R.anim.appearing
        )
        displayMatrix(viewModel.matrix)
    }

    fun reset() {
        viewModel.reset()
        displayMatrix(viewModel.matrix)
    }

    private fun displayMatrix(m: Matrix) {
        val n: Int = 4
        var number: Int
        for (r in 0 until n) {
            for (c in 0 until n) {
                number = m.getSpot(r, c)
                if (number == Matrix.EMPTY) {
                    tiles[r][c]?.text = ""
                } else {
                    tiles[r][c]?.text = number.toString()
                }
                tiles[r][c]?.background = resources.getDrawable(getDrawableId(number))
                if (viewModel.isMergeSpot(r, c)) {
                    tiles[r][c]?.startAnimation(appearingAnimation)
                }
            }
        }
        invalidate()
    }

    fun onSwipeUp() {
        handleSwipe(Direction.UP)
    }

    fun onSwipeRight() {
        handleSwipe(Direction.RIGHT)
    }

    fun onSwipeLeft() {
        handleSwipe(Direction.LEFT)
    }

    fun onSwipeDown() {
        handleSwipe(Direction.DOWN)
    }

    private fun handleSwipe(dir: Direction) {
        val result =viewModel.handleSwipe(dir)
        displayMatrix(viewModel.matrix)
        callback?.invoke(result)
    }

    private fun getDrawableId(n: Int): Int {
        when (n) {
            2 -> return R.drawable.n_2
            4 -> return R.drawable.n_4
            8 -> return R.drawable.n_8
            16 -> return R.drawable.n_16
            32 -> return R.drawable.n_32
            64 -> return R.drawable.n_64
            128 -> return R.drawable.n_128
            256 -> return R.drawable.n_256
            512 -> return R.drawable.n_512
            1024 -> return R.drawable.n_1024
            2048 -> return R.drawable.n_2048
        }
        return R.drawable.n_0
    }

    companion object {
        private val N: Int = 4
    }
}
