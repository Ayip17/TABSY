package com.kel3.tabsy2nd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kel3.tabsy.Restaurant
import com.kel3.tabsy2nd.utils.FavoriteManager
import java.util.*

class BerandaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)

        // Inisialisasi Spinner untuk jumlah orang
        val spinnerPeople = view.findViewById<Spinner>(R.id.people_count)
        val peopleAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.people_count_array,
            android.R.layout.simple_spinner_item
        )
        peopleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPeople.adapter = peopleAdapter

        // Inisialisasi DatePicker untuk memilih tanggal
        val btnDate = view.findViewById<Button>(R.id.date_button)
        btnDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    btnDate.text = selectedDate
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        // Inisialisasi TimePicker untuk memilih waktu
        val btnTime = view.findViewById<Button>(R.id.time_button)
        btnTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                    btnTime.text = selectedTime
                },
                calendar[Calendar.HOUR_OF_DAY],
                calendar[Calendar.MINUTE],
                true
            ).show()
        }

        // Inisialisasi RecyclerView
        val recyclerViewRec = view.findViewById<RecyclerView>(R.id.recommended_restaurants)
        val recyclerViewNear = view.findViewById<RecyclerView>(R.id.nearest_restaurants)
        recyclerViewRec.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewNear.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Data untuk RecyclerView Recommended
        val recommendedRestaurants: MutableList<Restaurant> = ArrayList()
        recommendedRestaurants.add(Restaurant("Resto1", "Restoran A", "Deskripsi Restoran A", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.gacoan))
        recommendedRestaurants.add(Restaurant("Resto2", "Restoran B", "Deskripsi Restoran B", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.jus))
        recommendedRestaurants.add(Restaurant("Resto3", "Restoran C", "Deskripsi Restoran C", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.donat))
        recommendedRestaurants.add(Restaurant("Resto4", "Restoran D", "Deskripsi Restoran D", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.bakso))

        // Data untuk RecyclerView Nearest
        val nearestRestaurants: MutableList<Restaurant> = ArrayList()
        nearestRestaurants.add(Restaurant("Resto5", "Restoran W", "Dekat Lokasi Anda", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.ramen))
        nearestRestaurants.add(Restaurant("Resto6", "Restoran X", "Dekat Lokasi Anda", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.tiramisu))
        nearestRestaurants.add(Restaurant("Resto7", "Restoran Y", "Dekat Lokasi Anda", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.burger))
        nearestRestaurants.add(Restaurant("Resto8", "Restoran Z", "Dekat Lokasi Anda", "10,0000", "Jl. Raya No. 123, Pekanbaru", R.drawable.kopi))


        // Set Adapter untuk Recommended
        val recommendAdapter = RestaurantAdapter(recommendedRestaurants) { restaurant ->
            addFavorite(restaurant)
        }
        recyclerViewRec.adapter = recommendAdapter

        // Set Adapter untuk Nearest
        val nearestAdapter = RestaurantAdapter(nearestRestaurants) { restaurant ->
            addFavorite(restaurant)
        }
        recyclerViewNear.adapter = nearestAdapter
        

        // Tombol Search dengan Validasi
        val searchButton = view.findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {
            val selectedPeople = spinnerPeople.selectedItem.toString()
            val selectedDate = btnDate.text.toString()
            val selectedTime = btnTime.text.toString()

            if (selectedPeople.isEmpty() || selectedDate == "Pilih Tanggal" || selectedTime == "Pilih Waktu") {
                Toast.makeText(requireContext(), "Harap lengkapi semua input", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Mencari restoran untuk $selectedPeople pada $selectedDate pukul $selectedTime",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    private fun addFavorite(restaurant: Restaurant) {
        FavoriteManager.addFavorite(restaurant)
        Toast.makeText(requireContext(), "${restaurant.name} ditambahkan ke Favorit", Toast.LENGTH_SHORT).show()
        Log.d("BerandaFragment", "Added to favorites: ${restaurant.name}")
    }


}
