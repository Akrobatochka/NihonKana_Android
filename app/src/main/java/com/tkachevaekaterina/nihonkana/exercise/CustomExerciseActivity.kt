package com.tkachevaekaterina.nihonkana.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R

class CustomExerciseActivity : AppCompatActivity() {

    private lateinit var tvCustomExerciseTitle: TextView
    private lateinit var checkboxContainer: LinearLayout
    private lateinit var btnStartCustomExercise: Button
    private lateinit var btnBack: Button

    private var alphabet = "hiragana"

    private val rowLabels = mapOf(
        "a" to "Ряд あ / ア",
        "k" to "Ряд か / カ",
        "s" to "Ряд さ / サ",
        "t" to "Ряд た / タ",
        "n" to "Ряд な / ナ",
        "h" to "Ряд は / ハ",
        "m" to "Ряд ま / マ",
        "y" to "Ряд や / ヤ",
        "r" to "Ряд ら / ラ",
        "w" to "Ряд わ / ワ",
        "n_special" to "Символ ん / ン",

        "dakuten_k" to "Озвонченный ряд が / ガ",
        "dakuten_s" to "Озвонченный ряд ざ / ザ",
        "dakuten_t" to "Озвонченный ряд だ / ダ",
        "dakuten_h" to "Озвонченный ряд ば / バ",
        "handakuten_h" to "Полуозвонченный ряд ぱ / パ",

        "combination_k" to "Сочетания きゃ / キャ",
        "combination_s" to "Сочетания しゃ / シャ",
        "combination_t" to "Сочетания ちゃ / チャ",
        "combination_n" to "Сочетания にゃ / ニャ",
        "combination_h" to "Сочетания ひゃ / ヒャ",
        "combination_m" to "Сочетания みゃ / ミャ",
        "combination_r" to "Сочетания りゃ / リャ",

        "combination_dakuten" to "Озвонченные сочетания",
        "combination_handakuten" to "Полуозвонченные сочетания"
    )

    private val orderedRows = listOf(
        "a", "k", "s", "t", "n", "h", "m", "y", "r", "w", "n_special",
        "dakuten_k", "dakuten_s", "dakuten_t", "dakuten_h", "handakuten_h",
        "combination_k", "combination_s", "combination_t", "combination_n",
        "combination_h", "combination_m", "combination_r",
        "combination_dakuten", "combination_handakuten"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_exercise)

        tvCustomExerciseTitle = findViewById(R.id.tvCustomExerciseTitle)
        checkboxContainer = findViewById(R.id.checkboxContainer)
        btnStartCustomExercise = findViewById(R.id.btnStartCustomExercise)
        btnBack = findViewById(R.id.btnBack)

        alphabet = intent.getStringExtra("alphabet") ?: "hiragana"

        tvCustomExerciseTitle.text = if (alphabet == "hiragana") {
            "Настраиваемая тренировка: хирагана"
        } else {
            "Настраиваемая тренировка: катакана"
        }

        createCheckboxes()

        btnStartCustomExercise.setOnClickListener {
            val selectedRows = getSelectedRows()

            if (selectedRows.isEmpty()) {
                Toast.makeText(
                    this,
                    "Выберите хотя бы один ряд",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val intent = Intent(this, ExerciseMenuActivity::class.java)
                intent.putExtra("alphabet", alphabet)
                intent.putExtra("symbolGroup", "custom")
                intent.putStringArrayListExtra("selectedRows", ArrayList(selectedRows))
                startActivity(intent)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun createCheckboxes() {
        orderedRows.forEach { rowName ->
            val checkBox = CheckBox(this)
            checkBox.text = rowLabels[rowName] ?: rowName
            checkBox.tag = rowName
            checkBox.textSize = 18f
            checkBox.setPadding(0, 8, 0, 8)

            checkboxContainer.addView(checkBox)
        }
    }

    private fun getSelectedRows(): List<String> {
        val selectedRows = mutableListOf<String>()

        for (i in 0 until checkboxContainer.childCount) {
            val view = checkboxContainer.getChildAt(i)

            if (view is CheckBox && view.isChecked) {
                selectedRows.add(view.tag.toString())
            }
        }

        return selectedRows
    }
}