package com.tkachevaekaterina.nihonkana

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.exercise.ExerciseSelectionActivity
import com.tkachevaekaterina.nihonkana.exercise.ExerciseActivity

class AlphabetSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alphabet_selection)

        val selectedAlphabet = intent.getStringExtra("alphabet") ?: "hiragana"

        val tvAlphabetTitle = findViewById<TextView>(R.id.tvAlphabetTitle)
        val tvNExplanation = findViewById<TextView>(R.id.tvNExplanation)

        val btnBackToAlphabet = findViewById<Button>(R.id.btnBackToAlphabet)
        val btnRowA = findViewById<Button>(R.id.btnRowA)
        val btnRowK = findViewById<Button>(R.id.btnRowK)
        val btnRowS = findViewById<Button>(R.id.btnRowS)
        val btnRowT = findViewById<Button>(R.id.btnRowT)
        val btnRowN = findViewById<Button>(R.id.btnRowN)
        val btnRowH = findViewById<Button>(R.id.btnRowH)
        val btnRowM = findViewById<Button>(R.id.btnRowM)
        val btnRowY = findViewById<Button>(R.id.btnRowY)
        val btnRowR = findViewById<Button>(R.id.btnRowR)
        val btnRowW = findViewById<Button>(R.id.btnRowW)
        val btnRowNOnly = findViewById<Button>(R.id.btnRowNOnly)
        val btnExercises = findViewById<Button>(R.id.btnExercises) // кнопка для упражнений

        // Настраиваем текст рядов
        if (selectedAlphabet == "hiragana") {
            tvAlphabetTitle.text = "Выберите ряд хираганы"

            btnRowA.text = "あ　い　う　え　お"
            btnRowK.text = "か　き　く　け　こ"
            btnRowS.text = "さ　し　す　せ　そ"
            btnRowT.text = "た　ち　つ　て　と"
            btnRowN.text = "な　に　ぬ　ね　の"
            btnRowH.text = "は　ひ　ふ　へ　ほ"
            btnRowM.text = "ま　み　む　め　も"
            btnRowY.text = "や　ゆ　よ"
            btnRowR.text = "ら　り　る　れ　ろ"
            btnRowW.text = "わ　を"
            btnRowNOnly.text = "ん"

            tvNExplanation.text =
                "Символ ん вынесен отдельно, потому что он не образует стандартный пятигласный ряд, как остальные знаки хираганы."
        } else {
            tvAlphabetTitle.text = "Выберите ряд катаканы"

            btnRowA.text = "ア　イ　ウ　エ　オ"
            btnRowK.text = "カ　キ　ク　ケ　コ"
            btnRowS.text = "サ　シ　ス　セ　ソ"
            btnRowT.text = "タ　チ　ツ　テ　ト"
            btnRowN.text = "ナ　ニ　ヌ　ネ　ノ"
            btnRowH.text = "ハ　ヒ　フ　ヘ　ホ"
            btnRowM.text = "マ　ミ　ム　メ　モ"
            btnRowY.text = "ヤ　ユ　ヨ"
            btnRowR.text = "ラ　リ　ル　レ　ロ"
            btnRowW.text = "ワ　ヲ"
            btnRowNOnly.text = "ン"

            tvNExplanation.text =
                "Символ ン вынесен отдельно, потому что он также не образует стандартный пятигласный ряд, как остальные знаки катаканы."
        }

        // Обработчики кнопок
        btnBackToAlphabet.setOnClickListener { finish() }
        btnRowA.setOnClickListener { openRow(selectedAlphabet, "a") }
        btnRowK.setOnClickListener { openRow(selectedAlphabet, "k") }
        btnRowS.setOnClickListener { openRow(selectedAlphabet, "s") }
        btnRowT.setOnClickListener { openRow(selectedAlphabet, "t") }
        btnRowN.setOnClickListener { openRow(selectedAlphabet, "n") }
        btnRowH.setOnClickListener { openRow(selectedAlphabet, "h") }
        btnRowM.setOnClickListener { openRow(selectedAlphabet, "m") }
        btnRowY.setOnClickListener { openRow(selectedAlphabet, "y") }
        btnRowR.setOnClickListener { openRow(selectedAlphabet, "r") }
        btnRowW.setOnClickListener { openRow(selectedAlphabet, "w") }
        btnRowNOnly.setOnClickListener { openRow(selectedAlphabet, "n_special") }

        // Обработчик кнопки упражнений
        btnExercises.setOnClickListener {
            val intent = Intent(this, ExerciseSelectionActivity::class.java)
            intent.putExtra("alphabet", selectedAlphabet)
            startActivity(intent)
        }
    }

    private fun openRow(alphabet: String, row: String) {
        val intent = Intent(this, KanaCardActivity::class.java)
        intent.putExtra("alphabet", alphabet)
        intent.putExtra("row", row)
        startActivity(intent)
    }
}