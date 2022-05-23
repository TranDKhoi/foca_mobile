package com.example.foca_mobile.activity.user.cart_order.adapter.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.google.android.material.card.MaterialCardView
import java.lang.ref.WeakReference

class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val view = WeakReference(itemView)
    private lateinit var btnDelete: CardView
    var name: TextView = itemView.findViewById(R.id.cartItemName)
    var option: TextView = itemView.findViewById(R.id.cartItemOption)
    var price: TextView = itemView.findViewById(R.id.cartItemPrice)
    var quantity: TextView = itemView.findViewById(R.id.cartItemQuantity)
    var img: ImageView = itemView.findViewById(R.id.cartItemImage)
    var subQuantity : MaterialCardView = itemView.findViewById(R.id.subtractQuantityCard)
    var addQuantity : MaterialCardView = itemView.findViewById(R.id.addQuantityCard)

    var onDeleteClick: ((RecyclerView.ViewHolder) -> Unit)? = null

    init {
        view.get()?.let {
            it.setOnClickListener {
                if (view.get()?.scrollX != 0) {
                    view.get()?.scrollTo(0, 0)
                }
            }

            btnDelete = it.findViewById(R.id.btnDelete)

            btnDelete.setOnClickListener {
                onDeleteClick?.let { onDeleteClick ->
                    onDeleteClick(this)
                }
            }

        }
    }

    fun updateView() {
        view.get()?.scrollTo(0, 0)
    }
}