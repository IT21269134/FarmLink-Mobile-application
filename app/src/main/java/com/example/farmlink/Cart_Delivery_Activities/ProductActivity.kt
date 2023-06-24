package com.example.farmlink.Cart_Delivery_Activities

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farmlink.Cart_Delivery_Adapters.MyProductAdapter
import com.example.farmlink.Cart_Delivery_Eventbus.UpdateCartEvent
import com.example.farmlink.Cart_Delivery_Modals.CartModel
import com.example.farmlink.Cart_Delivery_Modals.ProductDataModel
import com.example.farmlink.Cart_Delivery_Utils.SpaceItemDecoration
import com.example.farmlink.R
import com.example.farmlink.listener.ICartLoadListener
import com.example.farmlink.listener.IDrinkLoadListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nex3z.notificationbadge.NotificationBadge
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ProductActivity : AppCompatActivity(), IDrinkLoadListener, ICartLoadListener {

    lateinit var cartLoadListener: ICartLoadListener
    lateinit var drinkLoadListener: IDrinkLoadListener
    lateinit var recycler_drink: RecyclerView
    lateinit var badge: NotificationBadge
    lateinit var btnCart: FrameLayout

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        if (EventBus.getDefault().hasSubscriberForEvent(UpdateCartEvent::class.java))
            EventBus.getDefault().removeStickyEvent(UpdateCartEvent::class.java)
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUpdateCartEvent(event: UpdateCartEvent){
        countCartFromFirebase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        recycler_drink = findViewById(R.id.recycler_drink)
        badge = findViewById(R.id.badge)
        btnCart = findViewById(R.id.btnCart)
        init()
        LoadDrinkFromFirebase()
        countCartFromFirebase()
    }

    private fun countCartFromFirebase() {
        val cartModels: MutableList<CartModel> = ArrayList()
        FirebaseDatabase.getInstance()
            .getReference("Cart")
            .child("Unique_User_Id")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (cartSnapshot in snapshot.children) {
                        val cartModel = cartSnapshot.getValue(CartModel::class.java)
                        cartModel!!.key = cartSnapshot.key
                        cartModels.add(cartModel)
                    }
                    cartLoadListener.onLoadCartSuccess(cartModels)
                }

                override fun onCancelled(error: DatabaseError) {
                    cartLoadListener.onLoadCartFailed(error.message)
                }
            })
    }


    private fun LoadDrinkFromFirebase() {
        val drinkModels : MutableList<ProductDataModel> = ArrayList()
        FirebaseDatabase.getInstance().getReference("Drink")
            .addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists())
                    {
                        for (drinkSnapshot in snapshot.children)
                        {
                            val drinkModel = drinkSnapshot.getValue(ProductDataModel::class.java)
                            drinkModel!!.key = drinkSnapshot.key
                            drinkModels.add(drinkModel)
                        }
                        drinkLoadListener.onDrinkLoadSuccess(drinkModels)
                    }else{
                        drinkLoadListener.onDrinkLoadFailed("Drink Items not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    drinkLoadListener.onDrinkLoadFailed(error.message)
                }

            })

    }

    private fun init(){
        drinkLoadListener = this
        cartLoadListener = this

//        recycler_drink.adapter = null
        val gridLayoutManager = GridLayoutManager(this,2)
        recycler_drink.layoutManager = gridLayoutManager
        recycler_drink.addItemDecoration(SpaceItemDecoration())

        btnCart.setOnClickListener{ startActivity(Intent(this, CartActivity::class.java))}
    }

    override fun onDrinkLoadSuccess(drinkModelList: List<ProductDataModel>?) {
        val adapter = MyProductAdapter(this,drinkModelList!!, cartLoadListener)
        recycler_drink.adapter = adapter
    }
//    override fun onDrinkLoadSuccess(drinkModelList: List<DrinkModel>?) {
//        if (recycler_drink != null && recycler_drink.adapter == null) {
//            val adapter = MyDrinkAdapter(this, drinkModelList!!, cartLoadListener)
//            recycler_drink.adapter = adapter
//            adapter.notifyDataSetChanged()
//
//        }
//}


    override fun onDrinkLoadFailed(message: String?) {
//        Snackbar.make(, message!!, Snackbar.LENGTH_LONG).show()
        Toast.makeText(this, "Error ${message}", Toast.LENGTH_LONG).show()
    }

    override fun onLoadCartSuccess(cartModelList: List<CartModel>) {
        var cartSum = 0
        for (cartModel in cartModelList!!) cartSum += cartModel!!.quantity
        badge!!.setNumber(cartSum)
        print("Cart Sum: " +cartSum)
    }

    override fun onLoadCartFailed(message: String?) {
//        Snackbar.make(, message!!, Snackbar.LENGTH_LONG).show()
        Toast.makeText(this, "${message}", Toast.LENGTH_LONG).show()
        print(message)
    }
}