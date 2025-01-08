package com.kel3.tabsy2nd

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestaurantViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_view)

        // Ambil data dari Intent
        val restaurantName = intent.getStringExtra("restaurant_name")
        val restaurantDescription = intent.getStringExtra("restaurant_description")
        val restaurantAddress = intent.getStringExtra("restaurant_address")
        val restaurantPrice = intent.getStringExtra("restaurant_price")
        val restaurantImage = intent.getIntExtra("restaurant_image", R.drawable.ic_img)

        // Bind data ke View
        val ivImage = findViewById<ImageView>(R.id.ivRestaurantImage)
        val tvName = findViewById<TextView>(R.id.tvRestaurantName)
        val tvAddress = findViewById<TextView>(R.id.tvRestaurantAddress)
        val tvPrice = findViewById<TextView>(R.id.tvRestaurantPrice)
        val tvDescription = findViewById<TextView>(R.id.tvRestaurantDescription)
        val btnBookTable = findViewById<Button>(R.id.btnBookTable)

        // Set data
        ivImage.setImageResource(restaurantImage)
        tvName.text = restaurantName
        tvAddress.text = restaurantAddress
        tvPrice.text = "Harga: $restaurantPrice"
        tvDescription.text = restaurantDescription

        // Navigasi ke ReservasiFragment
        btnBookTable.setOnClickListener {
            val reservasiFragment = ReservasiFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.flFragment, reservasiFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
