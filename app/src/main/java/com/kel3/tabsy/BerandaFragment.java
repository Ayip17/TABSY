package com.kel3.tabsy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BerandaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout fragment_beranda
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        // Inisialisasi Spinner
        Spinner spinnerPeople = view.findViewById(R.id.people_count);

        // Isi Spinner dengan data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.people_count_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeople.setAdapter(adapter);

        // Inisialisasi Button untuk memilih tanggal
        Button btnDate = view.findViewById(R.id.date_button);
        btnDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    requireContext(),
                    (view1, year, month, dayOfMonth) -> {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        // Inisialisasi Button untuk memilih waktu
        Button btnTime = view.findViewById(R.id.time_button);
        btnTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePicker = new TimePickerDialog(
                    requireContext(),
                    (view12, hourOfDay, minute) -> {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        btnTime.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePicker.show();
        });

        // Inisialisasi RecyclerView
        RecyclerView recyclerViewRec = view.findViewById(R.id.recommended_restaurants);
        RecyclerView recyclerViewNear = view.findViewById(R.id.nearest_restaurants);
        recyclerViewRec.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewNear.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        // Data contoh untuk RecyclerView
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(new Restaurant("Restoran A", "Deskripsi Restoran A"));
        restaurants.add(new Restaurant("Restoran B", "Deskripsi Restoran B"));
        restaurants.add(new Restaurant("Restoran C", "Deskripsi Restoran C"));
        restaurants.add(new Restaurant("Restoran C", "Deskripsi Restoran D"));


        // Set Adapter
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(restaurants);
        recyclerViewRec.setAdapter(restaurantAdapter);


        // Return View di akhir metode
        return view;
    }
}
