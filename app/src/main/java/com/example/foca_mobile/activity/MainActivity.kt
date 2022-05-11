package com.example.foca_mobile.activity

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.admin.chat.listconversation.ListConversationFragment
import com.example.foca_mobile.activity.admin.home.AdminHomeFragment
import com.example.foca_mobile.activity.admin.menu.AdminMenu
import com.example.foca_mobile.activity.admin.order.allorder.AdminOrderManagement
import com.example.foca_mobile.activity.user.cart_order.UserMyCart
import com.example.foca_mobile.activity.user.chat.UserChatScreen
import com.example.foca_mobile.activity.user.home.userhome.UserHomeFragment
import com.example.foca_mobile.activity.user.notifi.UserNotificationAdapter
import com.example.foca_mobile.activity.user.profile.UserProfileFragment
import com.example.foca_mobile.databinding.ActivityMainBinding
import com.example.foca_mobile.model.ApiResponse
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.model.Notification
import com.example.foca_mobile.service.NotificationService
import com.example.foca_mobile.service.ServiceGenerator
import com.example.foca_mobile.socket.SocketHandler
import com.example.foca_mobile.utils.ErrorUtils
import com.example.foca_mobile.utils.GlobalObject
import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var socket: Socket
    private lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //SAVE IT TO GLOBAL OBJECT
        GlobalObject.bottomNavigation = binding.bottomNavigation
        GlobalObject.notSeenNotificationList = ArrayList<Int>()

        socket = SocketHandler.getSocket()

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
                    sendNotification(message.sender!!.fullName, message.text!!)
            }

            //get not seen notify
            getUnseenNotify()

            //notification
            socket.on("received_notification") {
                val dataJson = it[0] as JSONObject
                val noti = Gson().fromJson(dataJson.toString(), Notification::class.java)
               GlobalObject.notSeenNotificationList.add(noti.id!!)
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
                Log.d("get_not_seen_conversations", listRoomJsonIds.toString())

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
                    sendNotification(message.sender!!.fullName, message.text!!)
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
    private fun sendNotification(sender: String?, mess: String) {

        val bmLargeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_message)

        val name = "New Message Notification"
        val descriptionText = "This channel is defined in Main Admin"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notifyChannel = NotificationChannel(strCHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notifyChannel)

        val builder = NotificationCompat.Builder(this, strCHANNEL_ID)
            .setSmallIcon(R.drawable.ic_message)
            .setLargeIcon(bmLargeIcon)
            .setContentTitle(sender ?: "You have a new message!")
            .setContentText(mess)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(intNOTIFY_ID, builder.build())
        }
    }
    private fun getUnseenNotify() {
        //CALL API
        var getNotificationCall = ServiceGenerator.buildService(NotificationService::class.java)
            .getUserNotify("false")

        getNotificationCall?.enqueue(object : Callback<ApiResponse<MutableList<Notification>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<MutableList<Notification>>>,
                response: Response<ApiResponse<MutableList<Notification>>>
            ) {
                if (response.isSuccessful) {
                    val res: ApiResponse<MutableList<Notification>> = response.body()!!

                    val listNotification =  GlobalObject.notSeenNotificationList
                        for (i in 0 until res.data.size){
                            listNotification.add(res.data[i].id!!)
                        }
                    Log.d("Check list noti", GlobalObject.notSeenNotificationList.toString())
                } else {
                    val errorRes = ErrorUtils.parseHttpError(response.errorBody()!!);
                    Log.d("Error From Api getNotificationCall: ", errorRes.message)
                    Toast.makeText(applicationContext, errorRes.message, Toast.LENGTH_LONG).show();
                }
            }

            override fun onFailure(
                call: Call<ApiResponse<MutableList<Notification>>>,
                t: Throwable
            ) {
            }
        })
    }
    companion object {
        private const val intNOTIFY_ID = 1
        private const val strCHANNEL_ID = "Channel 1"
    }
}