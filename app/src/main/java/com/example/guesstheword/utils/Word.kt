package com.example.guesstheword.utils

import android.view.View
import android.widget.Button
import android.widget.TextView

class Word(word: String) {

    var answer = getAnswer(word)
    var letters = addLetters(getLetters(word))
    var fWord = formatWord(word)


    private fun getLetters(word: String): MutableList<Char> {
        var letters = mutableListOf<Char>()
        for (c in word) {
            if (!(letters.contains(c)) && c != word[0] && c != word[word.length - 1]) {
                letters.add(c)
            }
        }
        return letters
    }


    private fun getAnswer(word: String): String {
        var answer = ""
        for (c in word) {
            answer += c + " "
        }
        return answer.substring(0, answer.length - 1)
    }


    private fun formatWord(word: String): CharArray {
        var fWord = ""
        for (c in word) {
            if (c != word[0] && c != word[word.length - 1]) {
                fWord += "_ "
            } else {
                fWord += c + " "
            }
        }
        return fWord.substring(0, fWord.length - 1).toCharArray()
    }


    private fun addLetters(letters: MutableList<Char>): MutableList<Char> {
        var lettersAux = letters
        while (lettersAux.size < 15) {
            val letter = ('a'..'z').random()
            if (!lettersAux.contains(letter) && letter != answer[0] && letter != answer[answer.length - 1]) {
                lettersAux.add(letter)
            }
        }
        lettersAux.shuffle()
        return lettersAux
    }

    fun guess(poz: Int, buttonView: Button, wordView: TextView): Int {
        var ok = false
        buttonView.visibility = View.INVISIBLE
        for (c in (0..answer.length - 1)) {
            if (answer[c] == letters[poz] && fWord[c] == '_') {
                fWord[c] = letters[poz]
                ok = true
            }
        }
        if (ok) {
            wordView.text = String(fWord)
            return 1
        } else {
            return 0
        }
    }

    fun verifyGameStatus(): Int {
        if (String(fWord) == answer) {
            return 1
        }
        return 0
    }

}
