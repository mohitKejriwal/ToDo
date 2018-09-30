package com.india.android.todo.details_todo

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.india.android.todo.Constants
import com.india.android.todo.R
import com.india.android.todo.TaskStatus
import com.india.android.todo.home.HomeViewModel

class ExpandedTaskActivity : AppCompatActivity() {

    var key=""
    var comments=ArrayList<String>()
    lateinit var adapter:CommentsAdapter
    var status=1
    var statusReqChange=0
    var addCommentReq=""
    lateinit var expandedTaskViewModel: ExpandedTaskViewModel

    val fabNewComment: FloatingActionButton by bind(R.id.fabNewComment)
    val tvNoComment: TextView by bind(R.id.tvNoComments)
    val tvHold: TextView by bind(R.id.tvHold)
    val tvProgress: TextView by bind(R.id.tvProgress)
    val tvComplete: TextView by bind(R.id.tvComplete)
    val rvComment: RecyclerView by bind(R.id.rvComments)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_task)
        setTitle("Comments List")


        status=intent.getIntExtra(Constants.TASK_STATUS,1)
        key=intent.getStringExtra(Constants.TASK_KEY)
        if (intent.getStringArrayListExtra(Constants.TASK_COMMENTS)!=null){
            comments=intent.getStringArrayListExtra(Constants.TASK_COMMENTS)
            setCommentList(comments)              //If Comments are already present
        }

        rvComment.layoutManager=LinearLayoutManager(this)
        expandedTaskViewModel = ViewModelProviders.of(this).get(ExpandedTaskViewModel::class.java)
        expandedTaskViewModel.key=key


        //Setting the current Status
        when (status) {
            TaskStatus.HOLD.status -> changeStatusView(tvHold,tvProgress,tvComplete)
            TaskStatus.PROGRESS.status -> changeStatusView(tvProgress,tvHold,tvComplete)
            else -> changeStatusView(tvComplete,tvHold,tvProgress)
        }


        tvHold.setOnClickListener {
            if (status!=TaskStatus.HOLD.status){
                statusReqChange=TaskStatus.HOLD.status
                expandedTaskViewModel.changeStatus(TaskStatus.HOLD.status)
            }
        }
        tvProgress.setOnClickListener {
            if (status!=TaskStatus.PROGRESS.status){
                statusReqChange=TaskStatus.PROGRESS.status
                expandedTaskViewModel.changeStatus(TaskStatus.PROGRESS.status)
            }
        }
        tvComplete.setOnClickListener {
            if (status!=TaskStatus.COMPLETED.status){
                statusReqChange=TaskStatus.COMPLETED.status
                expandedTaskViewModel.changeStatus(TaskStatus.COMPLETED.status)
            }
        }

        fabNewComment.setOnClickListener {
            val dialog=Dialog(this)
            dialog.setContentView(R.layout.dialog_comment)
            dialog.window.setBackgroundDrawable(null)
            dialog.setCanceledOnTouchOutside(false)
            val submit=dialog.findViewById<TextView>(R.id.tvSubmitComment)
            val etComment=dialog.findViewById<TextView>(R.id.etComment)
            submit.setOnClickListener {
                if (!etComment.text.isNullOrBlank()) {
                    addCommentReq=etComment.text.toString()
                    expandedTaskViewModel.addComment(etComment.text.toString(),comments.size)
                }
                dialog.dismiss()
            }
            dialog.show()
        }

        expandedTaskViewModel.getCommentsUpdate().observe(this, Observer {
            if(it!=null && it){
                Toast.makeText(applicationContext,"Comment added",Toast.LENGTH_SHORT).show()
                comments.add(addCommentReq)
                setCommentList(comments)
            }else{
                Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })

        expandedTaskViewModel.getStatusUpdate().observe(this, Observer {
            if (it!=null && it){
                Toast.makeText(applicationContext,"Status updated",Toast.LENGTH_SHORT).show()
                when (statusReqChange) {
                    TaskStatus.HOLD.status -> {changeStatusView(tvHold,tvProgress,tvComplete)
                    status=TaskStatus.HOLD.status
                    }
                    TaskStatus.PROGRESS.status -> {changeStatusView(tvProgress,tvHold,tvComplete)
                        status=TaskStatus.PROGRESS.status
                    }
                    else -> {changeStatusView(tvComplete,tvHold,tvProgress)
                        status=TaskStatus.COMPLETED.status
                    }
                }
            }else{
                Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })
    }


    fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE){ findViewById<T>(res) }
    }


    private fun changeStatusView(selectedTV:TextView,unselectedTV1:TextView,unselectedTV2:TextView){
        selectedTV.background=getDrawable(R.drawable.bg_btn_colored)
        selectedTV.setTextColor(ContextCompat.getColor(this,android.R.color.white))

        unselectedTV1.background=null
        unselectedTV2.background=null

        unselectedTV1.setTextColor(ContextCompat.getColor(this,R.color.colorGradientStart))
        unselectedTV2.setTextColor(ContextCompat.getColor(this,R.color.colorGradientStart))
    }


    private fun setCommentList(commentList:ArrayList<String>){
        commentList.let { if (it.size>0){
            adapter= CommentsAdapter(it)
            rvComment.visibility=View.VISIBLE
            tvNoComment.visibility=View.GONE
            rvComment.adapter=adapter
        } else{
            rvComment.visibility=View.GONE
            tvNoComment.visibility=View.VISIBLE
        }
        }
    }
}
