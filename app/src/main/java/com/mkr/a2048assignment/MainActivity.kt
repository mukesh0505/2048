package com.mkr.a2048assignment

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mkr.a2048assignment.databinding.ActivityMainBinding
import com.mkr.a2048assignment.helper.SwipeDetector
import com.mkr.a2048assignment.helper.SwipeListener
import com.mkr.a2048assignment.viewModel.MainActivityViewModel


class MainActivity : AppCompatActivity(), View.OnTouchListener,
    SwipeListener {

    private lateinit var mDetector: GestureDetectorCompat

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    private val sharedPreferences by lazy {
        getSharedPreferences("2048Game", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpView()
        addObserver()
        initGame()
    }

    private fun initGame() {
        fetchBestScore()
        reset()
    }

    private fun fetchBestScore() {
        viewModel.updateBestScore(sharedPreferences.getInt(BEST_SCORE, 0))
    }

    private fun reset() {
        viewModel.reset {
            binding.matrixView.reset()
        }
    }

    private fun setUpView() {
        binding.apply {
            matrixOverlay.setOnTouchListener(this@MainActivity)
            matrixView.callback = {
                viewModel.addScore(it.first)
                viewModel.updateGameOverStatus(it.second)
            }

            mDetector = GestureDetectorCompat(this@MainActivity,
                SwipeDetector(this@MainActivity)
            )

            reset.setOnClickListener {
                reset()
            }
        }
    }

    private fun addObserver() {
        viewModel.totalScoreLiveData.observe(this, Observer {
            binding.score.text = getString(R.string.score, it.toString())
        })
        viewModel.bestScoreLiveData.observe(this, Observer {
            binding.best.text = getString(R.string.best, it.toString())
            sharedPreferences.edit()?.putInt(BEST_SCORE, it)?.apply()
        })
        viewModel.isGameOverLiveData.observe(this, Observer {
            if (it) {
                reset()
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return mDetector.onTouchEvent(event)
    }

    override fun onSwipeLeft() {
        binding.matrixView.onSwipeLeft()
    }

    override fun onSwipeRight() {
        binding.matrixView.onSwipeRight()
    }

    override fun onSwipeTop() {
        binding.matrixView.onSwipeUp()
    }

    override fun onSwipeBottom() {
        binding.matrixView.onSwipeDown()
    }

    companion object {
        private const val BEST_SCORE = "BestScore"
    }
}