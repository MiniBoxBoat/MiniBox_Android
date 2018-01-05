package com.jay86.minibox.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created By jay68 on 2017/11/21.
 */

open class ApiWrapper(val status: String, val message: String)

class ObjectApiWrapper<out T>(status: String, message: String, @SerializedName("object") val data: T) : ApiWrapper(status, message)

data class User(@SerializedName("taken") val token: String,
                @SerializedName("userId") val id: String,
                @SerializedName("userName") val nickname: String,
                @SerializedName("image") val avatar: String,
                val phoneNumber: String,
                val trueName: String,
                val age: Int?,
                val email: String,
                val credibility: Int?,
                val useTime: Int?,
                val sex: String,
                var password: String) {
    companion object {
        val PASSWORD_LENGTH = 6..20
        val NICKNAME_LENGTH = 2..8

        fun toJson(user: User): String = Gson().toJson(user)

        fun fromJson(json: String): User? = Gson().fromJson(json, User::class.java)
    }

    override fun toString(): String {
        return toJson(this)
    }
}

data class Box(val boxId: String, val boxSize: String, val boxStatus: String,
               val groupName: String, val openTime: String, val lat: Double, val lng: Double) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(boxId)
        parcel.writeString(boxSize)
        parcel.writeString(boxStatus)
        parcel.writeString(groupName)
        parcel.writeString(openTime)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Box> {
        override fun createFromParcel(parcel: Parcel): Box {
            return Box(parcel)
        }

        override fun newArray(size: Int): Array<Box?> {
            return arrayOfNulls(size)
        }
    }

    private fun usedMinutes(): Long {
        val split = openTime.split("-", " ", ":", ".")
        val now = GregorianCalendar()
        val openTime = GregorianCalendar(split[0].toInt(), split[1].toInt() - 1, split[2].toInt(),
                split[3].toInt(), split[4].toInt(), split[5].toInt())
        return (now.timeInMillis - openTime.timeInMillis) / 1000 / 60
    }

    val price: Double get() = usedMinutes() * 0.05

    val usedTime: String
        get() {
            val usedMinutes = usedMinutes().toInt()
            val hour = usedMinutes / 60
            val minute = usedMinutes % 60
            return when {
                hour == 0 -> "${minute}分钟"
                minute == 0 -> "${hour}小时"
                else -> "${hour}小时${minute}分钟"
            }
        }
}

data class BoxGroup(val groupId: String,
                    val lat: Double,
                    val lng: Double,
                    @SerializedName("position") val title: String,
                    @SerializedName("emptyLargeBoxNum") val largeEmpty: Int,
                    @SerializedName("emptySmallBoxNum") val smallEmpty: Int,
                    @SerializedName("quantity") val sum: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(groupId)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeString(title)
        parcel.writeInt(largeEmpty)
        parcel.writeInt(smallEmpty)
        parcel.writeInt(sum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BoxGroup> {
        override fun createFromParcel(parcel: Parcel): BoxGroup {
            return BoxGroup(parcel)
        }

        override fun newArray(size: Int): Array<BoxGroup?> {
            return arrayOfNulls(size)
        }

        fun toJson(boxGroup: BoxGroup): String = Gson().toJson(boxGroup)

        fun fromJson(json: String): BoxGroup? = Gson().fromJson(json, BoxGroup::class.java)
    }
}