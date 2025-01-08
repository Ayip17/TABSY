package com.kel3.tabsy2nd

data class Pesanan(
    val idPesanan: String = "",
    val idResto: String = "",
    val idUser: String = "",
    val tanggal: String = "",
    val waktu: String = "",
    val jumlahOrang: Int = 0,
    val status: String = "pending",
    val imageResource: Int? = null // Tambahkan properti untuk ID resource
)

