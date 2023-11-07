package com.infobyte

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.infobyte.databinding.ActivityLoginBinding
import com.infobyte.databinding.ActivityOtpScreenBinding
import com.infobyte.databinding.ActivitySignupBinding
import java.util.concurrent.TimeUnit

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this@Signup, Login::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Sending OTP...")

        binding.btnotp.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etphone.text.toString()
            val password = binding.etPass.text.toString()
            val confirmPassword = binding.etConfirmpass.text.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                showToast("All fields are required.")
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showToast("Password and Confirm Password do not match.")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showToast("Password should be at least 6 characters.")
                return@setOnClickListener
            }

            progressDialog.show()

            // Send OTP to the provided phone number
            val phoneNumber = "+91$phone" // Adjust the country code as needed
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // This is called when verification is automatically completed, e.g., when the code is received without user input.
                        // You can proceed to the next step.
                        progressDialog.dismiss()
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        // This is called when verification fails, handle the error as needed.
                        progressDialog.dismiss()
                        showToast("Verification failed. Please try again.")
                    }

                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                        // This is called when the verification code is successfully sent to the user's phone.
                        // You should save the `verificationId` for later use.
                        this@Signup.verificationId = verificationId
                        progressDialog.dismiss()

                        // Proceed to OTP verification screen
                        val intent = Intent(this@Signup, OtpScreen::class.java)
                        intent.putExtra("verificationId", verificationId)
                        intent.putExtra("password", password)
                        intent.putExtra("email", email)
                        intent.putExtra("phone", phone)
                        startActivity(intent)
                        finish()
                    }
                })
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}