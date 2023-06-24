package com.example.farmlink.Cart_Delivery_Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlink.HomeFragment
import com.example.farmlink.R
import com.example.farmlink.store_manager_activities.MainActivity

class SuccessDeliveryActivity : AppCompatActivity() {

    private lateinit var btnHome : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartdelivery_success)

        btnHome = findViewById(R.id.btnHome)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
