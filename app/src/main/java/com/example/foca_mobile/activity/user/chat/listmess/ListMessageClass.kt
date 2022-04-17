package com.example.foca_mobile.activity.user.chat.listmess

import android.os.Parcel
import android.os.Parcelable


class ListMessageClass(val image: Int, val name: String, val lastMess: String, val lastTime: String): Parcelable {
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

    companion object CREATOR : Parcelable.Creator<ListMessageClass> {
        override fun createFromParcel(parcel: Parcel): ListMessageClass {
            return ListMessageClass(parcel)
        }

        override fun newArray(size: Int): Array<ListMessageClass?> {
            return arrayOfNulls(size)
        }
    }


}