package com.mkr.a2048assignment.helper

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class SwipeDetector(private val listener: SwipeListener) : GestureDetector.SimpleOnGestureListener() {

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            if (e1.x - e2.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                listener.onSwipeLeft()
            } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                listener.onSwipeRight()
            } else if (e1.y - e2.y > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                listener.onSwipeTop()
            } else if (e2.y - e1.y > SWIPE_MIN_DISTANCE && abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                listener.onSwipeBottom()
            }
        } catch (e: Exception) {
        }
        return true
    }

    companion object {
        private const val SWIPE_MIN_DISTANCE = 120
        private const val SWIPE_THRESHOLD_VELOCITY = 100
    }
}