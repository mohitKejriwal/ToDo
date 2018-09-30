package com.india.android.todo.create_todo

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.india.android.todo.Constants
import com.india.android.todo.TaskStatus
import com.india.android.todo.ToDo

/**
 * Created by admin on 30-09-2018.
 */
class CreateTodoViewModel:ViewModel() {
    private var taskUploaded=MutableLiveData<Boolean>()

    fun saveTask(title:String){
        val dbReference=FirebaseDatabase.getInstance().reference
        val task= ToDo(title,TaskStatus.HOLD.status,null)

           val childREf= dbReference.child(Constants.USER_ID).push()
            childREf.setValue(task).addOnSuccessListener {
                taskUploaded.postValue(true)
            }.addOnFailureListener {
                taskUploaded.postValue(false)
            }
    }

    fun getTaskUploaded():MutableLiveData<Boolean>{
        return taskUploaded
    }
}