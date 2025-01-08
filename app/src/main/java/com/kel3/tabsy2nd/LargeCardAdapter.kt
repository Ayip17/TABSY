package com.kel3.tabsy2nd

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kel3.tabsy.Restaurant

class LargeCardAdapter(
    private val restaurantList: MutableList<Restaurant>, // Gunakan MutableList
    private val onFavoriteClicked: (Restaurant) -> Unit
) : RecyclerView.Adapter<LargeCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_large_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.tvName.text = restaurant.name
        holder.tvPrice.text = restaurant.price
        holder.tvLocation.text = restaurant.location
        // Favorit click
        holder.ivFavorite.setOnClickListener {
            onFavoriteClicked(restaurant)
        }
    }

    fun removeItem(restaurantName: String) {
        val index = restaurantList.indexOfFirst { it.name == restaurantName }
        if (index != -1) {
            restaurantList.removeAt(index) // Hapus item dari daftar
            notifyItemRemoved(index) // Perbarui RecyclerView
        }
    }


    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvRestaurantPrice)
        val tvLocation: TextView = itemView.findViewById(R.id.tvRestaurantLocation)
        val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)
    }
}
