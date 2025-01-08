package com.kel3.tabsy2nd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kel3.tabsy.Restaurant
import com.kel3.tabsy2nd.utils.FavoriteManager

class FavoritFragment : Fragment() {

    private lateinit var favoriteAdapter: LargeCardAdapter
    private val favoriteList = mutableListOf<Restaurant>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorit, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favorite_restaurants)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty list
        favoriteAdapter = LargeCardAdapter(favoriteList) { restaurant ->
            // Handle removing favorite
            FavoriteManager.removeFavorite(restaurant.id!!) {
                Toast.makeText(
                    requireContext(),
                    "${restaurant.name} dihapus dari favorit",
                    Toast.LENGTH_SHORT
                ).show()

                // Update the dataset and notify the adapter
                favoriteList.remove(restaurant)
                favoriteAdapter.notifyDataSetChanged()
            }
        }
        recyclerView.adapter = favoriteAdapter

        // Load favorites from Firebase and update the adapter
        FavoriteManager.loadFromFirebase { favorites ->
            favoriteList.clear()
            favoriteList.addAll(favorites)
            favoriteAdapter.notifyDataSetChanged()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // Reload favorites to ensure data is up-to-date
        FavoriteManager.loadFromFirebase { favorites ->
            favoriteList.clear()
            favoriteList.addAll(favorites)
            favoriteAdapter.notifyDataSetChanged()
        }
    }
}
