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
                favoriteRestaurants.add(restaurant)
                Log.d("FavoriteManager", "Favorite added: ${restaurant.name} with imageResource: ${restaurant.imageResource}")
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
                favoriteRestaurants.clear() // Clear the local list first
                snapshot.children.forEach { child ->
                    val restaurant = child.getValue(Restaurant::class.java)
                    restaurant?.let {
                        favoriteRestaurants.add(
                            Restaurant(
                                id = it.id,
                                name = it.name,
                                description = it.description,
                                price = it.price,
                                location = it.location,
                                imageResource = it.imageResource // Include the image resource
                            )
                        )
                    }
                }
                Log.d("FavoriteManager", "Favorites loaded: ${favoriteRestaurants.size}")
                onLoaded(favoriteRestaurants) // Notify the UI with the updated list
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
