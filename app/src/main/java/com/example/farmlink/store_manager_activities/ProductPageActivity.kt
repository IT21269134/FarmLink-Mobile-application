package com.example.farmlink.store_manager_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlink.store_manager_adapter.SellerAdapter
import com.example.farmlink.store_manager_models.SellerModel
import com.example.farmlink.R
import com.google.firebase.database.*

class ProductPageActivity : AppCompatActivity() {
    private lateinit var sellerRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var sellerList: ArrayList<SellerModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_page)
        sellerRecyclerView = findViewById(R.id.rvSeller)
        sellerRecyclerView.layoutManager = LinearLayoutManager(this)
        sellerRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        sellerList = arrayListOf<SellerModel>()

        getSellerData()
    }
    private fun getSellerData() {

        sellerRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Seller")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sellerList.clear()
                if (snapshot.exists()){
                    for (sellerSnap in snapshot.children){
                        val sellerData = sellerSnap.getValue(SellerModel::class.java)
                        sellerList.add(sellerData!!)
                    }
                    val mAdapter = SellerAdapter(sellerList)
                    sellerRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : SellerAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@ProductPageActivity, ProductDetails::class.java)

                            //put extras
                            intent.putExtra("sellerId", sellerList[position].sellerId)
                            intent.putExtra("sellerName", sellerList[position].sellerName)
                            intent.putExtra("rating", sellerList[position].rating)
                            intent.putExtra("description", sellerList[position].description)
                            startActivity(intent)
                        }

                    })

                    sellerRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}

