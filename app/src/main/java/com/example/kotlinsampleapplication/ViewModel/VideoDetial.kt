package com.example.kotlinsampleapplication.ViewModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class VideoDetial : Parcelable {
    var fileName: String = ""
    var type: String = ""
    var path: String = ""
    var startDate: String = ""
    var endDate: String = ""
    var sDate: Date? = null
    var eDate: Date? = null

}