package com.example.foca_mobile.activity.admin.chat.listmess

import android.os.Parcel
import android.os.Parcelable


class VirtualConversation(
    val image: Int,
    val name: String,
    val lastMess: String,
    val lastTime: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(name)
        parcel.writeString(lastMess)
        parcel.writeString(lastTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VirtualConversation> {
        override fun createFromParcel(parcel: Parcel): VirtualConversation {
            return VirtualConversation(parcel)
        }

        override fun newArray(size: Int): Array<VirtualConversation?> {
            return arrayOfNulls(size)
        }
    }

}