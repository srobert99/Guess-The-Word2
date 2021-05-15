package com.example.guesstheword

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.guesstheword.utils.Word

class MainActivity : AppCompatActivity() {

    var lives = 3
    var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startGame()
    }

    private fun startGame() {
        val queue = Volley.newRequestQueue(applicationContext)
        val url = "https://random-word-api.herokuapp.com/word?number=1"

        val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val word = Word(response.substring(2, response.length - 2))
                    findViewById<TextView>(R.id.WordTV).text = String(word.fWord)
                    initButtons(word)
                },
                {
                    findViewById<TextView>(R.id.WordTV).text = "Errors"
                })

        queue.add(stringRequest)
    }


    private fun initButtons(word: Word) {
        val wordView = findViewById<TextView>(R.id.WordTV)
        val scoreView = findViewById<TextView>(R.id.ScoreTV)

        for (c in 1..14) {
            val lButton: Button =
                    findViewById(resources.getIdentifier("Letter$c", "id", packageName))
            lButton.setText(word.letters[c - 1].toString())
            lButton.setOnClickListener {
                if (word.guess(c - 1, lButton, wordView) == 0) {
                    val live: ImageView = findViewById(
                            resources.getIdentifier(
                                    "Star$lives" + "IV",
                                    "id",
                                    packageName
                            )
                    )
                    live.visibility = View.INVISIBLE
                    lives--
                    if (!findViewById<ImageView>(R.id.Star1IV).isVisible) {
                        Toast.makeText(applicationContext, "You lose", Toast.LENGTH_SHORT).show()
                        resetGame()
                        score--
                        scoreView.text = "Score: $score"
                        startGame()
                    }
                }
                if (word.verifyGameStatus() == 1) {
                    Toast.makeText(applicationContext, "You win", Toast.LENGTH_SHORT).show()
                    resetGame()
                    startGame()
                    score++
                    scoreView.text = "Score: $score"
                }
            }
        }
    }

    private fun resetGame() {
        for (i in (1..3)) {
            val star: ImageView =
                    findViewById(resources.getIdentifier("Star$i" + "IV", "id", packageName))
            star.visibility = View.VISIBLE
        }
        for (j in (1..14)) {
            val lButton: Button =
                    findViewById(resources.getIdentifier("Letter$j", "id", packageName))
            lButton.visibility = View.VISIBLE
        }
    }

}