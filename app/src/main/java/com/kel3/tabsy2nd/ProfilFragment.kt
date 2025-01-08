package com.kel3.tabsy2nd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kel3.tabsy2nd.UserProfile
import com.kel3.tabsy2nd.utils.ProfileManager

class ProfilFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        // Inisialisasi Views
        etName = view.findViewById(R.id.etName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhone = view.findViewById(R.id.etPhone)
        btnSave = view.findViewById(R.id.btnSave)

        // Muat profil pengguna
        loadProfile()

        // Simpan data profil
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "Nama dan Email wajib diisi", Toast.LENGTH_SHORT).show()
            } else {
                val userProfile = UserProfile(name, email, phone)
                ProfileManager.saveProfile(userProfile, {
                    Toast.makeText(requireContext(), "Profil berhasil disimpan", Toast.LENGTH_SHORT).show()
                }, { error ->
                    Toast.makeText(requireContext(), "Gagal menyimpan profil: $error", Toast.LENGTH_SHORT).show()
                })
            }
        }

        return view
    }

    private fun loadProfile() {
        ProfileManager.loadProfile({ profile ->
            etName.setText(profile.name)
            etEmail.setText(profile.email)
            etPhone.setText(profile.phone)
        }, { error ->
            Toast.makeText(requireContext(), "Gagal memuat profil: $error", Toast.LENGTH_SHORT).show()
        })
    }
}
