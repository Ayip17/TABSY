package com.kel3.tabsy2nd
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.identity.util.UUID
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PesananFragment : Fragment(R.layout.fragment_pesanan) {

    private lateinit var recyclerView: RecyclerView
    private val pesananList = mutableListOf<Pesanan>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rvPesanan)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = PesananAdapter(
            pesananList,
            onCancelClicked = { pesanan -> cancelPesanan(pesanan) },
            onHubungiClicked = { pesanan -> hubungiRestoran(pesanan) }
        )
        recyclerView.adapter = adapter

        // Load Data from Firebase
        loadPesanan(adapter)
    }

    private fun loadPesanan(adapter: PesananAdapter) {

        // Ambil User ID dari Firebase Auth
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("PesananFragment", "User is not logged in!")
            return
        }
        Log.d("PesananFragment", "User ID: $userId")

        // Referensi ke Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val pesananRef = database.getReference("Pesanan")

        // Query Firebase untuk mendapatkan data dengan idUser tertentu
        pesananRef.orderByChild("idUser").equalTo(userId).get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {
                    Log.d("PesananFragment", "Snapshot exists: ${snapshot.value}")
                    pesananList.clear()
                    // Tambahkan log untuk memverifikasi setiap data
                    snapshot.children.forEach { child ->
                        val idUserInData = child.child("idUser").value.toString()
                        Log.d("PesananFragment", "idUser in data: $idUserInData")
                        if (idUserInData == userId) {
                            Log.d("PesananFragment", "Match found for idUser!")
                        } else {
                            Log.d("PesananFragment", "Mismatch: $idUserInData != $userId")
                        }

                        // Ambil data menjadi objek Pesanan
                        val pesanan = child.getValue(Pesanan::class.java)
                        if (pesanan != null) {
                            pesananList.add(pesanan)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("PesananFragment", "No data found for this user")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PesananFragment", "Error loading data: ${exception.message}")
            }
    }



    private fun cancelPesanan(pesanan: Pesanan) {
        val database = FirebaseDatabase.getInstance()
        val pesananRef = database.getReference("Pesanan").child(pesanan.idPesanan)

        pesananRef.removeValue().addOnSuccessListener {
            // Remove the pesanan from the local list and update the RecyclerView
            pesananList.remove(pesanan)
            recyclerView.adapter?.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Pesanan berhasil dibatalkan", Toast.LENGTH_SHORT).show()
            Log.d("FragmentPesanan", "Pesanan berhasil dibatalkan")
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Gagal membatalkan pesanan", Toast.LENGTH_SHORT).show()
            Log.e("FragmentPesanan", "Gagal membatalkan pesanan: ${exception.message}")
        }
    }

    private fun hubungiRestoran(pesanan: Pesanan) {
        // Implementasikan logika untuk menghubungi restoran, misalnya membuka WhatsApp
        Log.d("FragmentPesanan", "Menghubungi restoran dengan ID: ${pesanan.idResto}")
    }

    private fun saveReservationToFirebase(
        restaurantId: String,
        userId: String,
        date: String,
        time: String,
        totalPeople: Int
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
            "status" to "pending"
        )

        reservationRef.child(reservationId).setValue(reservationData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Pemesanan berhasil disimpan", Toast.LENGTH_SHORT).show()
                Log.d("FirebaseReservation", "Reservasi berhasil disimpan dengan ID: $reservationId")

                // Navigasi ke PesananFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, PesananFragment()) // Ganti ID container sesuai dengan milik Anda
                    .addToBackStack(null)
                    .commit()

                // Ubah state Bottom Navigation
                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.selectedItemId = R.id.icPesanan // ID item Pesanan di Bottom Navigation
            } else {
                Toast.makeText(requireContext(), "Gagal menyimpan pemesanan", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseReservation", "Gagal menyimpan reservasi: ${task.exception?.message}")
            }
        }
    }

}
