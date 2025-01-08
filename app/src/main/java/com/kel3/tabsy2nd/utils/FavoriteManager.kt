package com.kel3.tabsy2nd.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kel3.tabsy.Restaurant

object FavoriteManager {
    private val favoriteRestaurants = mutableListOf<Restaurant>()

    // Menambahkan restoran ke daftar favorit dan Firebase
    fun addFavorite(restaurant: Restaurant) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("FavoriteManager", "User not logged in")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("Favorites").child(userId)

        restaurant.id = restaurant.id ?: favoritesRef.push().key // Buat ID unik jika belum ada
        favoritesRef.child(restaurant.id!!).setValue(restaurant)
            .addOnSuccessListener {
                favoriteRestaurants.add(restaurant)
                Log.d("FavoriteManager", "Favorite saved: ${restaurant.name}")
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to save favorite: ${e.message}")
            }
    }


    // Mengambil semua restoran favorit
    fun getFavorites(): List<Restaurant> = favoriteRestaurants

    // Menyimpan restoran ke Firebase
    private fun saveToFirebase(restaurant: Restaurant) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("FavoriteManager", "User not logged in")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("Favorites").child(userId)

        favoritesRef.child(restaurant.name ?: "Unknown").setValue(restaurant)
            .addOnSuccessListener {
                Log.d("FavoriteManager", "Favorite saved: ${restaurant.name}")
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to save favorite: ${e.message}")
            }

    }

    // Memuat restoran dari Firebase
    fun loadFromFirebase(onLoaded: (List<Restaurant>) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("FavoriteManager", "User not logged in")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("Favorites").child(userId)

        favoritesRef.get()
            .addOnSuccessListener { snapshot ->
                favoriteRestaurants.clear() // Bersihkan daftar lokal
                snapshot.children.forEach { child ->
                    val restaurant = child.getValue(Restaurant::class.java)
                    restaurant?.let { favoriteRestaurants.add(it) }
                }
                Log.d("FavoriteManager", "Favorites loaded: ${favoriteRestaurants.size}")
                onLoaded(favoriteRestaurants) // Sinkronkan dengan UI
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to load favorites: ${e.message}")
            }
    }




    // Fungsi untuk menghapus restoran dari daftar favorit dan Firebase
    fun removeFavorite(restaurantName: String, onComplete: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("FavoriteManager", "User not logged in")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("Favorites").child(userId)

        fun removeFavorite(restaurantName: String, onComplete: () -> Unit) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId == null) {
                Log.e("FavoriteManager", "User not logged in")
                return
            }

            val database = FirebaseDatabase.getInstance()
            val favoritesRef = database.getReference("Favorites").child(userId)

            // Log referensi path untuk debugging
            Log.d("FavoriteManager", "Removing node at: ${favoritesRef.child(restaurantName).toString()}")

            // Hapus data berdasarkan nama restoran
            favoritesRef.child(restaurantName).removeValue()
                .addOnSuccessListener {
                    favoriteRestaurants.removeAll { it.name == restaurantName }
                    Log.d("FavoriteManager", "Favorite removed: $restaurantName")
                    onComplete()
                }
                .addOnFailureListener { e ->
                    Log.e("FavoriteManager", "Failed to remove favorite: ${e.message}")
                }
        }

        // Hapus berdasarkan nama restoran
        favoritesRef.child(restaurantName).removeValue()
            .addOnSuccessListener {
                favoriteRestaurants.removeAll { it.name == restaurantName } // Hapus dari daftar lokal
                Log.d("FavoriteManager", "Favorite removed: $restaurantName")
                onComplete() // Callback hanya jika berhasil
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to remove favorite: ${e.message}")
            }
    }



}
