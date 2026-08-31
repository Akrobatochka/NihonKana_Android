package com.tkachevaekaterina.nihonkana.progress

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R
import com.tkachevaekaterina.nihonkana.data.DatabaseProvider
import com.tkachevaekaterina.nihonkana.data.MistakeInfo
import com.tkachevaekaterina.nihonkana.data.RowProgressInfo
import com.tkachevaekaterina.nihonkana.data.SymbolProgressInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProgressActivity : AppCompatActivity() {

    private lateinit var tvProgressTitle: TextView
    private lateinit var tvTotalExercises: TextView
    private lateinit var tvAveragePercent: TextView
    private lateinit var tvCorrectAnswers: TextView
    private lateinit var tvWrongAnswers: TextView
    private lateinit var tvLearnedSymbols: TextView
    private lateinit var btnBackFromProgress: Button

    private lateinit var tvRowsHeader: TextView
    private lateinit var tvSymbolsHeader: TextView
    private lateinit var tvMistakesHeader: TextView

    private lateinit var layoutRowsProgress: LinearLayout
    private lateinit var layoutSymbolsProgress: LinearLayout
    private lateinit var layoutMistakesProgress: LinearLayout

    private var isRowsVisible = false
    private var isSymbolsVisible = false
    private var isMistakesVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        tvProgressTitle = findViewById(R.id.tvProgressTitle)
        tvTotalExercises = findViewById(R.id.tvTotalExercises)
        tvAveragePercent = findViewById(R.id.tvAveragePercent)
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers)
        tvWrongAnswers = findViewById(R.id.tvWrongAnswers)
        tvLearnedSymbols = findViewById(R.id.tvLearnedSymbols)
        btnBackFromProgress = findViewById(R.id.btnBackFromProgress)

        tvRowsHeader = findViewById(R.id.tvRowsHeader)
        tvSymbolsHeader = findViewById(R.id.tvSymbolsHeader)
        tvMistakesHeader = findViewById(R.id.tvMistakesHeader)

        layoutRowsProgress = findViewById(R.id.layoutRowsProgress)
        layoutSymbolsProgress = findViewById(R.id.layoutSymbolsProgress)
        layoutMistakesProgress = findViewById(R.id.layoutMistakesProgress)

        layoutRowsProgress.visibility = LinearLayout.GONE
        layoutSymbolsProgress.visibility = LinearLayout.GONE
        layoutMistakesProgress.visibility = LinearLayout.GONE

        tvRowsHeader.setOnClickListener {
            isRowsVisible = !isRowsVisible
            layoutRowsProgress.visibility =
                if (isRowsVisible) LinearLayout.VISIBLE else LinearLayout.GONE
            tvRowsHeader.text =
                if (isRowsVisible) "Прогресс по рядам ▲" else "Прогресс по рядам ▼"
        }

        tvSymbolsHeader.setOnClickListener {
            isSymbolsVisible = !isSymbolsVisible
            layoutSymbolsProgress.visibility =
                if (isSymbolsVisible) LinearLayout.VISIBLE else LinearLayout.GONE
            tvSymbolsHeader.text =
                if (isSymbolsVisible) "Прогресс по символам ▲" else "Прогресс по символам ▼"
        }

        tvMistakesHeader.setOnClickListener {
            isMistakesVisible = !isMistakesVisible
            layoutMistakesProgress.visibility =
                if (isMistakesVisible) LinearLayout.VISIBLE else LinearLayout.GONE
            tvMistakesHeader.text =
                if (isMistakesVisible) "Частые ошибки ▲" else "Частые ошибки ▼"
        }

        btnBackFromProgress.setOnClickListener {
            finish()
        }

        loadProgress()
    }

    private fun loadProgress() {
        val db = DatabaseProvider.getDatabase(this)
        val progressDao = db.progressDao()

        CoroutineScope(Dispatchers.IO).launch {
            val totalExercises = progressDao.getSessionsCount()
            val averagePercent = progressDao.getAveragePercent() ?: 0.0
            val totalCorrect = progressDao.getTotalCorrectAnswers() ?: 0
            val totalWrong = progressDao.getTotalWrongAnswers() ?: 0
            val learnedSymbols = progressDao.getLearnedSymbolsCount()

            val rowProgress = progressDao.getRowProgressInfo()
            val symbolProgress = progressDao.getSymbolProgressInfo()
            val topMistakes = progressDao.getTopMistakes()

            runOnUiThread {
                tvTotalExercises.text = "Пройдено упражнений: $totalExercises"
                tvAveragePercent.text = "Средний результат: ${averagePercent.toInt()}%"
                tvCorrectAnswers.text = "Правильных ответов: $totalCorrect"
                tvWrongAnswers.text = "Ошибок: $totalWrong"
                tvLearnedSymbols.text = "Изучено символов: $learnedSymbols"

                layoutRowsProgress.removeAllViews()
                layoutSymbolsProgress.removeAllViews()
                layoutMistakesProgress.removeAllViews()

                rowProgress.forEach { row ->
                    addRowProgressCard(row)
                }

                symbolProgress.forEach { symbol ->
                    addSymbolProgressCard(symbol)
                }

                if (topMistakes.isEmpty()) {
                    addEmptyMistakesCard()
                } else {
                    topMistakes.forEach { mistake ->
                        addMistakeCard(mistake)
                    }
                }
            }
        }
    }

    private fun addRowProgressCard(row: RowProgressInfo) {
        val alphabetName = if (row.alphabet == "hiragana") "Хирагана" else "Катакана"

        val card = TextView(this)
        card.text =
            "$alphabetName — ряд ${row.rowName}\n" +
                    "Изучено: ${row.learnedCount} из ${row.symbolCount}\n" +
                    "Освоение: ${row.averageMastery.toInt()}%"

        card.textSize = 18f
        card.setTextColor(Color.parseColor("#7B585A"))
        card.setBackgroundColor(Color.parseColor("#FEFFAF"))
        card.setPadding(24, 20, 24, 20)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 16)

        layoutRowsProgress.addView(card, params)
    }

    private fun addSymbolProgressCard(symbol: SymbolProgressInfo) {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.HORIZONTAL
        card.gravity = Gravity.CENTER_VERTICAL
        card.setBackgroundColor(Color.parseColor("#FFFFFF"))
        card.setPadding(32, 24, 32, 24)

        val glyphView = TextView(this)
        glyphView.text = symbol.glyph
        glyphView.textSize = if (symbol.glyph.length > 1) 40f else 54f
        glyphView.setTextColor(Color.parseColor("#7B585A"))
        glyphView.gravity = Gravity.CENTER
        glyphView.includeFontPadding = false
        glyphView.maxLines = 1

        val glyphParams = LinearLayout.LayoutParams(
            240,
            170
        )
        glyphParams.marginEnd = 20

        card.addView(glyphView, glyphParams)

        val infoView = TextView(this)
        infoView.text =
            "${symbol.romaji} — ${symbol.masteryPercent}% освоения\n" +
                    "Правильно: ${symbol.correctTotal}\n" +
                    "Ошибок: ${symbol.wrongTotal}"

        infoView.textSize = 19f
        infoView.setTextColor(Color.parseColor("#7B585A"))
        infoView.setPadding(24, 0, 0, 0)

        card.addView(
            infoView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 14)

        layoutSymbolsProgress.addView(card, params)
    }

    private fun addMistakeCard(mistake: MistakeInfo) {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.HORIZONTAL
        card.gravity = Gravity.CENTER_VERTICAL
        card.setBackgroundColor(Color.parseColor("#FFFFFF"))
        card.setPadding(32, 24, 32, 24)

        val glyphView = TextView(this)
        glyphView.text = mistake.glyph
        glyphView.textSize = if (mistake.glyph.length > 1) 40f else 54f
        glyphView.setTextColor(Color.parseColor("#7B585A"))
        glyphView.gravity = Gravity.CENTER
        glyphView.includeFontPadding = false
        glyphView.maxLines = 1

        val glyphParams = LinearLayout.LayoutParams(
            240,
            170
        )
        glyphParams.marginEnd = 20

        card.addView(glyphView, glyphParams)

        val infoView = TextView(this)
        infoView.text =
            "${mistake.romaji}\n" +
                    "Ошибок: ${mistake.mistakeCount}"

        infoView.textSize = 20f
        infoView.setTextColor(Color.parseColor("#7B585A"))
        infoView.setPadding(24, 0, 0, 0)

        card.addView(
            infoView,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 14)

        layoutMistakesProgress.addView(card, params)
    }

    private fun addEmptyMistakesCard() {
        val card = TextView(this)
        card.text = "Ошибок пока нет"
        card.textSize = 18f
        card.setTextColor(Color.parseColor("#7B585A"))
        card.setBackgroundColor(Color.parseColor("#FFFFFF"))
        card.setPadding(24, 20, 24, 20)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 16)

        layoutMistakesProgress.addView(card, params)
    }
}