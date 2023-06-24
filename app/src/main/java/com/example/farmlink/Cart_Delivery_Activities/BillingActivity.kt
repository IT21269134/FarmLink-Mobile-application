package com.example.farmlink.Cart_Delivery_Activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlink.Cart_Delivery_Adapters.AddressAdapter
import com.example.farmlink.Cart_Delivery_Modals.AddressData
import com.example.farmlink.Cart_Delivery_Modals.CartModel
import com.example.farmlink.R
import com.google.firebase.database.*



class BillingActivity : AppCompatActivity() {

    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var btnAddAddresses: ImageView
    private lateinit var btn_back: ImageView
    private lateinit var btnOrder: Button
    private lateinit var txtTotal: TextView
    private lateinit var addressList: ArrayList<AddressData>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_billing)

        addressRecyclerView = findViewById(R.id.rvProducts)
        addressRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        addressRecyclerView.setHasFixedSize(true)

        addressList = arrayListOf<AddressData>()


        txtTotal = findViewById(R.id.txtTotal)
        val total = intent.getStringExtra("total")
        txtTotal.text = total

        btnAddAddresses = findViewById(R.id.btnAddAddresses)
        btnAddAddresses.setOnClickListener{
            val intent = Intent(this, AddressAcivity::class.java)
            startActivity(intent)
        }

        btn_back = findViewById(R.id.btn_back)
        btn_back.setOnClickListener{ finish() }

        btnOrder = findViewById(R.id.btnOrder)
        btnOrder.setOnClickListener {

            // Retrieve the cart items from the Firebase database
            val cartItems: MutableList<CartModel> = mutableListOf()
            dbRef = FirebaseDatabase.getInstance().getReference("Cart").child("Unique_User_Id")
            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children) {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartItems.add(cartModel)
                    }

                    // Create a new OrderData object with the selected address, total, and cart items
                    val order = OrderData(addressList[0], total, cartItems)

                    // Push the order to the "Orders" node
                    dbRef = FirebaseDatabase.getInstance().getReference("Orders")
                    dbRef.push().setValue(order)
                    Toast.makeText(this@BillingActivity, "Order Successfull!", Toast.LENGTH_LONG).show()

                    // Navigate to the success delivery activity
                    val intent2 = Intent(this@BillingActivity, SuccessDeliveryActivity::class.java)
                    startActivity(intent2)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                }
            })
        }


        dbRef = FirebaseDatabase.getInstance().getReference("Orders")

        getAddressData()
    }

    data class OrderData(
        val address: AddressData,
        val total: String?,
        val cartItems: List<CartModel>,
    )

    private fun getAddressData(){
        addressRecyclerView.visibility = View.GONE

        val dbRef = FirebaseDatabase.getInstance().getReference("Addresses")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               addressList.clear()
                if(snapshot.exists()){
                    for (addressSnap in snapshot.children){
                        val addressesData = addressSnap.getValue(AddressData::class.java)
                        addressList.add(addressesData!!)
                    }
                    val mAdapter = AddressAdapter(addressList)
                    addressRecyclerView.adapter = mAdapter

                    //to click on the card and navigate
                    mAdapter.setOnItemClickListener(object : AddressAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@BillingActivity, AddressDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("addressId", addressList[position].addressId)
                            intent.putExtra("fullName", addressList[position].fullName)
                            intent.putExtra("addressLine1", addressList[position].addressLine1)
                            intent.putExtra("addressLine2", addressList[position].addressLine2)
                            intent.putExtra("city", addressList[position].city)
                            intent.putExtra("district", addressList[position].district)
                            intent.putExtra("phone", addressList[position].phone)
                            startActivity(intent)
                        }
                    })

                    if (addressList.isNotEmpty()) {
                        // Set the textviews to display the details of the first address in the list
                        var selectedAddress = addressList[0]
                        //to display address
                        val tvFullName = findViewById<TextView>(R.id.tvFullName2)
                        val tvAddressLine1 = findViewById<TextView>(R.id.tvAddressLine1b)
                        val tvAddressLine2 = findViewById<TextView>(R.id.tvAddressLine2b)
                        val tvCity = findViewById<TextView>(R.id.tvCity2)
                        val tvDistrict = findViewById<TextView>(R.id.tvDistrict2)
                        val tvPhone = findViewById<TextView>(R.id.tvPhone2)
                        tvFullName.text = selectedAddress!!.fullName
                        tvAddressLine1.text = selectedAddress!!.addressLine1
                        tvAddressLine2.text = selectedAddress!!.addressLine2
                        tvCity.text = selectedAddress!!.city
                        tvDistrict.text = selectedAddress!!.district
                        tvPhone.text = selectedAddress!!.phone
                    }

                    addressRecyclerView.visibility = View.VISIBLE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}