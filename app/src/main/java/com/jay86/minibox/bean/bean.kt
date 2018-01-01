package com.jay86.minibox.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created By jay68 on 2017/11/21.
 */

data class ApiWrapper<out T>(val status: String,
                             val message: String,
                             @SerializedName("object") val data: T)

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

    companion object {
        @JvmStatic
        val CREATOR: Parcelable.Creator<BoxGroup> = object : Parcelable.Creator<BoxGroup> {
            override fun createFromParcel(parcel: Parcel): BoxGroup {
                return BoxGroup(parcel)
            }

            override fun newArray(size: Int): Array<BoxGroup?> {
                return arrayOfNulls(size)
            }
        }

        fun toJson(boxGroup: BoxGroup): String = Gson().toJson(boxGroup)

        fun fromJson(json: String): BoxGroup? = Gson().fromJson(json, BoxGroup::class.java)
    }

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
}