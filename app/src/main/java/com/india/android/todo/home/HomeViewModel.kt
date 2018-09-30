package com.india.android.todo.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.database.*
import com.india.android.todo.Constants
import com.india.android.todo.ToDo
import java.util.ArrayList

/**
 * Created by admin on 29-09-2018.
 */
class HomeViewModel:ViewModel() {
    lateinit var dbReference: DatabaseReference
    private var todoList= MutableLiveData<ArrayList<ToDo>>()
    var keyList=ArrayList<String>()

    fun fetchTodoList(){
        dbReference= FirebaseDatabase.getInstance().reference
        if (!Constants.USER_ID.isNullOrEmpty()){
            dbReference.child(Constants.USER_ID).addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    todoList.postValue(null)
                }

                override fun onDataChange(data: DataSnapshot) {
                    System.out.print(data)
                    if (data.exists()){
                        var todoArrayList=ArrayList<ToDo>()
                        for (i in 0 until data.childrenCount){
                            keyList.add(data.children.elementAt(i.toInt()).key?:"")
                            val todoChild=data.children.elementAt(i.toInt()).getValue(ToDo::class.java)
                            if (todoChild!=null){
                            todoArrayList.add(todoChild)
                            }
                        }
                        todoList.postValue(todoArrayList)
                    }
                    else{
                        todoList.postValue(null)
                    }
                }
            })
        }

    }

    fun getToDoLiveData(): LiveData<ArrayList<ToDo>> {
        return todoList
    }
}