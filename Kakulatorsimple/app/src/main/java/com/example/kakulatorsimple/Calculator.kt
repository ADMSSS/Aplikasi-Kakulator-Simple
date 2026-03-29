package com.example.kakulatorsimple

/**
 * Class Calculator
 * 
 * Kalkulator yang menghitung ekspresi matematika langkah demi langkah.
 * Prioritas operasi: perkalian (*) dan pembagian (/) terlebih dahulu,
 * baru kemudian penjumlahan (+) dan pengurangan (-).
 * 
 * Contoh: 64 * 5 + 3 + 2 / 2
 *   Langkah 1: 64 * 5 = 320    → [320 + 3 + 2 / 2]
 *   Langkah 2: 2 / 2 = 1       → [320 + 3 + 1]
 *   Langkah 3: 320 + 3 = 323   → [323 + 1]
 *   Langkah 4: 323 + 1 = 324
 *   Hasil akhir: 324
 */
class Calculator {

    // Menyimpan angka-angka dari ekspresi
    private var numbers: MutableList<Double> = mutableListOf()

    // Menyimpan operator-operator dari ekspresi
    private var operators: MutableList<Char> = mutableListOf()

    // ===== 4 FUNGSI UTAMA (rumus normal satu per satu) =====

    /**
     * Fungsi Penjumlahan
     * Menjumlahkan dua angka
     */
    fun tambah(a: Double, b: Double): Double {
        return a + b
    }

    /**
     * Fungsi Pengurangan
     * Mengurangi angka pertama dengan angka kedua
     */
    fun kurang(a: Double, b: Double): Double {
        return a - b
    }

    /**
     * Fungsi Perkalian
     * Mengalikan dua angka
     */
    fun kali(a: Double, b: Double): Double {
        return a * b
    }

    /**
     * Fungsi Pembagian
     * Membagi angka pertama dengan angka kedua
     * Mengecek pembagian dengan nol
     */
    fun bagi(a: Double, b: Double): Double {
        if (b == 0.0) {
            throw ArithmeticException("Tidak bisa membagi dengan nol!")
        }
        return a / b
    }

    // ===== FUNGSI PARSING =====

    /**
     * Mengubah string ekspresi menjadi list angka dan operator
     * Contoh: "64 * 5 + 3" → numbers=[64, 5, 3], operators=[*, +]
     */
    fun parse(expression: String) {
        // Reset data sebelumnya
        numbers.clear()
        operators.clear()

        // Bersihkan spasi berlebih
        val expr = expression.trim()

        if (expr.isEmpty()) {
            throw IllegalArgumentException("Ekspresi tidak boleh kosong!")
        }

        // Variabel untuk membangun angka satu per satu
        var currentNumber = ""

        for (i in expr.indices) {
            val char = expr[i]

            when {
                // Jika karakter adalah digit atau titik desimal, tambahkan ke angka saat ini
                char.isDigit() || char == '.' -> {
                    currentNumber += char
                }
                // Jika karakter adalah operator (+, -, *, /)
                char == '+' || char == '-' || char == '*' || char == '/' -> {
                    // Simpan angka yang sudah terkumpul
                    if (currentNumber.isNotEmpty()) {
                        numbers.add(currentNumber.toDouble())
                        currentNumber = ""
                    }
                    // Simpan operator
                    operators.add(char)
                }
                // Abaikan spasi
                char == ' ' -> {
                    if (currentNumber.isNotEmpty()) {
                        numbers.add(currentNumber.toDouble())
                        currentNumber = ""
                    }
                }
                else -> {
                    throw IllegalArgumentException("Karakter tidak valid: '$char'")
                }
            }
        }

        // Jangan lupa angka terakhir
        if (currentNumber.isNotEmpty()) {
            numbers.add(currentNumber.toDouble())
        }

        // Validasi: jumlah angka harus = jumlah operator + 1
        if (numbers.size != operators.size + 1) {
            throw IllegalArgumentException("Ekspresi tidak valid!")
        }

        if (numbers.isEmpty()) {
            throw IllegalArgumentException("Tidak ada angka ditemukan!")
        }
    }

    // ===== FUNGSI HITUNG LANGKAH DEMI LANGKAH =====

    /**
     * Menghitung ekspresi langkah demi langkah
     * Prioritas: perkalian (*) dan pembagian (/) dulu, baru tambah (+) dan kurang (-)
     * 
     * Return: List<String> berisi setiap langkah perhitungan
     */
    fun hitungLangkahDemiLangkah(): List<String> {
        // Buat salinan agar data asli tidak berubah
        val nums = numbers.toMutableList()
        val ops = operators.toMutableList()
        val langkah = mutableListOf<String>()
        var nomorLangkah = 1

        // ===== TAHAP 1: Kerjakan perkalian (*) dan pembagian (/) terlebih dahulu =====
        var i = 0
        while (i < ops.size) {
            if (ops[i] == '*' || ops[i] == '/') {
                val a = nums[i]
                val b = nums[i + 1]
                val operator = ops[i]

                // Hitung menggunakan fungsi yang sesuai
                val hasil = when (operator) {
                    '*' -> kali(a, b)
                    '/' -> bagi(a, b)
                    else -> throw IllegalStateException("Operator tidak valid")
                }

                // Format nama operasi
                val namaOperasi = when (operator) {
                    '*' -> "×"
                    '/' -> "÷"
                    else -> operator.toString()
                }

                // Hapus dua angka yang sudah dihitung, ganti dengan hasil
                nums[i] = hasil
                nums.removeAt(i + 1)
                ops.removeAt(i)

                // Buat string ekspresi sisa
                val sisaEkspresi = buatStringEkspresi(nums, ops)

                // Catat langkah ini
                langkah.add(
                    "Langkah $nomorLangkah: ${formatAngka(a)} $namaOperasi ${formatAngka(b)} = ${formatAngka(hasil)}   →  $sisaEkspresi"
                )
                nomorLangkah++

                // Jangan increment i, karena elemen sudah bergeser
            } else {
                i++
            }
        }

        // ===== TAHAP 2: Kerjakan penjumlahan (+) dan pengurangan (-) =====
        while (ops.isNotEmpty()) {
            val a = nums[0]
            val b = nums[1]
            val operator = ops[0]

            // Hitung menggunakan fungsi yang sesuai
            val hasil = when (operator) {
                '+' -> tambah(a, b)
                '-' -> kurang(a, b)
                else -> throw IllegalStateException("Operator tidak valid")
            }

            // Format nama operasi
            val namaOperasi = when (operator) {
                '+' -> "+"
                '-' -> "-"
                else -> operator.toString()
            }

            // Hapus dua angka yang sudah dihitung, ganti dengan hasil
            nums[0] = hasil
            nums.removeAt(1)
            ops.removeAt(0)

            // Buat string ekspresi sisa
            val sisaEkspresi = buatStringEkspresi(nums, ops)

            // Catat langkah ini
            if (ops.isEmpty()) {
                // Langkah terakhir, tidak perlu panah
                langkah.add(
                    "Langkah $nomorLangkah: ${formatAngka(a)} $namaOperasi ${formatAngka(b)} = ${formatAngka(hasil)}"
                )
            } else {
                langkah.add(
                    "Langkah $nomorLangkah: ${formatAngka(a)} $namaOperasi ${formatAngka(b)} = ${formatAngka(hasil)}   →  $sisaEkspresi"
                )
            }
            nomorLangkah++
        }

        // Tambahkan hasil akhir
        if (nums.isNotEmpty()) {
            langkah.add("\nHasil akhir: ${formatAngka(nums[0])}")
        }

        return langkah
    }

    // ===== FUNGSI HELPER =====

    /**
     * Membuat string ekspresi dari list angka dan operator
     * Contoh: nums=[320, 3, 1], ops=[+, +] → "320 + 3 + 1"
     */
    private fun buatStringEkspresi(nums: List<Double>, ops: List<Char>): String {
        if (nums.isEmpty()) return ""

        val sb = StringBuilder()
        sb.append(formatAngka(nums[0]))

        for (j in ops.indices) {
            val opSymbol = when (ops[j]) {
                '*' -> "×"
                '/' -> "÷"
                else -> ops[j].toString()
            }
            sb.append(" $opSymbol ${formatAngka(nums[j + 1])}")
        }

        return sb.toString()
    }

    /**
     * Format angka: hilangkan .0 jika angka bulat
     * Contoh: 320.0 → "320", 3.5 → "3.5"
     */
    private fun formatAngka(angka: Double): String {
        return if (angka == angka.toLong().toDouble()) {
            angka.toLong().toString()
        } else {
            angka.toString()
        }
    }
}
