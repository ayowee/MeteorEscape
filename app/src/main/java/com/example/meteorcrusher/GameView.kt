package com.example.meteorescape

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.example.meteorcrusher.R

@SuppressLint("ViewConstructor")
class GameView(var c : Context, var gameTask: GameTask): View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 1
    private var score = 0
    private var myShipPosition = 0
    private val otherMeteors = ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0
    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 +speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherMeteors.add(map)
        }

        time = time + 10 + speed
        val meteorWidth = viewWidth / 5
        val meteorHeight = meteorWidth / 5
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.meteor, null)

        d.setBounds(
            myShipPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - meteorHeight,
            myShipPosition * viewWidth / 3 + viewWidth / 15 + meteorWidth - 25,
            viewHeight - 2
        )

        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in otherMeteors.indices){
            try {
                val meteorX = otherMeteors[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var meteorY = time - otherMeteors[i]["startTime"] as Int
                var d2 = resources.getDrawable(R.drawable.meteor, null)

                d2.setBounds(
                    meteorX + 25,
                    meteorY - meteorHeight,
                    meteorX + meteorWidth - 25,
                    meteorY
                )

                d2.draw(canvas)
                if (otherMeteors[i]["lane"] as Int == myShipPosition){
                    if (meteorY > viewHeight - 2 - meteorHeight && meteorY < viewHeight - 2){
                        gameTask.closeGame(score)
                    }
                }

                if(meteorY > viewHeight + meteorHeight){
                    otherMeteors.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)

                    if (score > highScore){
                        highScore = score
                    }
                }
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                if (x < viewWidth/2){
                    if (myShipPosition > 0){
                        myShipPosition--
                    }
                }
                if (x > viewWidth/2){
                    if (myShipPosition < 2){
                        myShipPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {}
        }
        return true
    }


}