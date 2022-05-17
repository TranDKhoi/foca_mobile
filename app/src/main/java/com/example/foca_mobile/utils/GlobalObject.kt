package com.example.foca_mobile.utils

import android.app.Activity
import android.content.Context
import com.example.foca_mobile.R
import com.example.foca_mobile.model.Filter
import com.example.foca_mobile.model.User
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import java.util.*

object GlobalObject {
    lateinit var notSeenConversationListIdsAdmin: ArrayList<Int>
    lateinit var notSeenConversationListIdsUser: ArrayList<Int>

    lateinit var bottomNavigation: ChipNavigationBar
    var currentSelectedPage = R.id.home

    var isOpenActivity: Boolean = false
    var isChangeLanguage: Boolean = false

    lateinit var locale: Locale

    lateinit var CurrentUser: User

    var filterData: Filter = Filter(
        type = "",
        sort = "-price",
        range = mutableListOf(0f, 500000f),
        wayUp = false
    )

    fun updateNotSeenConversationAdmin(
        activity: Activity,
        roomId: Int,
        isRemove: Boolean = false
    ) {
        val found = notSeenConversationListIdsAdmin.find { item -> item == roomId } != null
        if (!found && !isRemove) {
            notSeenConversationListIdsAdmin.add(roomId)
        }
        if (found && isRemove) {
            notSeenConversationListIdsAdmin.remove(roomId)
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