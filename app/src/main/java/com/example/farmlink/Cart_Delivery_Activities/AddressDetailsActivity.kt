package com.example.farmlink.Cart_Delivery_Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlink.Cart_Delivery_Modals.AddressData
import com.example.farmlink.R
import com.google.firebase.database.FirebaseDatabase

class AddressDetailsActivity : AppCompatActivity() {

    private lateinit var tvAddressId: TextView
    private lateinit var tvFullName: TextView
    private lateinit var tvAddress1: TextView
    private lateinit var tvAddress2: TextView
    private lateinit var tvCity: TextView
    private lateinit var tvDistrict: TextView
    private lateinit var tvPhone: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_address)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("addressId").toString(),
                intent.getStringExtra("fullName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("addressId").toString(),
            )
        }

        btnBack = findViewById(R.id.btn_back)
        btnBack!!.setOnClickListener{ finish() }
    }

    private fun initView(){
        tvAddressId = findViewById(R.id.tvAddressId)
        tvFullName = findViewById(R.id.tvFullName)
        tvAddress1 = findViewById(R.id.tvAddress1)
        tvAddress2 = findViewById(R.id.tvAddress2)
        tvCity = findViewById(R.id.tvCity)
        tvDistrict = findViewById(R.id.tvDistrict)
        tvPhone = findViewById(R.id.tvPhone)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

    }

    private fun setValuesToViews(){
        tvAddressId.text = intent.getStringExtra("addressId")
        tvFullName.text = intent.getStringExtra("fullName")
        tvAddress1.text = intent.getStringExtra("addressLine1")
        tvAddress2.text = intent.getStringExtra("addressLine2")
        tvCity.text = intent.getStringExtra("city")
        tvDistrict.text = intent.getStringExtra("district")
        tvPhone.text = intent.getStringExtra("phone")
    }

    private fun openUpdateDialog(
        addressId: String,
        fullName: String,
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_address_dialog, null)

        mDialog.setView(mDialogView)

        val edtFullName = mDialogView.findViewById<EditText>(R.id.editFullName2)
        val edtAddress1 = mDialogView.findViewById<EditText>(R.id.editAddress1b)
        val edtAddress2 = mDialogView.findViewById<EditText>(R.id.editAddress2b)
        val edtCity = mDialogView.findViewById<EditText>(R.id.editCity2)
        val edtDistrict = mDialogView.findViewById<EditText>(R.id.editDistrict2)
        val edtPhone = mDialogView.findViewById<EditText>(R.id.editPhone2)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        edtFullName.setText(intent.getStringExtra("fullName").toString())
        edtAddress1.setText(intent.getStringExtra("addressLine1").toString())
        edtAddress2.setText(intent.getStringExtra("addressLine2").toString())
        edtCity.setText(intent.getStringExtra("city").toString())
        edtDistrict.setText(intent.getStringExtra("district").toString())
        edtPhone.setText(intent.getStringExtra("phone").toString())

        mDialog.setTitle("Updating $fullName Address Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateAddressData(
                addressId,
                edtFullName.text.toString(),
                edtAddress1.text.toString(),
                edtAddress2.text.toString(),
                edtCity.text.toString(),
                edtDistrict.text.toString(),
                edtPhone.text.toString()
            )
            Toast.makeText(applicationContext, "Address Data Updated Successfully", Toast.LENGTH_LONG).show()

            //setting updated data to the textviews
            tvFullName.text = edtFullName.text.toString()
            tvAddress1.text = edtAddress1.text.toString()
            tvAddress2.text = edtAddress2.text.toString()
            tvCity.text = edtCity.text.toString()
            tvDistrict.text = edtDistrict.text.toString()
            tvPhone.text = edtPhone.text.toString()

            alertDialog.dismiss()
        }

    }

    private fun updateAddressData(
        id: String,
        fullName: String,
        address1: String,
        address2: String,
        city: String,
        district: String,
        phone: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Addresses").child(id)
        val addressInfo = AddressData(id,fullName,address1,address2,city,district,phone)
        dbRef.setValue(addressInfo)
    }

    private fun deleteRecord( id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Addresses").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Address Deleted", Toast.LENGTH_LONG).show()
            val intent = Intent(this, BillingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Address Error ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
}