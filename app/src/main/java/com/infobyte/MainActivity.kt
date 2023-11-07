package com.infobyte

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var list:ArrayList<DataModelItem>
    lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        list = ArrayList()

        val layoutManager = LinearLayoutManager(this)
        val adapter = RecyclerAdapter(list, this)
        recyclerView.layoutManager = layoutManager

        progressDialog = ProgressDialog(this) // Initialize ProgressDialog
        progressDialog.setMessage("Loading...") // Set the message
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)

        progressDialog.show() // Show the progress dialog

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/AKfycbxJ97TFn-5gQBJkKvOlpMdirdFNEx0Pp9pRWLTRSmqqYfcbgUNCW52EY2tdBHSyI2jn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api: ApiInterface = retrofit.create(ApiInterface::class.java)

        val call: Call<DataModel> = api.getData()

        call.enqueue(object : Callback<DataModel?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<DataModel?>, response: Response<DataModel?>) {
                progressDialog.dismiss() // Dismiss the progress dialog

                if (response.isSuccessful) {
                    list.clear()

                    for (myData in response.body()!!) {
                        list.add(myData)
                    }
                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<DataModel?>, t: Throwable) {
                progressDialog.dismiss() // Dismiss the progress dialog in case of an error
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
            }
        })

    }
}