package com.example.foca_mobile.activity.user.cart_order.fragment

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.cart_order.`object`.Food
import com.example.foca_mobile.activity.user.cart_order.adapter.RecyclerViewAdapterCart
import com.example.foca_mobile.databinding.FragmentMyCartBinding


class MyCartFragment : Fragment() {
    private lateinit var binding: FragmentMyCartBinding
    private val listFood = ArrayList<Food>()
    private val adapter : RecyclerViewAdapterCart = RecyclerViewAdapterCart(listFood)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_my_cart, container, true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyCartBinding.bind(view)
        val adapter = RecyclerViewAdapterCart(listFood)

        binding.rvCart.layoutManager = LinearLayoutManager(activity)
        binding.rvCart.adapter = adapter
//        binding.swipeRefreshLayout.setOnRefreshListener {
//            binding.swipeRefreshLayout.isRefreshing = false
//        }
        setItemTouchHelper()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listFood.add(Food("Spacy fresh crab","Waroenk kita",100000,1,R.drawable.image_logo))
        listFood.add(Food("Sushi","Waroenk kita",25000,1,R.drawable.image_logo))
        listFood.add(Food("Beef steak","Waroenk kita",12000,1,R.drawable.image_logo))
        listFood.add(Food("Udon noodles","Waroenk kita",15000,1,R.drawable.image_logo))
        listFood.add(Food("Mỳ tôm hảo hảo","Waroenk kita",20000,1,R.drawable.image_logo))
        listFood.add(Food("Bánh hỏi heo quay","Waroenk kita",30000,1,R.drawable.image_logo))
        adapter.notifyDataSetChanged()

    }

    private fun setItemTouchHelper(){
        ItemTouchHelper(object : ItemTouchHelper.Callback(){

            private val limitScrollX = dipToPx(60f, this@MyCartFragment)
            private var currentScrollX = 0
            private var currentSrollWhenActive = 0
            private var initXWhenActive = 0f
            private var firstInActive = false


            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
                return Integer.MAX_VALUE.toFloat()
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
                    if (dX==0f){
                        currentScrollX = viewHolder.itemView.scrollX
                        firstInActive=true
                    }

                    if (isCurrentlyActive){
                        var scrollOffset = currentScrollX + (-dX).toInt()
                        if(scrollOffset > limitScrollX){
                            scrollOffset = limitScrollX
                        }
                        else if(scrollOffset<0){
                            scrollOffset = 0
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0)
                    }
                    else{
                        if (firstInActive){
                            firstInActive = false
                            currentSrollWhenActive = viewHolder.itemView.scrollX
                            initXWhenActive = dX
                        }

                        if(viewHolder.itemView.scrollX<limitScrollX){
                            viewHolder.itemView.scrollTo((currentSrollWhenActive*dX/initXWhenActive).toInt(), 0)
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                if (viewHolder.itemView.scrollX > limitScrollX){
                    viewHolder.itemView.scrollTo(limitScrollX, 0)
                }
                else if(viewHolder.itemView.scrollX < 0){
                    viewHolder.itemView.scrollTo(0, 0)
                }
            }

        }).apply {

            attachToRecyclerView(binding.rvCart)
        }
    }

    private fun dipToPx(dipValue: Float, context: MyCartFragment): Int{
        return (dipValue * context.resources.displayMetrics.density).toInt()
    }

}