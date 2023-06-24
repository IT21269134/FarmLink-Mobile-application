package com.example.farmlink
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlink.store_manager_activities.ProductDetails
import com.example.farmlink.store_manager_adapter.SellerAdapter
import com.example.farmlink.store_manager_models.SellerModel
import com.google.firebase.database.*


class HomeFragment : Fragment() {
    private lateinit var sellerRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var sellerList: ArrayList<SellerModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        sellerRecyclerView = view.findViewById(R.id.rvSeller)
        sellerRecyclerView.layoutManager = LinearLayoutManager(activity)
        sellerRecyclerView.setHasFixedSize(true)
        tvLoadingData = view.findViewById(R.id.tvLoadingData)

        sellerList = arrayListOf<SellerModel>()

        getSellerData()

        return view
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

                            val intent = Intent(activity, ProductDetails::class.java)

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
                // Handle error
            }

        })
    }

}