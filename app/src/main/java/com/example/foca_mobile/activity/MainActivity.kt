package com.example.foca_mobile.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.chat.listconversation.ListConversationFragment
import com.example.foca_mobile.activity.admin.home.AdminHomeFragment
import com.example.foca_mobile.activity.admin.menu.AdminMenu
import com.example.foca_mobile.activity.admin.order.allorder.AdminOrderManagement
import com.example.foca_mobile.activity.admin.order.orderdetail.AdminOrderDetail
import com.example.foca_mobile.activity.user.cart_order.UserDetailOrder
import com.example.foca_mobile.activity.user.cart_order.UserMyCart
import com.example.foca_mobile.activity.user.chat.UserChatScreen
import com.example.foca_mobile.activity.user.home.userhome.UserHomeFragment
import com.example.foca_mobile.activity.user.profile.UserProfileFragment
import com.example.foca_mobile.databinding.ActivityMainBinding
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.model.Notification
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.GlobalObject
import com.example.foca_mobile.utils.OnboardingPrefs
import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var socket: Socket
    private lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appContext = this

        OnboardingPrefs.setFirsttime("firsttime")

        //SAVE IT TO GLOBAL OBJECT
        GlobalObject.bottomNavigation = binding.bottomNavigation

        socket = SocketHandler.getSocket()

        //notification
        socket.on("received_notification") {
            val dataJson = it[0] as JSONObject
            val noti = Gson().fromJson(dataJson.toString(), Notification::class.java)
            Log.d("Check received_notification", noti.toString())
            sendOrderNotification(noti)
        }

        //THIS IS WHERE WE DECIDE WHO IS LOGIN
        if (GlobalObject.CurrentUser.role == "USER") {
            //USER FRAGMENT
            val userHomeFragment = UserHomeFragment()
            val cartFragment = UserMyCart()
            val profileFragment = UserProfileFragment()

            binding.bottomNavigation.setMenuResource(R.menu.user_menu)
            binding.bottomNavigation.setItemSelected(GlobalObject.currentSelectedPage)
            if (GlobalObject.currentSelectedPage == R.id.home)
                setCurrentFragment(userHomeFragment)
            else
                setCurrentFragment(profileFragment)

            socket.emit("get_not_seen_conversations", Ack {
                val listRoomJsonIds = it[0] as JSONArray
                Log.d("get_not_seen_conversations", listRoomJsonIds.toString())
                val listRoomIds =
                    Gson().fromJson(listRoomJsonIds.toString(), ArrayList<Int>()::class.java)
                GlobalObject.notSeenConversationListIdsUser = listRoomIds
                val badgeNum = listRoomIds.size
                if (badgeNum > 0)
                    runOnUiThread {
                        binding.bottomNavigation.showBadge(R.id.message)
                    }
            })

            socket.on("received_message") {
                val messageJson = it[0] as JSONObject
                val message = Gson().fromJson(messageJson.toString(), Message::class.java)
                binding.bottomNavigation.showBadge(R.id.message)
                if (!GlobalObject.isOpenActivity)
                    sendMessageNotification(message.sender!!.fullName, message.text!!)
            }
            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> {
                        setCurrentFragment(userHomeFragment)
                        GlobalObject.currentSelectedPage = R.id.home
                    }
                    R.id.message -> {
                        userToAdminChat()
                        binding.bottomNavigation.setItemSelected(GlobalObject.currentSelectedPage)
                    }
                    R.id.cart -> {
                        setCurrentFragment(cartFragment)
                        GlobalObject.currentSelectedPage = R.id.cart
                    }
                    R.id.user -> {
                        setCurrentFragment(profileFragment)
                        GlobalObject.currentSelectedPage = R.id.user
                    }
                }
            }
        } else if (GlobalObject.CurrentUser.role == "ADMIN") {
            //ADMIN FRAGMENT
            val adminHomeFragment = AdminHomeFragment()
            val messageFragment = ListConversationFragment()
            val orderFragment = AdminOrderManagement()
            val menuFragment = AdminMenu()
            val profileFragment = UserProfileFragment()

            binding.bottomNavigation.setMenuResource(R.menu.admin_menu)
            binding.bottomNavigation.setItemSelected(GlobalObject.currentSelectedPage)
            if (GlobalObject.currentSelectedPage == R.id.home)
                setCurrentFragment(adminHomeFragment)
            else
                setCurrentFragment(profileFragment)


            socket.emit("get_not_seen_conversations", Ack {
                val listRoomJsonIds = it[0] as JSONArray

                val listRoomIds =
                    Gson().fromJson(listRoomJsonIds.toString(), ArrayList<Int>()::class.java)
                GlobalObject.notSeenConversationListIdsAdmin = listRoomIds
                val badgeNum = listRoomIds.size
                if (badgeNum > 0)
                    runOnUiThread {
                        binding.bottomNavigation.showBadge(R.id.message, badgeNum)
                    }
            })

            socket.on("received_message") {
                val messageJson = it[0] as JSONObject
                message =
                    Gson().fromJson(messageJson.toString(), Message::class.java)
                GlobalObject.updateNotSeenConversationAdmin(
                    this@MainActivity,
                    message.roomId!!
                )
                if (!GlobalObject.isOpenActivity)
                    sendMessageNotification(message.sender!!.fullName, message.text!!)
            }

            binding.bottomNavigation.setOnItemSelectedListener { id ->
                when (id) {
                    R.id.home -> {
                        setCurrentFragment(adminHomeFragment)
                        GlobalObject.currentSelectedPage = R.id.home
                    }
                    R.id.message -> {
                        setCurrentFragment(messageFragment)
                        GlobalObject.currentSelectedPage = R.id.message
                    }
                    R.id.order -> {
                        setCurrentFragment(orderFragment)
                        GlobalObject.currentSelectedPage = R.id.order
                    }
                    R.id.menu -> {
                        setCurrentFragment(menuFragment)
                        GlobalObject.currentSelectedPage = R.id.menu
                    }
                    R.id.user -> {
                        setCurrentFragment(profileFragment)
                        GlobalObject.currentSelectedPage = R.id.user
                    }
                }
            }
        }
    }

    override fun onRestart() {
        super.onRestart()

        if (GlobalObject.isChangeLanguage) {
            when (GlobalObject.CurrentUser.role) {
                "USER" -> {
                    GlobalObject.bottomNavigation.setMenuResource(R.menu.user_menu)
                    GlobalObject.bottomNavigation.setItemSelected(R.id.user)
                }
                "ADMIN" -> {
                    GlobalObject.bottomNavigation.setMenuResource(R.menu.admin_menu)
                    GlobalObject.bottomNavigation.setItemSelected(R.id.user)
                }
            }
            GlobalObject.isChangeLanguage = false
        }
    }

    private fun userToAdminChat() {
        val intent = Intent(this, UserChatScreen::class.java)
        startActivity(intent)
        binding.bottomNavigation.setMenuResource(R.menu.user_menu)
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.wrapper, fragment)
            commit()
        }

    //HIDE KEYBOARD WHEN LOST FOCUS
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    //NOTIFY MESSAGE AREA
    private fun sendMessageNotification(sender: String?, mess: String) {

        val bmLargeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_message)

        val name = "New Message Notification"
        val descriptionText = "This channel is defined in Main Admin"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notifyChannel = NotificationChannel(strCHANNEL_ID1, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifyChannel)

        val builder = NotificationCompat.Builder(this, strCHANNEL_ID1)
            .setSmallIcon(R.drawable.ic_message)
            .setLargeIcon(bmLargeIcon)
            .setContentTitle(sender ?: resources.getString(R.string.Youhavenewmessage))
            .setContentText(mess)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(intNOTIFY_ID1, builder.build())
        }
    }

    //NOTIFY NOTIFY AREA
    private fun sendOrderNotification(item: Notification) {

        var bmLargeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_order)
        var message = ""

        when (item.order!!.status) {
            "PENDING" -> {
                bmLargeIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_pending)
                message = resources.getString(R.string.YourOrder).plus(" ")
                    .plus(resources.getString(R.string.UPendingNoti))
            }
            "CANCELLED" -> {
                bmLargeIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_cancel)
                message = resources.getString(R.string.YourOrder).plus(" ")
                    .plus(resources.getString(R.string.UCancelledNoti))
            }
            "COMPLETED" -> {
                bmLargeIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_success)
                message = resources.getString(R.string.YourOrder).plus(" ")
                    .plus(resources.getString(R.string.UCompletedNoti))
            }
            "PROCESSED" -> {
                bmLargeIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_success)
                message = resources.getString(R.string.YourOrder).plus(" ")
                    .plus(resources.getString(R.string.UProcessedNoti))
            }
            "ARRIVED" -> {
                bmLargeIcon =
                    BitmapFactory.decodeResource(resources, R.drawable.ic_pending)
                message = resources.getString(R.string.YourOrder).plus(" ")
                    .plus(resources.getString(R.string.UArrivedorder))
            }
        }

        val name = "Order Notification"
        val descriptionText = "This channel is defined in Main Admin"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notifyChannel = NotificationChannel(strCHANNEL_ID2, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifyChannel)


        // Create an Intent for the activity you want to start
        var resultIntent = Intent()
        if (GlobalObject.CurrentUser.role == "ADMIN") {
            resultIntent = Intent(this, AdminOrderDetail::class.java)
            resultIntent.putExtra("orderid", item.order!!.id)
        } else {
            resultIntent = Intent(this, UserDetailOrder::class.java)
            resultIntent.putExtra("orderid", item.order!!.id)
        }
        val resultPendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            resultIntent, 0
        )

        val builder = NotificationCompat.Builder(this, strCHANNEL_ID2)
            .setSmallIcon(R.drawable.ic_order)
            .setLargeIcon(bmLargeIcon)
            .setContentTitle(resources.getString(R.string.Youhavenews))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(intNOTIFY_ID2, builder.build())
        }
    }

    companion object {
        private const val intNOTIFY_ID1 = 1
        private const val intNOTIFY_ID2 = 1
        private const val strCHANNEL_ID1 = "Message channel"
        private const val strCHANNEL_ID2 = "Order channel"
        lateinit var appContext: Context
    }
}