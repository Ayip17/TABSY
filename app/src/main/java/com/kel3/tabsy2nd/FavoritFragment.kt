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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorit, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.favorite_restaurants)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Muat data favorit dari Firebase
        FavoriteManager.loadFromFirebase { favorites: List<Restaurant> ->
            recyclerView.adapter = LargeCardAdapter(favorites.toMutableList()) { restaurant ->
                FavoriteManager.removeFavorite(restaurant.id!!) {
                    Toast.makeText(
                        requireContext(),
                        "${restaurant.name} dihapus dari favorit",
                        Toast.LENGTH_SHORT
                    ).show()
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        // Perbarui adapter jika fragment dilanjutkan
        view?.findViewById<RecyclerView>(R.id.favorite_restaurants)?.adapter?.notifyDataSetChanged()
    }
}
