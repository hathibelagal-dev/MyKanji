package io.github.hathibelagal.kanjitrainer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var kanjiItems: KanjiItems

    private val answer1: TextView by lazy { findViewById(R.id.answer_choice_1) }
    private val answer2: TextView by lazy { findViewById(R.id.answer_choice_2) }
    private val answer3: TextView by lazy { findViewById(R.id.answer_choice_3) }
    private val answer4: TextView by lazy { findViewById(R.id.answer_choice_4) }

    private lateinit var correctAnswer: String
    private lateinit var currentKanji: String
    private lateinit var vibrator: Vibrator

    private lateinit var correctSound: MediaPlayer
    private lateinit var incorrectSound: MediaPlayer

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        kanjiItems = KanjiItems(this)
        kanjiItems.loadKanjiFromJSON()

        showKanji()

        answer1.setOnClickListener {
            isCorrectAnswer(answer1.text.toString())
        }
        answer2.setOnClickListener {
            isCorrectAnswer(answer2.text.toString())
        }
        answer3.setOnClickListener {
            isCorrectAnswer(answer3.text.toString())
        }
        answer4.setOnClickListener {
            isCorrectAnswer(answer4.text.toString())
        }

        correctSound = MediaPlayer.create(this, R.raw.correct)
        incorrectSound = MediaPlayer.create(this, R.raw.incorrect)

    }

    private fun playSound(correct: Boolean) {
        if(correct) {
            correctSound.seekTo(0)
            correctSound.start()
        } else {
            incorrectSound.seekTo(0)
            incorrectSound.start()
        }
    }

    private fun isCorrectAnswer(answer: String) {
        playSound(answer == correctAnswer)
        if(answer != correctAnswer) {
            kanjiItems.resetNSeen(currentKanji)
            vibrator.vibrate(400)
        }
        showKanji()
    }

    private fun showKanji() {
        val kanji = kanjiItems.getRandKanji()
        findViewById<TextView>(R.id.kanji_question).text = kanji.kanji
        val nSeenText = "Streak: ${kanji.nSeen + 1}"
        findViewById<TextView>(R.id.n_seen).text = nSeenText

        val meanings = kanjiItems.getRandomMeanings(kanji.kanji, 3)
        meanings.add(kanji.meaning)
        meanings.shuffle()

        correctAnswer = kanji.meaning
        currentKanji = kanji.kanji

        if(kanji.nSeen == 0) {
            answer1.text = kanji.meaning
            findViewById<LinearLayout>(R.id.other_answers).visibility = View.GONE
            findViewById<TextView>(R.id.answer_title).text = getString(R.string.correct_answer_is)
        } else {
            findViewById<TextView>(R.id.answer_title).text = getString(R.string.pick_an_answer)
            findViewById<LinearLayout>(R.id.other_answers).visibility = View.VISIBLE
            answer1.text = meanings[0]
            answer2.text = meanings[1]
            answer3.text = meanings[2]
            answer4.text = meanings[3]
        }

        kanjiItems.incrementNSeen(kanji.kanji)
    }
}