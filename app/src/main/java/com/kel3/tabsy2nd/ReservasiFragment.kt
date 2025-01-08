package com.kel3.tabsy2nd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class ReservasiFragment : Fragment(R.layout.fragment_reservasi) {

    private var countDewasa = 0
    private var countAnak = 0
    private var countBalita = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View Elements
        val btnDatePicker: Button = view.findViewById(R.id.btnDatePicker)
        val tvSelectedDate: TextView = view.findViewById(R.id.tvSelectedDate)
        val btnTimePicker: Button = view.findViewById(R.id.btnTimePicker)
        val tvSelectedTime: TextView = view.findViewById(R.id.tvSelectedTime)
        val tvTotalPelanggan: TextView = view.findViewById(R.id.tvTotalPelanggan)
        val btnConfirmReservation: Button = view.findViewById(R.id.btnConfirmReservation)

        // Date Picker
        btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvSelectedDate.text = "Tanggal: $formattedDate"
            }, year, month, day).show()
        }

        // Time Picker
        btnTimePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                tvSelectedTime.text = "Waktu: $formattedTime"
            }, hour, minute, true).show()
        }

        // Counter Logic for Dewasa, Anak, and Balita
        setupCounterLogic(view, tvTotalPelanggan)

        // Confirm Reservation Button
        btnConfirmReservation.setOnClickListener {
            val selectedDate = tvSelectedDate.text.toString().replace("Tanggal: ", "").trim()
            val selectedTime = tvSelectedTime.text.toString().replace("Waktu: ", "").trim()
            val totalPeople = countDewasa + countAnak + countBalita

            val restaurantId = arguments?.getString("restaurant_id") ?: ""
            val imageResource = arguments?.getInt("restaurant_image") ?: R.drawable.ic_img // Default jika tidak ada
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && totalPeople > 0) {
                saveReservationToFirebase(restaurantId, userId, selectedDate, selectedTime, totalPeople, imageResource)
            } else {
                Toast.makeText(requireContext(), "Pastikan semua data terisi!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setupCounterLogic(view: View, tvTotalPelanggan: TextView) {
        // Counter Dewasa
        val btnMinusDewasa: Button = view.findViewById(R.id.btnMinusDewasa)
        val btnPlusDewasa: Button = view.findViewById(R.id.btnPlusDewasa)
        val tvCountDewasa: TextView = view.findViewById(R.id.tvCountDewasa)

        btnMinusDewasa.setOnClickListener {
            if (countDewasa > 0) {
                countDewasa--
                tvCountDewasa.text = countDewasa.toString()
                updateTotal(tvTotalPelanggan)
            }
        }

        btnPlusDewasa.setOnClickListener {
            countDewasa++
            tvCountDewasa.text = countDewasa.toString()
            updateTotal(tvTotalPelanggan)
        }

        // Counter Anak-anak
        val btnMinusAnak: Button = view.findViewById(R.id.btnMinusAnak)
        val btnPlusAnak: Button = view.findViewById(R.id.btnPlusAnak)
        val tvCountAnak: TextView = view.findViewById(R.id.tvCountAnak)

        btnMinusAnak.setOnClickListener {
            if (countAnak > 0) {
                countAnak--
                tvCountAnak.text = countAnak.toString()
                updateTotal(tvTotalPelanggan)
            }
        }

        btnPlusAnak.setOnClickListener {
            countAnak++
            tvCountAnak.text = countAnak.toString()
            updateTotal(tvTotalPelanggan)
        }

        // Counter Balita
        val btnMinusBalita: Button = view.findViewById(R.id.btnMinusBalita)
        val btnPlusBalita: Button = view.findViewById(R.id.btnPlusBalita)
        val tvCountBalita: TextView = view.findViewById(R.id.tvCountBalita)

        btnMinusBalita.setOnClickListener {
            if (countBalita > 0) {
                countBalita--
                tvCountBalita.text = countBalita.toString()
                updateTotal(tvTotalPelanggan)
            }
        }

        btnPlusBalita.setOnClickListener {
            countBalita++
            tvCountBalita.text = countBalita.toString()
            updateTotal(tvTotalPelanggan)
        }
    }

    private fun updateTotal(totalView: TextView) {
        val total = countDewasa + countAnak + countBalita
        totalView.text = "Total: $total"
    }

    private fun saveReservationToFirebase(
        restaurantId: String,
        userId: String,
        date: String,
        time: String,
        totalPeople: Int,
        imageResource: Int
    ) {
        val database = FirebaseDatabase.getInstance()
        val reservationRef = database.getReference("Pesanan")

        val reservationId = UUID.randomUUID().toString()

        val reservationData = mapOf(
            "idPesanan" to reservationId,
            "idResto" to restaurantId,
            "idUser" to userId,
            "tanggal" to date,
            "waktu" to time,
            "jumlahOrang" to totalPeople,
            "status" to "pending",
            "imageResource" to imageResource // Tambahkan ID resource gambar
        )

        reservationRef.child(reservationId).setValue(reservationData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Pemesanan berhasil disimpan", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseReservation", "Reservasi berhasil disimpan dengan ID: $reservationId")
            } else {
                Toast.makeText(requireContext(), "Gagal menyimpan pemesanan", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseReservation", "Gagal menyimpan reservasi: ${task.exception?.message}")
            }
        }
    }
}
