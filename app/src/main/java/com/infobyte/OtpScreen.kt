package com.infobyte

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.infobyte.databinding.ActivityLoginBinding
import com.infobyte.databinding.ActivityOtpScreenBinding

class OtpScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityOtpScreenBinding
    private lateinit var progressDialog: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_screen)

        binding = ActivityOtpScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Verifying OTP...")

        val password = intent.getStringExtra("password")
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val verid = intent.getStringExtra("verificationId").toString()
        Log.d("DEMO", verid)

        val formattedPhoneNumber = phone!!.take(2) + "******" + phone.takeLast(2)
        binding.phnum.setText(formattedPhoneNumber)

        binding.btnOTP.setOnClickListener {
            val otp = getOTP()
            val verid = intent.getStringExtra("verificationId")
            Log.d("DEMO", "$verid")
            Log.d("DEMO", "$otp")

            if (otp.length != 6) {
                showToast("Please enter a valid 6-digit OTP.")
                return@setOnClickListener
            }

            progressDialog.show()

            val credential = PhoneAuthProvider.getCredential(verid!!, otp)

            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    progressDialog.dismiss()

                    if (task.isSuccessful) {
                        progressDialog.setMessage("Creating User...")
                        progressDialog.show()

                        createUserWithEmailAndPassword(email)
                    } else {
                        showToast("OTP verification failed.")
                    }
                }
        }
    }

    private fun getOTP(): String {
        val otpDigit1 = findViewById<EditText>(R.id.otpDigit1)
        val otpDigit2 = findViewById<EditText>(R.id.otpDigit2)
        val otpDigit3 = findViewById<EditText>(R.id.otpDigit3)
        val otpDigit4 = findViewById<EditText>(R.id.otpDigit4)
        val otpDigit5 = findViewById<EditText>(R.id.otpDigit5)
        val otpDigit6 = findViewById<EditText>(R.id.otpDigit6)

        val otpStringBuilder = StringBuilder()
        otpStringBuilder.append(otpDigit1.text.toString())
        otpStringBuilder.append(otpDigit2.text.toString())
        otpStringBuilder.append(otpDigit3.text.toString())
        otpStringBuilder.append(otpDigit4.text.toString())
        otpStringBuilder.append(otpDigit5.text.toString())
        otpStringBuilder.append(otpDigit6.text.toString())

        return otpStringBuilder.toString()
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun createUserWithEmailAndPassword(email: String?) {
        val password = intent.getStringExtra("password")
        if (email != null) {
            val password = intent.getStringExtra("password")
            auth.createUserWithEmailAndPassword(email, password!!)
                .addOnCompleteListener(this) { task ->
                    progressDialog.dismiss()

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign-Up successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val errorMessage = task.exception?.message ?: "User creation failed."
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}