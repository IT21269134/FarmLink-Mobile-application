package com.example.farmlink.Cart_Delivery_Activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlink.Cart_Delivery_Modals.AddressData
import com.example.farmlink.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddressAcivity : AppCompatActivity() {

    private lateinit var edtFullName : EditText
    private lateinit var edtAddressLine1 : EditText
    private lateinit var edtAddressLine2 : EditText
    private lateinit var edtCity : EditText
    private lateinit var edtDistrict : EditText
    private lateinit var edtPhone : EditText
    private lateinit var btnSaveData : Button
    private lateinit var btnBack: ImageView

    private lateinit var dbRef : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        edtFullName = findViewById(R.id.editFullName)
        edtAddressLine1 = findViewById(R.id.editAddress1)
        edtAddressLine2 = findViewById(R.id.editAddress2)
        edtCity = findViewById(R.id.editCity)
        edtDistrict = findViewById(R.id.editDistrict)
        edtPhone = findViewById(R.id.editPhone)
        btnSaveData = findViewById(R.id.btnUpdate)

        btnBack = findViewById(R.id.btn_back)
        btnBack!!.setOnClickListener{ finish() }

        dbRef = FirebaseDatabase.getInstance().getReference("Addresses")

        btnSaveData.setOnClickListener{
            saveAddressData()
        }
    }

    private fun saveAddressData(){

        //getting Values
        val fullName = edtFullName.text.toString()
        val addressLine1 = edtAddressLine1.text.toString()
        val addressLine2 = edtAddressLine2.text.toString()
        val city = edtCity.text.toString()
        val district = edtDistrict.text.toString()
        val phone = edtPhone.text.toString()

        if (fullName.isEmpty()){
            edtFullName.error = "Please enter your name"
        }
        if (addressLine1.isEmpty()){
            edtAddressLine1.error = "Please enter your address"
        }
        if (addressLine2.isEmpty()){
            edtAddressLine2.error = "Please enter your address"
        }
        if (city.isEmpty()){
            edtCity.error = "Please enter your city"
        }
        if (district.isEmpty()){
            edtDistrict.error = "Please enter your district"
        }
        if (phone.isEmpty()){
            edtPhone.error = "Please enter your phone number"
        }

        if(fullName.isEmpty()!= true &&
            addressLine1.isEmpty()!= true &&
            city.isEmpty()!= true &&
            district.isEmpty()!= true &&
            phone.isEmpty()!= true)
        {
            val addressId = dbRef.push().key!!

            val address = AddressData(addressId, fullName, addressLine1, addressLine2, city, district, phone)

            dbRef.child(addressId).setValue(address)
                .addOnCompleteListener {
                    Toast.makeText(this, "Address Inserted Successfully", Toast.LENGTH_LONG).show()
                    edtFullName.text.clear()
                    edtAddressLine1.text.clear()
                    edtAddressLine2.text.clear()
                    edtCity.text.clear()
                    edtDistrict.text.clear()
                    edtPhone.text.clear()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }

    }
}