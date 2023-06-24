package com.example.farmlink.listener

import com.example.farmlink.Cart_Delivery_Modals.ProductDataModel

//For Products preview (delete)
interface IDrinkLoadListener {
    fun onDrinkLoadSuccess(drinkModelList: List<ProductDataModel>?)
    fun onDrinkLoadFailed(message:String?)
}