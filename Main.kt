import java.util.Scanner

class Kalkulator {
    // 4 Fungsi Utama dengan rumus normal
    fun tambah(a: Double, b: Double): Double = a + b
    fun kurang(a: Double, b: Double): Double = a - b
    fun kali(a: Double, b: Double): Double = a * b
    fun bagi(a: Double, b: Double): Double = a / b

    fun hitungProgresif(input: String) {
        // Membersihkan spasi dan memisahkan angka dengan operator menjadi list
        val tokens = input.replace(" ", "").split("(?<=[-+*/])|(?=[-+*/])".toRegex()).toMutableList()
        
        println("\nProgres Perhitungan:")
        println(tokens.joinToString(" ")) // Menampilkan rumus awal utuh

        // TAHAP 1: Prioritas Perkalian (*) dan Pembagian (/)
        // Selama masih ada simbol * atau /, terus lakukan loop ini
        while (tokens.contains("*") || tokens.contains("/")) {
            val indexKali = tokens.indexOf("*")
            val indexBagi = tokens.indexOf("/")
            
            // Cari operasi mana yang muncul lebih dulu dari sebelah kiri
            val index = if (indexKali != -1 && indexBagi != -1) {
                minOf(indexKali, indexBagi)
            } else if (indexKali != -1) {
                indexKali
            } else {
                indexBagi
            }

            val kiri = tokens[index - 1].toDouble()
            val kanan = tokens[index + 1].toDouble()
            val operator = tokens[index]
            
            // Eksekusi fungsi utama
            val hasil = if (operator == "*") kali(kiri, kanan) else bagi(kiri, kanan)
            
            // Format agar angka bulat tidak memiliki akhiran .0 (misal 320.0 jadi 320)
            val formatHasil = if (hasil % 1 == 0.0) hasil.toLong().toString() else hasil.toString()

            // Update List: Timpa angka kiri dengan hasil, lalu hapus operator dan angka kanan
            tokens[index - 1] = formatHasil
            tokens.removeAt(index)
            tokens.removeAt(index)

            // Cetak bentuk terbaru dari seluruh rumus
            println(tokens.joinToString(" "))
        }

        // TAHAP 2: Penjumlahan (+) dan Pengurangan (-)
        // Setelah kali/bagi habis, kerjakan tambah/kurang dengan cara yang sama
        while (tokens.contains("+") || tokens.contains("-")) {
            val indexTambah = tokens.indexOf("+")
            val indexKurang = tokens.indexOf("-")
            
            val index = if (indexTambah != -1 && indexKurang != -1) {
                minOf(indexTambah, indexKurang)
            } else if (indexTambah != -1) {
                indexTambah
            } else {
                indexKurang
            }

            val kiri = tokens[index - 1].toDouble()
            val kanan = tokens[index + 1].toDouble()
            val operator = tokens[index]
            
            // Eksekusi fungsi utama
            val hasil = if (operator == "+") tambah(kiri, kanan) else kurang(kiri, kanan)
            
            val formatHasil = if (hasil % 1 == 0.0) hasil.toLong().toString() else hasil.toString()

            // Update List
            tokens[index - 1] = formatHasil
            tokens.removeAt(index)
            tokens.removeAt(index)

            // Cetak bentuk terbaru dari seluruh rumus
            println(tokens.joinToString(" "))
        }
        println("-----------------------------")
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val kalkulator = Kalkulator()

    println("Kalkulator")
    println("Ketik rumus (misal: 64 * 5 + 3 + 2 / 2)")
    println("Ketik 'exit' untuk menutup aplikasi.\n")

    while (true) {
        print("> ")
        val input = scanner.nextLine()

        if (input.lowercase() == "exit") {
            println("Program dihentikan.")
            break
        }
        if (input.isBlank()) continue

        try {
            kalkulator.hitungProgresif(input)
        } catch (e: Exception) {
            println("Error: Pastikan format rumus benar.")
        }
    }
}