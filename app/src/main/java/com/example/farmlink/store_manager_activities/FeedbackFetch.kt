package com.example.farmlink.store_manager_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlink.store_manager_adapter.FeedbackAdapter
import com.example.farmlink.store_manager_models.FeedbackModel
import com.example.farmlink.R
import com.google.firebase.database.*

class FeedbackFetch : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var userList: ArrayList<FeedbackModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback_fetch)
        userRecyclerView = findViewById(R.id.rvUser)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        userList = arrayListOf<FeedbackModel>()

        getFeedbackData()
    }
    private fun getFeedbackData() {

        userRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Feedback")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()){
                    for (userSnap in snapshot.children){
                        val userData = userSnap.getValue(FeedbackModel::class.java)
                        userList.add(userData!!)
                    }
                    val mAdapter = FeedbackAdapter(userList)
                    userRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FeedbackAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FeedbackFetch, FeedbackDetails::class.java)

                            //put extras
                            intent.putExtra("userId", userList[position].userId)
                            intent.putExtra("userName", userList[position].userName)
                            intent.putExtra("email", userList[position].email)
                            intent.putExtra("description", userList[position].description)
                            startActivity(intent)
                        }

                    })

                    userRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}

