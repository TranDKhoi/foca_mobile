package com.example.foca_mobile.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.foca_mobile.R
import com.example.foca_mobile.activity.user.profile.generalsetting.GeneralSetting
import com.example.foca_mobile.model.User
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import java.util.*

object GlobalObject {
    lateinit var notSeenConversationListIdsAdmin: ArrayList<Int>
    lateinit var notSeenConversationListIdsUser: ArrayList<Int>

    lateinit var bottomNavigation: ChipNavigationBar
    var isOpenActivity: Boolean = false

    lateinit var locale: Locale

    lateinit var CurrentUser: User

    fun updateNotSeenConversationAdmin(activity: Activity, roomId: Int, isRemove: Boolean = false) {
        val found = notSeenConversationListIdsAdmin.find { item -> item == roomId } != null
        if (!found && !isRemove) {
            notSeenConversationListIdsAdmin.add(roomId!!)
        }
        if (found && isRemove) {
            notSeenConversationListIdsAdmin.remove(roomId!!)
        }
        val badgeNum = notSeenConversationListIdsAdmin.size
        if (badgeNum > 0)
            activity.runOnUiThread {
                bottomNavigation.showBadge(R.id.message, badgeNum)
            }
        else {
            bottomNavigation.setMenuResource(R.menu.admin_menu)
            bottomNavigation.setItemSelected(R.id.message)
        }
    }

    fun setLocale(context: Context, localeName: String) {
        locale = Locale(localeName)
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
    }
}