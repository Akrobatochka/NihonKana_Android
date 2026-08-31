package com.tkachevaekaterina.nihonkana.grammar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R

class GrammarMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_menu)

        val btnHiraganaGrammar = findViewById<Button>(R.id.btnHiraganaGrammar)
        val btnKatakanaGrammar = findViewById<Button>(R.id.btnKatakanaGrammar)
        val btnAdditionalSoundsGrammar = findViewById<Button>(R.id.btnAdditionalSoundsGrammar)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnHiraganaGrammar.setOnClickListener {
            openGrammarInfo("hiragana")
        }

        btnKatakanaGrammar.setOnClickListener {
            openGrammarInfo("katakana")
        }

        btnAdditionalSoundsGrammar.setOnClickListener {
            openGrammarInfo("additional")
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun openGrammarInfo(section: String) {
        val intent = Intent(this, GrammarInfoActivity::class.java)
        intent.putExtra("section", section)
        startActivity(intent)
    }
}