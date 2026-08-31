package com.tkachevaekaterina.nihonkana.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R

class ExerciseMenuActivity : AppCompatActivity() {

    private lateinit var btnKanaToRomaji: Button
    private lateinit var btnRomajiToKana: Button
    private lateinit var btnBack: Button

    private var alphabet = "hiragana"
    private var symbolGroup = "all"
    private var selectedRows = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_menu)

        btnKanaToRomaji = findViewById(R.id.btnKanaToRomaji)
        btnRomajiToKana = findViewById(R.id.btnRomajiToKana)
        btnBack = findViewById(R.id.btnBack)

        alphabet = intent.getStringExtra("alphabet") ?: "hiragana"
        symbolGroup = intent.getStringExtra("symbolGroup") ?: "all"
        selectedRows = intent.getStringArrayListExtra("selectedRows") ?: arrayListOf()

        btnKanaToRomaji.setOnClickListener {
            startExercise("kana_to_romaji")
        }

        btnRomajiToKana.setOnClickListener {
            startExercise("romaji_to_kana")
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startExercise(exerciseMode: String) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("alphabet", alphabet)
        intent.putExtra("exerciseMode", exerciseMode)
        intent.putExtra("symbolGroup", symbolGroup)
        intent.putStringArrayListExtra("selectedRows", selectedRows)
        startActivity(intent)
    }
}