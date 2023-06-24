package com.example.farmlink.store_manager_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.farmlink.store_manager_models.FeedbackModel
import com.example.farmlink.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FeedbackInsert : AppCompatActivity() {
    private lateinit var etuserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_insert)

        etuserName = findViewById(R.id.etuserName)
        etEmail = findViewById(R.id.etEmail)
        etDescription = findViewById(R.id.etDescription)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Feedback")

        btnSaveData.setOnClickListener {
            saveFeedbackData()
        }
    }

    private fun saveFeedbackData() {
        val userName = etuserName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val description = etDescription.text.toString().trim()

        if (userName.isEmpty()) {
            etuserName.error = "Please enter your Name"
            return
        }
        if (email.isEmpty()) {
            etEmail.error = "Please enter email"
            return
        }
        if (!isValidEmail(email)) {
            etEmail.error = "Please enter a valid email address"
            return
        }
        if (description.isEmpty()) {
            etDescription.error = "Please enter description"
            return
        }

        val userId = dbRef.push().key!!

        val user = FeedbackModel(userId, userName, email, description)

        dbRef.child(userId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etuserName.text.clear()
                etEmail.text.clear()
                etDescription.text.clear()

                val intent = Intent(this, FeedbackFetch::class.java)
                finish()
                startActivity(intent)

            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }
}
