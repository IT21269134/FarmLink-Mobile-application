package com.example.farmlink.store_manager_activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.farmlink.R
import com.example.farmlink.store_manager_models.FeedbackModel
import com.google.firebase.database.FirebaseDatabase

class FeedbackDetails : AppCompatActivity() {
    private lateinit var tvUserId: TextView
    private lateinit var tvUserName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_details)
        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("userId").toString(),
                intent.getStringExtra("userName").toString()
            )
        }
        btnDelete.setOnClickListener {
            confirmDeletion(
                intent.getStringExtra("userId").toString()
            )
        }
    }

    private fun openUpdateDialog(
        userId: String,
        userName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.feedback_update_dialog, null)

        mDialog.setView(mDialogView)

        val etuserName = mDialogView.findViewById<EditText>(R.id.etuserName)
        val etEmail = mDialogView.findViewById<EditText>(R.id.etEmail)
        val etDescription = mDialogView.findViewById<EditText>(R.id.etDescription)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etuserName.setText(intent.getStringExtra("userName").toString())
        etEmail.setText(intent.getStringExtra("email").toString())
        etDescription.setText(intent.getStringExtra("description").toString())

        mDialog.setTitle("Updating $userName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            val name = etuserName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter an email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(applicationContext, "Please enter a valid email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter a description", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            updateUserData(
                userId,
                name,
                email,
                description
            )

            Toast.makeText(applicationContext, "FeedbackData Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvUserName.text = name
            tvEmail.text = email
            tvDescription.text = description

            alertDialog.dismiss()
        }
    }

    private fun updateUserData(
        id: String,
        name: String,
        email: String,
        description: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Feedback").child(id)
        val userInfo = FeedbackModel(id, name, email, description)
        dbRef.setValue(userInfo)
    }

    private fun confirmDeletion(
        id: String
    ) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Are you sure you want to delete this record?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                deleteRecord(id)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun deleteRecord(id: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Feedback").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Feedback data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FeedbackFetch::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear the activity stack
            finish() // Finish the current activity
            startActivity(intent)
        }.addOnFailureListener { error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun initView() {
        tvUserId = findViewById(R.id.tvUserId)
        tvUserName = findViewById(R.id.tvUserName)
        tvEmail = findViewById(R.id.tvEmail)
        tvDescription = findViewById(R.id.tvDescription)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvUserId.text = intent.getStringExtra("userId")
        tvUserName.text = intent.getStringExtra("userName")
        tvEmail.text = intent.getStringExtra("email")
        tvDescription.text = intent.getStringExtra("description")

    }
}