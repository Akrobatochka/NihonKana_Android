package com.tkachevaekaterina.nihonkana

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tkachevaekaterina.nihonkana.grammar.GrammarMenuActivity
import com.tkachevaekaterina.nihonkana.ui.theme.NihonKanaTheme
import com.tkachevaekaterina.nihonkana.progress.ProgressActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NihonKanaTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {
        // Указываем цвет фона прямо в Scaffold через containerColor
        Scaffold(
            containerColor = Color(0xFFFDE5E8),
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Верх: логотип и название
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "Логотип",
                        modifier = Modifier.height(100.dp)
                    )

                    Text(
                        text = "Nihon Kana",
                        fontSize = 28.sp,
                        color = Color(0xFF7B585A),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Кнопки
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StyledButton(
                        text = "Хирагана",
                        onClick = {
                            val intent = Intent(this@MainActivity, AlphabetSelectionActivity::class.java)
                            intent.putExtra("alphabet", "hiragana")
                            startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    StyledButton(
                        text = "Катакана",
                        onClick = {
                            val intent = Intent(this@MainActivity, AlphabetSelectionActivity::class.java)
                            intent.putExtra("alphabet", "katakana")
                            startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    StyledButton(
                        text = "Грамматика",
                        onClick = {
                            val intent = Intent(this@MainActivity, GrammarMenuActivity::class.java)
                            startActivity(intent)
                        }
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    StyledButton(
                        text = "Прогресс",
                        onClick = {
                            val intent = Intent(this@MainActivity, ProgressActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }

                // Нижняя часть: котик
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_page_hi),
                        contentDescription = "Котик с облачком",
                        modifier = Modifier
                            .height(220.dp)
                            .aspectRatio(600f / 304f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }

    @Composable
    fun StyledButton(text: String, onClick: () -> Unit) {
        // Добавляем BorderStroke, чтобы кнопка была с рамкой, как в XML макете
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .width(270.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFFEFFAF),
                contentColor = Color(0xFF999D4F)
            ),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF999D4F))
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                color = Color(0xFF999D4F)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NihonKanaTheme {
        MainActivity().MainScreen()
    }
}