import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.example.meteorcrusher.GameTask
import com.example.meteorcrusher.GameViewModel
import com.example.meteorcrusher.R
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class GameView(
    c: Context,
    private var gameTask: GameTask,
    viewModel: GameViewModel,
    private val sharedPreferences: SharedPreferences
) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 1
    private var score = 0
    private var myShipPosition = 0
    private val otherMeteors = ArrayList<HashMap<String, Any>>()

    private var viewWidth = 0
    private var viewHeight = 0

    init {
        myPaint = Paint()
    }

    @SuppressLint("DrawAllocation", "UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherMeteors.add(map)
        }

        time += 10 + speed
        val meteorWidth = 200
        val meteorHeight = 200
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.ship, null)

        d.setBounds(
            myShipPosition * viewWidth / 3,
            viewHeight - 2 - meteorHeight,
            myShipPosition * viewWidth / 3 + meteorWidth,
            viewHeight - 2
        )

        d.draw(canvas)
        myPaint!!.color = Color.GREEN

        // Retrieve high score from SharedPreferences
        val highScore = sharedPreferences.getInt(PREF_HIGH_SCORE_KEY, 0)

        for (i in otherMeteors.indices) {
            try {
                val meteorX = otherMeteors[i]["lane"] as Int * viewWidth / 3
                val meteorY = time - otherMeteors[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.meteor, null)

                d2.setBounds(
                    meteorX,
                    meteorY,
                    meteorX + meteorWidth,
                    meteorY + meteorHeight
                )

                d2.draw(canvas)
                if (otherMeteors[i]["lane"] as Int == myShipPosition) {
                    if (meteorY > viewHeight - 2 - meteorHeight && meteorY < viewHeight - 2) {
                        gameTask.closeGame(score)
                    }
                }

                if (meteorY > viewHeight + meteorHeight) {
                    otherMeteors.removeAt(i)
                    score++
                    speed = 1 + abs(score / 8)

                    // Update high score if necessary
                    if (score > highScore) {
                        sharedPreferences.edit().putInt(PREF_HIGH_SCORE_KEY, score).apply()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint!!)
        canvas.drawText("High Score: $highScore", 680f, 80f, myPaint!!)
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                if (x < viewWidth / 2) {
                    if (myShipPosition > 0) {
                        myShipPosition--
                    }
                }
                if (x > viewWidth / 2) {
                    if (myShipPosition < 2) {
                        myShipPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }

    companion object {
        private const val PREF_HIGH_SCORE_KEY = "high_score"
    }
}
