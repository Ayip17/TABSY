package com.kel3.tabsy2nd

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(this)


        auth = FirebaseAuth.getInstance()

        val registerUsernameEditText = findViewById<EditText>(R.id.registerUsername)
        val registerPasswordEditText = findViewById<EditText>(R.id.registerPassword)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = registerUsernameEditText.text.toString()
            val password = registerPasswordEditText.text.toString()

            // Validasi input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Implementasi pendaftaran
            auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Pendaftaran berhasil
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                // Arahkan ke LoginActivity atau MainActivity
                finish() // Optional: close RegisterActivity
            } else {
                // Pendaftaran gagal
                Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
        }
    }
}