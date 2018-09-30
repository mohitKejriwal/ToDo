package com.india.android.todo.details_todo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.india.android.todo.Constants

/**
 * Created by admin on 30-09-2018.
 */
class ExpandedTaskViewModel:ViewModel() {
    private var isStatusUpdate=MutableLiveData<Boolean>()
    private var isCommentUpdate=MutableLiveData<Boolean>()
    var key=""

    fun getStatusUpdate():MutableLiveData<Boolean>{
        return isStatusUpdate
    }

    fun getCommentsUpdate():MutableLiveData<Boolean>{
        return isCommentUpdate
    }

    fun addComment(comment:String,count:Int){
        val dbReference=FirebaseDatabase.getInstance().reference.child(Constants.USER_ID).child(key).child("comments").child(count.toString())
        dbReference.setValue(comment).addOnSuccessListener {
            isCommentUpdate.postValue(true)
        }.addOnFailureListener {
            isCommentUpdate.postValue(false)
        }
    }


    fun changeStatus(status:Int){
        val dbReference=FirebaseDatabase.getInstance().reference.child(Constants.USER_ID).child(key).child("status")
        dbReference.setValue(status).addOnSuccessListener {
            isStatusUpdate.postValue(true)
        }.addOnFailureListener {
            isStatusUpdate.postValue(false)
        }
    }
}