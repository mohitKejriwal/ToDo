package com.india.android.todo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by admin on 30-09-2018.
 */
@Parcelize
 class ToDo() : Parcelable {
     var title=""
     var comments:ArrayList<String>?=null
     var status=0
    constructor( title:String, status:Int, comments:ArrayList<String>?) : this(){
        this.title=title
        this.status=status
        this.comments=comments
    }
}

enum class TaskStatus(val status: Int) {
    HOLD(1),
    PROGRESS(2),
    COMPLETED(3)
}