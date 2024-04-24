package com.example.meteorcrusher

import GameView
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: RelativeLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var score: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootLayout = findViewById(R.id.rootLayout)
        startBtn = findViewById(R.id.startBtn)
        score = findViewById(R.id.score)
        mGameView = GameView(this,this)

        startBtn.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.background)
            rootLayout.addView(mGameView)
            startBtn.visibility = View.GONE
            score.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    override fun closeGame(mScore: Int) {
        score.text = "Score: $mScore"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
    }
}