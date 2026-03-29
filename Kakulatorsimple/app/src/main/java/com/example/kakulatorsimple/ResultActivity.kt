package com.example.kakulatorsimple

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvHasilDetail = findViewById<TextView>(R.id.tvHasilDetail)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val hasil = intent.getStringExtra("EXTRA_RESULT") ?: "Tidak ada data"
        tvHasilDetail.text = hasil

        btnKembali.setOnClickListener {
            finish()
        }
    }
}
