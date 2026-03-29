package com.example.kakulatorsimple

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etEkspresi: EditText
    private val calculator = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEkspresi = findViewById(R.id.etEkspresi)

        setupButtons()
    }

    private fun setupButtons() {
        val buttonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnTambah, R.id.btnKurang, R.id.btnKali, R.id.btnBagi, R.id.btnTitik
        )

        for (id in buttonIds) {
            findViewById<Button>(id).setOnClickListener {
                val button = it as Button
                etEkspresi.append(button.text)
            }
        }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            etEkspresi.setText("")
        }

        findViewById<Button>(R.id.btnDel).setOnClickListener {
            val currentText = etEkspresi.text.toString()
            if (currentText.isNotEmpty()) {
                etEkspresi.setText(currentText.substring(0, currentText.length - 1))
            }
        }

        findViewById<Button>(R.id.btnSamaDengan).setOnClickListener {
            hitungDanPindahHalaman()
        }
    }

    private fun hitungDanPindahHalaman() {
        val ekspresi = etEkspresi.text.toString().trim()

        if (ekspresi.isEmpty()) {
            Toast.makeText(this, "Masukkan ekspresi!", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            calculator.parse(ekspresi)
            val langkahList = calculator.hitungLangkahDemiLangkah()

            val hasilText = buildString {
                appendLine("Ekspresi: $ekspresi")
                appendLine("─".repeat(20))
                for (langkah in langkahList) {
                    appendLine(langkah)
                }
            }

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("EXTRA_RESULT", hasilText)
            startActivity(intent)

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
