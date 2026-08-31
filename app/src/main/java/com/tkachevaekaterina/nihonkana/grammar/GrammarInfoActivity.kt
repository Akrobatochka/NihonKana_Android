package com.tkachevaekaterina.nihonkana.grammar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R

class GrammarInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grammar_info)

        val tvGrammarTitle = findViewById<TextView>(R.id.tvGrammarTitle)
        val tvGrammarText = findViewById<TextView>(R.id.tvGrammarText)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val section = intent.getStringExtra("section") ?: "hiragana"

        when (section) {
            "hiragana" -> {
                tvGrammarTitle.text = "Хирагана"
                tvGrammarText.text = getHiraganaText()
            }

            "katakana" -> {
                tvGrammarTitle.text = "Катакана"
                tvGrammarText.text = getKatakanaText()
            }

            "additional" -> {
                tvGrammarTitle.text = "Дополнительные звуки"
                tvGrammarText.text = getAdditionalSoundsText()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getHiraganaText(): String {
        return """
            Хирагана — одна из двух японских слоговых азбук. Она используется для записи японских слов, грамматических окончаний, частиц и вспомогательных элементов предложения.

            Каждый символ хираганы обозначает не отдельную букву, а слог. Например, символ か читается как «ka», み — как «mi», а の — как «no».

            При изучении хираганы важно обращать внимание на форму символа, его произношение и связь с рядом, к которому он относится. Символы сгруппированы по рядам: あ, か, さ, た, な и другим.

            Хирагана является базовой азбукой для начинающих, поэтому её изучение рекомендуется начинать первой.
        """.trimIndent()
    }

    private fun getKatakanaText(): String {
        return """
            Катакана — вторая японская слоговая азбука. По звучанию она соответствует хирагане, но имеет другое графическое написание.

            Катакана чаще всего используется для записи иностранных слов, заимствований, имён, названий, а также для выделения слов в тексте.

            Например, слово «camera» в японском языке записывается катаканой как カメラ.

            При изучении катаканы важно сравнивать её с хираганой, так как многие слоги имеют одинаковое произношение, но разные символы.
        """.trimIndent()
    }

    private fun getAdditionalSoundsText(): String {
        return """
            Дополнительные звуки образуются на основе базовых символов хираганы и катаканы.

            Дакутэн — это две маленькие черточки, которые добавляются к символу и изменяют его звучание. Например, か превращается в が, а さ — в ざ.

            Хандакутэн — это маленький кружок, который используется с рядом は. Например, は превращается в ぱ.

            Также в японском языке используются сочетания с маленькими ゃ, ゅ, ょ. Например, き + ゃ образует きゃ, что читается как «kya».

            Такие символы позволяют передавать больше звуков и являются важной частью японской письменности.
        """.trimIndent()
    }
}