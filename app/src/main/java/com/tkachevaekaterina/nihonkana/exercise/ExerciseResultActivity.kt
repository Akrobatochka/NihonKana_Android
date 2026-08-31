package com.tkachevaekaterina.nihonkana.exercise

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R

class ExerciseResultActivity : AppCompatActivity() {

    private lateinit var tvResultTitle: TextView
    private lateinit var tvResultInfo: TextView
    private lateinit var btnExitResult: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_result)

        tvResultTitle = findViewById(R.id.tvResultTitle)
        tvResultInfo = findViewById(R.id.tvResultInfo)
        btnExitResult = findViewById(R.id.btnExitResult)

        val correctCount = intent.getIntExtra("correctCount", 0)
        val wrongCount = intent.getIntExtra("wrongCount", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val percent = intent.getIntExtra("percent", 0)

        tvResultTitle.text = "Упражнение\nзавершено"

        tvResultInfo.text =
            "Результат: $correctCount из $totalQuestions\n" +
                    "Ошибок: $wrongCount\n" +
                    "Правильных ответов: $percent%"

        btnExitResult.setOnClickListener {
            finish()
        }
    }
}