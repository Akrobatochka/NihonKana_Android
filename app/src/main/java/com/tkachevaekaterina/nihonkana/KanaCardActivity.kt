package com.tkachevaekaterina.nihonkana

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tkachevaekaterina.nihonkana.data.DatabaseProvider
import com.tkachevaekaterina.nihonkana.data.KanaSymbolEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KanaCardActivity : AppCompatActivity() {

    private var currentIndex = 0
    private var symbols = listOf<KanaSymbolEntity>()

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kana_card)

        val tvCardCounter = findViewById<TextView>(R.id.tvCardCounter)
        val tvSymbol = findViewById<TextView>(R.id.tvSymbol)
        val tvRomaji = findViewById<TextView>(R.id.tvRomaji)
        val ivAssociation = findViewById<ImageView>(R.id.ivAssociation)
        val ivPlaySound = findViewById<ImageView>(R.id.ivPlaySound)
        val tvAssociationText = findViewById<TextView>(R.id.tvAssociationText)
        val btnPrevious = findViewById<Button>(R.id.btnPrevious)
        val btnBackToRows = findViewById<Button>(R.id.btnBackToRows)
        val btnNext = findViewById<Button>(R.id.btnNext)

        val alphabet = intent.getStringExtra("alphabet") ?: "hiragana"
        val row = intent.getStringExtra("row") ?: "a"

        val db = DatabaseProvider.getDatabase(this)
        val dao = db.kanaSymbolDao()

        CoroutineScope(Dispatchers.IO).launch {
            symbols = dao.getSymbolsByAlphabetAndRow(alphabet, row)

            runOnUiThread {
                if (symbols.isNotEmpty()) {
                    showCard(
                        tvCardCounter,
                        tvSymbol,
                        tvRomaji,
                        tvAssociationText,
                        ivAssociation,
                        ivPlaySound
                    )
                } else {
                    Toast.makeText(
                        this@KanaCardActivity,
                        "Символы не найдены",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }

        btnNext.setOnClickListener {
            if (symbols.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % symbols.size
                showCard(
                    tvCardCounter,
                    tvSymbol,
                    tvRomaji,
                    tvAssociationText,
                    ivAssociation,
                    ivPlaySound
                )
            }
        }

        btnPrevious.setOnClickListener {
            if (symbols.isNotEmpty()) {
                currentIndex = if (currentIndex - 1 < 0) {
                    symbols.size - 1
                } else {
                    currentIndex - 1
                }

                showCard(
                    tvCardCounter,
                    tvSymbol,
                    tvRomaji,
                    tvAssociationText,
                    ivAssociation,
                    ivPlaySound
                )
            }
        }

        btnBackToRows.setOnClickListener {
            finish()
        }
    }

    private fun showCard(
        tvCardCounter: TextView,
        tvSymbol: TextView,
        tvRomaji: TextView,
        tvAssociationText: TextView,
        ivAssociation: ImageView,
        ivPlaySound: ImageView
    ) {
        val item = symbols[currentIndex]

        tvCardCounter.text = "${currentIndex + 1} из ${symbols.size}"
        tvSymbol.text = item.glyph
        tvRomaji.text = item.romaji
        tvAssociationText.text = item.mnemonicText

        val imageResId = resources.getIdentifier(
            item.imageName,
            "drawable",
            packageName
        )

        if (imageResId != 0) {
            ivAssociation.setImageResource(imageResId)
        } else {
            ivAssociation.setImageDrawable(null)
        }

        val playClickListener = View.OnClickListener {
            if (item.audioName.isNotBlank()) {
                playAudio(item.audioName)
            } else {
                Toast.makeText(
                    this,
                    "Аудио для этого символа не указано",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ivAssociation.setOnClickListener(playClickListener)
        ivPlaySound.setOnClickListener(playClickListener)
    }

    private fun playAudio(audioName: String) {
        val audioResId = resources.getIdentifier(
            audioName,
            "raw",
            packageName
        )

        if (audioResId != 0) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, audioResId)
            mediaPlayer?.start()
        } else {
            Toast.makeText(
                this,
                "Аудио не найдено: $audioName",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}