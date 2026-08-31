package com.tkachevaekaterina.nihonkana.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.R
import com.tkachevaekaterina.nihonkana.data.DatabaseProvider
import com.tkachevaekaterina.nihonkana.data.ExerciseSessionEntity
import com.tkachevaekaterina.nihonkana.data.KanaSymbolEntity
import com.tkachevaekaterina.nihonkana.data.UserSymbolProgressEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.tkachevaekaterina.nihonkana.data.ExerciseAnswerEntity
class ExerciseActivity : AppCompatActivity() {

    private lateinit var tvProgress: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvSymbol: TextView

    private lateinit var btnOption1: Button
    private lateinit var btnOption2: Button
    private lateinit var btnOption3: Button
    private lateinit var btnOption4: Button
    private lateinit var btnExitExercise: Button

    private var symbols = listOf<KanaSymbolEntity>()
    private var questions = listOf<KanaSymbolEntity>()

    private var currentQuestionIndex = 0
    private var currentQuestionNumber = 1
    private val totalQuestions = 50

    private var correctAnswer = ""
    private var correctCount = 0
    private var wrongCount = 0

    private var alphabet = "hiragana"
    private var exerciseMode = "kana_to_romaji"
    private var symbolGroup = "all"
    private var selectedRows = arrayListOf<String>()

    private val answerResults = mutableListOf<AnswerResult>()

    private val basicRows = listOf(
        "a", "k", "s", "t", "n",
        "h", "m", "y", "r", "w", "n_special"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        tvProgress = findViewById(R.id.tvProgress)
        tvScore = findViewById(R.id.tvScore)
        tvSymbol = findViewById(R.id.tvSymbol)

        btnOption1 = findViewById(R.id.btnOption1)
        btnOption2 = findViewById(R.id.btnOption2)
        btnOption3 = findViewById(R.id.btnOption3)
        btnOption4 = findViewById(R.id.btnOption4)
        btnExitExercise = findViewById(R.id.btnExitExercise)

        alphabet = intent.getStringExtra("alphabet") ?: "hiragana"
        exerciseMode = intent.getStringExtra("exerciseMode") ?: "kana_to_romaji"
        symbolGroup = intent.getStringExtra("symbolGroup") ?: "all"
        selectedRows = intent.getStringArrayListExtra("selectedRows") ?: arrayListOf()

        btnExitExercise.setOnClickListener {
            finish()
        }

        loadSymbols()
    }

    private fun loadSymbols() {
        val db = DatabaseProvider.getDatabase(this)
        val dao = db.kanaSymbolDao()

        CoroutineScope(Dispatchers.IO).launch {
            val allSymbols = dao.getSymbolsByAlphabet(alphabet)

            symbols = when (symbolGroup) {
                "basic" -> allSymbols.filter {
                    it.rowName in basicRows
                }

                "additional" -> allSymbols.filter {
                    it.rowName !in basicRows
                }

                "custom" -> allSymbols.filter {
                    it.rowName in selectedRows
                }

                else -> allSymbols
            }

            questions = symbols.shuffled().take(totalQuestions)

            runOnUiThread {
                if (symbols.isNotEmpty()) {
                    showQuestion()
                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "Нет символов для выбранной тренировки",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun showQuestion() {
        if (currentQuestionIndex >= questions.size) {
            saveProgressAndShowResult()
            return
        }

        val question = questions[currentQuestionIndex]

        if (exerciseMode == "romaji_to_kana") {
            tvSymbol.text = question.romaji
            correctAnswer = question.glyph
        } else {
            tvSymbol.text = question.glyph
            correctAnswer = question.romaji
        }

        val options = mutableListOf(correctAnswer)

        val allAnswers = if (exerciseMode == "romaji_to_kana") {
            symbols.map { it.glyph }.distinct()
        } else {
            symbols.map { it.romaji }.distinct()
        }

        while (options.size < 4 && options.size < allAnswers.size) {
            val randomAnswer = allAnswers.random()

            if (randomAnswer !in options) {
                options.add(randomAnswer)
            }
        }

        options.shuffle()

        val actualTotalQuestions = questions.size

        tvProgress.text = "Вопрос $currentQuestionNumber из $actualTotalQuestions"
        tvScore.text = "Правильно: $correctCount | Ошибок: $wrongCount"

        val buttons = listOf(btnOption1, btnOption2, btnOption3, btnOption4)

        buttons.forEachIndexed { index, button ->
            button.text = options.getOrNull(index) ?: ""
            button.isEnabled = options.getOrNull(index) != null

            button.setOnClickListener {
                checkAnswer(button.text.toString())
            }
        }
    }

    private fun checkAnswer(selectedAnswer: String) {
        val currentSymbol = questions[currentQuestionIndex]
        val isCorrect = selectedAnswer == correctAnswer

        if (isCorrect) {
            correctCount++
            Toast.makeText(this, "Верно!", Toast.LENGTH_SHORT).show()
        } else {
            wrongCount++
            Toast.makeText(
                this,
                "Неверно. Правильно: $correctAnswer",
                Toast.LENGTH_SHORT
            ).show()
        }

        answerResults.add(
            AnswerResult(
                symbolId = currentSymbol.id,
                selectedAnswer = selectedAnswer,
                correctAnswer = correctAnswer,
                isCorrect = isCorrect
            )
        )

        currentQuestionNumber++
        currentQuestionIndex++
        showQuestion()
    }

    private fun saveProgressAndShowResult() {
        val actualTotalQuestions = questions.size

        val percent = if (actualTotalQuestions > 0) {
            correctCount * 100 / actualTotalQuestions
        } else {
            0
        }

        val db = DatabaseProvider.getDatabase(this)
        val progressDao = db.progressDao()

        CoroutineScope(Dispatchers.IO).launch {

            val session = ExerciseSessionEntity(
                alphabet = alphabet,
                exerciseMode = exerciseMode,
                symbolGroup = symbolGroup,
                totalQuestions = actualTotalQuestions,
                correctCount = correctCount,
                wrongCount = wrongCount,
                percent = percent,
                date = System.currentTimeMillis()
            )

            val sessionId = progressDao.insertExerciseSession(session).toInt()

            answerResults.forEach { result ->

                val answerEntity = ExerciseAnswerEntity(
                    sessionId = sessionId,
                    symbolId = result.symbolId,
                    selectedAnswer = result.selectedAnswer,
                    correctAnswer = result.correctAnswer,
                    isCorrect = result.isCorrect,
                    answeredAt = System.currentTimeMillis()
                )

                progressDao.insertExerciseAnswer(answerEntity)

                val oldProgress = progressDao.getSymbolProgress(result.symbolId)

                val newCorrectTotal =
                    (oldProgress?.correctTotal ?: 0) + if (result.isCorrect) 1 else 0

                val newWrongTotal =
                    (oldProgress?.wrongTotal ?: 0) + if (result.isCorrect) 0 else 1

                val totalAnswers = newCorrectTotal + newWrongTotal

                val masteryPercent = if (totalAnswers > 0) {
                    newCorrectTotal * 100 / totalAnswers
                } else {
                    0
                }

                val updatedProgress = UserSymbolProgressEntity(
                    id = oldProgress?.id ?: 0,
                    symbolId = result.symbolId,
                    correctTotal = newCorrectTotal,
                    wrongTotal = newWrongTotal,
                    lastSeenAt = System.currentTimeMillis(),
                    masteryPercent = masteryPercent
                )

                progressDao.saveSymbolProgress(updatedProgress)
            }

            runOnUiThread {
                openResultScreen(actualTotalQuestions, percent)
            }
        }
    }

    private fun openResultScreen(actualTotalQuestions: Int, percent: Int) {
        val intent = Intent(this, ExerciseResultActivity::class.java)
        intent.putExtra("correctCount", correctCount)
        intent.putExtra("wrongCount", wrongCount)
        intent.putExtra("totalQuestions", actualTotalQuestions)
        intent.putExtra("percent", percent)
        startActivity(intent)
        finish()
    }

    private data class AnswerResult(
        val symbolId: Int,
        val selectedAnswer: String,
        val correctAnswer: String,
        val isCorrect: Boolean
    )
}