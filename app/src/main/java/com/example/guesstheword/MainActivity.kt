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

private var lives=3
private var characters = mutableListOf<Char>()
private var score=0
var answer=""

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startGame()
    }


    private fun startGame(){
        val queue = Volley.newRequestQueue(this)
        val url = "https://random-word-api.herokuapp.com/word?number=1"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            {response ->
                generateWordAspect(response.substring(2,response.length-2))
                generateList()
                initButtonsFin()
            },
            {
                findViewById<TextView>(R.id.WordTV).text = "Errors"
            })

        queue.add(stringRequest)
    }


    private fun generateWordAspect(word: String){
        var aux=""
        for(c in word){
            if(c != word[0] && c != word[word.length-1]){
                if(!characters.contains(c)){
                    characters.add(c)
                }
                aux+="_ "
            }else{
                aux+=c + " "
            }
            answer+=c + " "
        }
        findViewById<TextView>(R.id.WordTV).text=aux
    }


    private fun generateList(){
        while(characters.size<14){
            val letter = ('a'..'z').random()
            if(!characters.contains(letter) && !answer.contains(letter)){
                characters.add(letter)
            }
        }
    }


    private fun initButtons(nr: Int, bChar: Char){
        val lButton:Button=findViewById(resources.getIdentifier("Letter$nr","id",packageName))
        lButton.setText(bChar.toString())
        lButton.setOnClickListener {
            guess(lButton.text.toString().toCharArray()[0],lButton)
        }
    }


    private fun initButtonsFin(){
        characters.shuffle()
        for(i in 1..14){
            initButtons(i, characters.get(i-1))
        }
    }


    private fun guess(letter:Char, view: TextView){
        var guessWord = findViewById<TextView>(R.id.WordTV).text.toString().toCharArray()
        val length = (answer.length)-2
        if(answer.contains(letter)){
            for(c in (0..length-2)){
                if(guessWord[c] == '_' && answer[c] == letter){
                    guessWord[c] = letter
                }
            }

            findViewById<TextView>(R.id.WordTV).text=String(guessWord)

        }else{
            val star:ImageView = findViewById(resources.getIdentifier("Star$lives"+"IV","id",packageName))
            star.visibility=(View.INVISIBLE)
            lives--
        }
        view.visibility=View.INVISIBLE

        if(lives==0){
            endGame(0)
        }else if(String(guessWord) == answer){
            endGame(1)
        }

    }


    private fun endGame(win : Int){
        if(win==1){
            Toast.makeText(applicationContext,"You won",Toast.LENGTH_SHORT).show()
            score++
        }else{
            Toast.makeText(applicationContext,"You lose",Toast.LENGTH_SHORT).show()
            score--
        }
        findViewById<TextView>(R.id.ScoreTV).text="Score: $score"
        resetGame()
    }


    private fun resetGame(){
        lives = 3
        characters.clear()
        for(i in (1..14)){
            val letters: Button=findViewById(resources.getIdentifier("Letter$i","id",packageName))
            if(!letters.isVisible){
                letters.visibility=View.VISIBLE
            }
        }
        for(j in (1..3)){
            val star: ImageView=findViewById(resources.getIdentifier("Star$j"+"IV","id",packageName))
            star.visibility=View.VISIBLE
        }
        answer=""
        startGame()
    }



}


