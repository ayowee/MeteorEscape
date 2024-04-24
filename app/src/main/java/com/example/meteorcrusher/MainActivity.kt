package com.example.meteorcrusher

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: RelativeLayout
    private lateinit var startBtn: Button
    private lateinit var continueBtn: Button
    private lateinit var scoreTextView: TextView
    private lateinit var highScoreTextView: TextView
    private lateinit var gameView: GameView
    private lateinit var viewModel: GameViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var logo: ImageView

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootLayout = findViewById(R.id.rootLayout)
        startBtn = findViewById(R.id.startBtn)
        continueBtn = findViewById(R.id.continueBtn)
        scoreTextView = findViewById(R.id.score)
        highScoreTextView = findViewById(R.id.highScoreTextView)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        logo = findViewById(R.id.logo)

        viewModel = GameViewModel()
        continueBtn.visibility = View.GONE

        startBtn.setOnClickListener {
            if (!::gameView.isInitialized) {
                gameView = GameView(this, this, sharedPreferences)
                rootLayout.addView(gameView)
            }
            startBtn.visibility = View.GONE
            scoreTextView.visibility = View.GONE
            logo.visibility = View.GONE
            continueBtn.visibility = View.GONE
            highScoreTextView.visibility = View.GONE
        }

        continueBtn.setOnClickListener {
            gameView = GameView(this, this, sharedPreferences)
            rootLayout.addView(gameView)
            startBtn.visibility = View.GONE
            scoreTextView.visibility = View.GONE
            logo.visibility = View.GONE
            continueBtn.visibility = View.GONE
            highScoreTextView.visibility = View.GONE
        }

        // Load and display the high score
        val highScore = sharedPreferences.getInt(PREF_HIGH_SCORE_KEY, 0)
        highScoreTextView.text = "High Score: $highScore"
    }

    @SuppressLint("SetTextI18n")
    override fun closeGame(mScore: Int) {
        // Update and display the high score if the current score is higher
        val highScore = sharedPreferences.getInt(PREF_HIGH_SCORE_KEY, 0)
        if (mScore > highScore) {
            sharedPreferences.edit().putInt(PREF_HIGH_SCORE_KEY, mScore).apply()
            highScoreTextView.text = "High Score: $mScore"
        }

        scoreTextView.text = "Score: $mScore"
        rootLayout.removeView(gameView)
        startBtn.visibility = View.GONE
        continueBtn.visibility = View.VISIBLE
        scoreTextView.visibility = View.VISIBLE
        highScoreTextView.visibility = View.VISIBLE
        logo.visibility = View.VISIBLE
    }

    companion object {
        private const val PREF_HIGH_SCORE_KEY = "high_score"
    }
}
