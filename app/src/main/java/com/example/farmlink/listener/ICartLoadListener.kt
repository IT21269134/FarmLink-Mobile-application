package com.example.farmlink.listener

import com.example.farmlink.Cart_Delivery_Modals.CartModel

interface ICartLoadListener {
    fun onLoadCartSuccess(cartModelList: List<CartModel>)
    fun onLoadCartFailed(message:String?)
}