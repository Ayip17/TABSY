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

        restaurant.id = restaurant.id ?: favoritesRef.push().key // Generate unique ID if null
        favoritesRef.child(restaurant.id!!).setValue(restaurant)
            .addOnSuccessListener {
                favoriteRestaurants.add(restaurant) // Add to local list
                Log.d("FavoriteManager", "Favorite added: ${restaurant.name}")
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to add favorite: ${e.message}")
            }
    }

    // Mengambil semua restoran favorit
    fun getFavorites(): List<Restaurant> = favoriteRestaurants

    // Memuat restoran favorit dari Firebase ke dalam daftar lokal
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
                favoriteRestaurants.clear() // Clear local list
                snapshot.children.forEach { child ->
                    val restaurant = child.getValue(Restaurant::class.java)
                    restaurant?.let { favoriteRestaurants.add(it) }
                }
                Log.d("FavoriteManager", "Favorites loaded: ${favoriteRestaurants.size}")
                onLoaded(favoriteRestaurants) // Update UI with loaded data
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to load favorites: ${e.message}")
            }
    }

    // Menghapus restoran dari daftar favorit dan Firebase
    fun removeFavorite(restaurantId: String, onComplete: () -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Log.e("FavoriteManager", "User not logged in")
            return
        }

        val database = FirebaseDatabase.getInstance()
        val favoritesRef = database.getReference("Favorites").child(userId)

        favoritesRef.child(restaurantId).removeValue()
            .addOnSuccessListener {
                favoriteRestaurants.removeAll { it.id == restaurantId } // Remove from local list
                Log.d("FavoriteManager", "Favorite removed: $restaurantId")
                onComplete()
            }
            .addOnFailureListener { e ->
                Log.e("FavoriteManager", "Failed to remove favorite: ${e.message}")
            }
    }
}
