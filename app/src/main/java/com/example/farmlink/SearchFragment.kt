package com.example.farmlink
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.example.farmlink.store_manager_adapter.FeedbackAdapter
import com.example.farmlink.store_manager_models.FeedbackModel
import com.example.farmlink.store_manager_activities.FeedbackDetails
import com.example.farmlink.store_manager_activities.ProductDetails
import com.example.farmlink.store_manager_adapter.SellerAdapter
import com.example.farmlink.store_manager_models.SellerModel


class SearchFragment : Fragment() {
    private lateinit var sellerRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var sellerList: ArrayList<SellerModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var originalSellerList: ArrayList<SellerModel>
    private lateinit var searchBox: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        sellerRecyclerView = view.findViewById(R.id.rvSeller)
        sellerRecyclerView.layoutManager = LinearLayoutManager(activity)
        sellerRecyclerView.setHasFixedSize(true)
        tvLoadingData = view.findViewById(R.id.tvLoadingData)
        searchBox = view.findViewById(R.id.etSearch)

        sellerList = arrayListOf<SellerModel>()
        originalSellerList = arrayListOf<SellerModel>()

        getSellerData()

        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchSellerData(s.toString())
            }
        })

        return view
    }

    private fun getSellerData() {
        sellerRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Seller")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                originalSellerList.clear()
                sellerList.clear()
                if (snapshot.exists()){
                    for (sellerSnap in snapshot.children){
                        val sellerData = sellerSnap.getValue(SellerModel::class.java)
                        originalSellerList.add(sellerData!!)
                    }
                    sellerList.addAll(originalSellerList)
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

    private fun searchSellerData(query: String?) {
        sellerList.clear()
        for (seller in originalSellerList) {
            if (seller.sellerName?.contains(query ?: "", true) == true) {
                sellerList.add(seller)
            }
        }
        sellerRecyclerView.adapter?.notifyDataSetChanged()
    }
}