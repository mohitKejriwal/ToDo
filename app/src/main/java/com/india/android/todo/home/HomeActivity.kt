package com.india.android.todo.home

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.india.android.todo.R
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.firebase.ui.auth.IdpResponse
import android.content.Intent
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.india.android.todo.Constants
import com.india.android.todo.ListClickListener
import com.india.android.todo.ToDo
import com.india.android.todo.create_todo.CreateTodoActivity
import com.india.android.todo.details_todo.ExpandedTaskActivity
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(),ListClickListener {

    lateinit var homeViewModel: HomeViewModel
    lateinit var auth:FirebaseAuth
    lateinit var dataList:ArrayList<ToDo>
    var user:FirebaseUser?=null
    val fabNewTask:FloatingActionButton by bind(R.id.fabNewTask)
    val tvNoTasks:TextView by bind(R.id.tvNoTasks)
    val rvTasks:RecyclerView by bind(R.id.rvTasks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        homeViewModel.getToDoLiveData().observe(this, Observer<ArrayList<ToDo>> {
            //Called whenever there is change in task list
            if(it!=null && it.size>0){
                dataList=it
                tvNoTasks.visibility=View.GONE
                rvTasks.visibility=View.VISIBLE
                rvTasks.layoutManager=LinearLayoutManager(this)
                val adapter=TasksAdapter(it,this)
                rvTasks.adapter=adapter
            }else{
                tvNoTasks.visibility=View.VISIBLE
                rvTasks.visibility=View.GONE
            }
        })

        fabNewTask.setOnClickListener {
            val intentCreateTask=Intent(this,CreateTodoActivity::class.java)
            startActivity(intentCreateTask)
        }
    }

    override fun onStart() {
        super.onStart()
        //Check for signed in user
        if (user==null){
            googleSignIn()
        }else{
            Constants.USER_ID=user?.uid?:""
            homeViewModel.fetchTodoList()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                Constants.USER_ID=user?.uid?:""
                homeViewModel.fetchTodoList()
            } else {
                Toast.makeText(applicationContext,"error",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun googleSignIn(){
        val providers = Arrays.asList(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                Constants.RC_SIGN_IN)
    }


    fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE){ findViewById<T>(res) }
    }


    override fun onClick(pos: Int) {
        val expandTaskIntent=Intent(this,ExpandedTaskActivity::class.java)
        expandTaskIntent.putExtra(Constants.TASK_KEY,homeViewModel.keyList[pos])
        expandTaskIntent.putExtra(Constants.TASK_STATUS,dataList[pos].status)
        expandTaskIntent.putStringArrayListExtra(Constants.TASK_COMMENTS,dataList[pos].comments)
        startActivity(expandTaskIntent)
    }


}
