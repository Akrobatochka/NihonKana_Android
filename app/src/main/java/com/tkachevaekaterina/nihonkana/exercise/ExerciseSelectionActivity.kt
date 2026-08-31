package com.tkachevaekaterina.nihonkana.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R

class ExerciseSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_selection)

        val selectedAlphabet = intent.getStringExtra("alphabet") ?: "hiragana"

        val tvExerciseTitle = findViewById<TextView>(R.id.tvExerciseTitle)
        val btnBackToRows = findViewById<Button>(R.id.btnBackToRows)
        val btnReadingExercise = findViewById<Button>(R.id.btnReadingExercise)
        val btnBasicExercise = findViewById<Button>(R.id.btnBasicExercise)
        val btnAdditionalExercise = findViewById<Button>(R.id.btnAdditionalExercise)
        val btnCustomExercise = findViewById<Button>(R.id.btnCustomExercise)

        tvExerciseTitle.text = if (selectedAlphabet == "hiragana") {
            "Упражнения: хирагана"
        } else {
            "Упражнения: катакана"
        }

        btnBackToRows.setOnClickListener {
            finish()
        }

        btnReadingExercise.setOnClickListener {
            openExerciseMenu(selectedAlphabet, "all")
        }

        btnBasicExercise.setOnClickListener {
            openExerciseMenu(selectedAlphabet, "basic")
        }

        btnAdditionalExercise.setOnClickListener {
            openExerciseMenu(selectedAlphabet, "additional")
        }

        btnCustomExercise.setOnClickListener {
            val intent = Intent(this, CustomExerciseActivity::class.java)
            intent.putExtra("alphabet", selectedAlphabet)
            startActivity(intent)
        }
    }

    private fun openExerciseMenu(alphabet: String, symbolGroup: String) {
        val intent = Intent(this, ExerciseMenuActivity::class.java)
        intent.putExtra("alphabet", alphabet)
        intent.putExtra("symbolGroup", symbolGroup)
        startActivity(intent)
    }
}