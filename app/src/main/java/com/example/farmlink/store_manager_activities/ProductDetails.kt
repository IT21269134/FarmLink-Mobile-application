package com.example.farmlink.store_manager_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.farmlink.Cart_Delivery_Activities.ProductActivity
import com.example.farmlink.R

class ProductDetails : AppCompatActivity() {
    private lateinit var tvSellerId: TextView
    private lateinit var tvSellerName: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnToFeedback: Button
    private lateinit var btnToProducts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        initView()
        setValuesToViews()

        btnToFeedback = findViewById(R.id.feedback)
        btnToProducts = findViewById(R.id.products)


        btnToFeedback.setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }
        btnToProducts.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }


    }


    private fun initView() {
        tvSellerId = findViewById(R.id.tvSellerId)
        tvSellerName = findViewById(R.id.tvSellerName)
        tvRating = findViewById(R.id.tvRating)
        tvDescription = findViewById(R.id.tvDescription)

    }

    private fun setValuesToViews() {
        tvSellerId.text = intent.getStringExtra("sellerId")
        tvSellerName.text = intent.getStringExtra("sellerName")
        tvRating.text = intent.getStringExtra("rating")
        tvDescription.text = intent.getStringExtra("description")

    }
}