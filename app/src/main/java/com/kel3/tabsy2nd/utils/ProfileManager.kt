package com.kel3.tabsy2nd.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kel3.tabsy2nd.UserProfile

object ProfileManager {

    // Simpan profil pengguna ke Firebase
    fun saveProfile(userProfile: UserProfile, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(userId)

        userRef.setValue(userProfile)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Gagal menyimpan profil") }
    }

    // Muat profil pengguna dari Firebase
    fun loadProfile(onSuccess: (UserProfile) -> Unit, onFailure: (String) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("Users").child(userId)

        userRef.get()
            .addOnSuccessListener { snapshot ->
                val profile = snapshot.getValue(UserProfile::class.java)
                if (profile != null) {
                    onSuccess(profile)
                } else {
                    onFailure("Profil tidak ditemukan")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Gagal memuat profil")
            }
    }
}
