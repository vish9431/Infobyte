package com.infobyte

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.infobyte.databinding.ActivityLoginBinding
import com.infobyte.databinding.ActivityOtpScreenBinding

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")

        binding.Signup.setOnClickListener {
            val intent = Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }

        binding.btnCnfrm.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Show the progress dialog while logging in
                progressDialog.show()

                // Sign in with email and password
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        // Dismiss the progress dialog
                        progressDialog.dismiss()

                        if (task.isSuccessful) {
                            // Login successful
                            val user = auth.currentUser
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Login failed
                            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Handle empty email or password fields
                Toast.makeText(this, "Please fill in both email and password fields.", Toast.LENGTH_SHORT).show()
            }
        }



    }
}