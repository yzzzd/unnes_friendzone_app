package com.friend.zone.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var id: Int?, var name: String?, var phone: String?, var about: String?, var photo: String?, var liked: Boolean?, var likes: Int?): Parcelable