package com.kel3.tabsy2nd

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kel3.tabsy.Restaurant
import com.kel3.tabsy2nd.R
import com.kel3.tabsy2nd.RestaurantViewActivity

class RestaurantAdapter(
    private val restaurantList: List<Restaurant>,
    private val onFavoriteClicked: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Meng-inflate item layout untuk RecyclerView
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant: Restaurant = restaurantList[position]

        // Menampilkan nama dan deskripsi restoran
        holder.tvName.text = restaurant.name
        holder.tvDescription.text = restaurant.description

        // Menampilkan gambar berdasarkan imageResource
        restaurant.imageResource?.let {
            holder.ivImage.setImageResource(it)
        } ?: run {
            holder.ivImage.setImageResource(R.drawable.ic_img) // Gambar default jika imageResource null
        }

        // Menangani klik pada item untuk navigasi ke detail restoran
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RestaurantViewActivity::class.java)
            intent.putExtra("restaurant_name", restaurant.name)
            intent.putExtra("restaurant_description", restaurant.description)
            intent.putExtra("restaurant_address", restaurant.location)
            intent.putExtra("restaurant_price", restaurant.price)
            intent.putExtra("restaurant_image", restaurant.imageResource)

            context.startActivity(intent)
        }

        // Menangani klik tombol favorit
        holder.btnFavorite.setOnClickListener {
            onFavoriteClicked(restaurant) // Memanggil fungsi callback
            Toast.makeText(
                holder.itemView.context,
                "${restaurant.name} ditambahkan ke Favorit",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return restaurantList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // View dari item_restaurant.xml
        val tvName: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvRestaurantDescription)
        val btnFavorite: Button = itemView.findViewById(R.id.btnFavorite)
        val ivImage: ImageView = itemView.findViewById(R.id.tvRestaurantImg) // Tambahkan ImageView
    }
}
