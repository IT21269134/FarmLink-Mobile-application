package com.example.farmlink.Cart_Delivery_Activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlink.R

class ActivityPlaceOrder : AppCompatActivity(){

//    val addressId = intent.getStringExtra("addressId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placeorder)

        val fullName = intent.getStringExtra("fullName")
        val addressLine1 = intent.getStringExtra("addressLine1")
        val addressLine2 = intent.getStringExtra("addressLine2")
        val city = intent.getStringExtra("city")
        val district = intent.getStringExtra("district")
        val phone = intent.getStringExtra("phone")

        findViewById<TextView>(R.id.tvFullName2).text = fullName
        findViewById<TextView>(R.id.tvAddressLine1b).text = addressLine1
        findViewById<TextView>(R.id.tvAddressLine2b).text = addressLine2
        findViewById<TextView>(R.id.tvCity2).text = city
        findViewById<TextView>(R.id.tvDistrict2).text = district
        findViewById<TextView>(R.id.tvPhone2).text = phone


    }
}