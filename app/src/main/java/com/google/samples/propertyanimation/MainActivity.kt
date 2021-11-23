/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.ImageViewCompat


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    private var rotateAnimator: ObjectAnimator?= null
    private var translateAnimator: ObjectAnimator?= null
    private var scaleAnimator: ObjectAnimator?= null
    private var fadeAnimator: ObjectAnimator?= null
    private var colorizeAnimator: ObjectAnimator?= null
    private var showerAnimator: ObjectAnimator?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun ObjectAnimator.disableButtonnAnimation(btn: Button) {
        addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationStart(
                animation: Animator?
            ) {
                btn.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                btn.isEnabled = true
            }
        })
    }

    @SuppressLint("Recycle")
    private fun rotater() {
        (rotateAnimator ?: ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f).apply {
            duration = 1000
            rotateAnimator = this
            disableButtonnAnimation(rotateButton)
        }).start()
    }

    private fun translater() {
        (translateAnimator ?: ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 200f).apply {
            duration = 1000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = 1
            translateAnimator = this
            disableButtonnAnimation(translateButton)
        }).start()
    }

    private fun scaler() {
        (scaleAnimator ?: run {
            val xScale = PropertyValuesHolder.ofFloat(View.SCALE_X, 5f)
            val yScale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 5f)
            ObjectAnimator.ofPropertyValuesHolder(star, xScale, yScale).apply {
                repeatMode = ObjectAnimator.REVERSE
                repeatCount = 1
                duration = 1000
                scaleAnimator = this
                disableButtonnAnimation(scaleButton)
            }
        }).start()
    }

    private fun fader() {
        (fadeAnimator ?: ObjectAnimator.ofFloat(star, View.ALPHA, 0f).apply {
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = 1
            duration = 1000
            fadeAnimator = this
            disableButtonnAnimation(fadeButton)
        }).start()
    }

    private fun colorizer() {
        (colorizeAnimator ?: ObjectAnimator.ofArgb(star, "backgroundColor", Color.BLACK, Color.RED).apply {
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = 1
            duration = 1000
            colorizeAnimator = this
            disableButtonnAnimation(colorizeButton)
        }).start()
    }

    private fun shower() {
        val xTranslateDirection = (Math.random().toFloat() - .5f) *2 //* 10f - 5f

        fun ViewGroup.addStarAnim(delay: Long = 0) {
            val starScale = Math.random().toFloat() *3f +.1f
            val xSpawn = Math.random().toFloat() * width
            val newStar = AppCompatImageView(context).apply {
                setImageResource(R.drawable.ic_star)
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                scaleX = starScale
                scaleY = starScale

                y = -(height * starScale * 2)
                x = xSpawn
                addView(this)
            }

            val fallingY = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_Y,
                //-(newStar.height * starScale * 2),
                -20f,
                height + newStar.height *starScale
            )
            val fallingX = PropertyValuesHolder.ofFloat(
                View.TRANSLATION_X,
                xSpawn,
                xSpawn + height * xTranslateDirection
            )
            val fallingAnim = ObjectAnimator.ofPropertyValuesHolder(newStar, fallingX, fallingY).apply {
                //duration = 1000
                interpolator = AccelerateInterpolator(Math.random().toFloat() *2f +.5f)
            }

            val rotationAnim = ObjectAnimator.ofFloat(newStar, View.ROTATION, 360f).apply {
                //duration = 1000
                interpolator = LinearInterpolator()
            }

            AnimatorSet().apply {
                playTogether(fallingAnim, rotationAnim)
                duration = (Math.random() *1500 +500).toLong()
                startDelay = delay
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(
                        animation: Animator?
                    ) {
                        removeView(newStar)
                    }
                })
            }.start()
        }

        repeat(10) {
            (star.parent as ViewGroup).addStarAnim() //it *100L
        }
    }

}
