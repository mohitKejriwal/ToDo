
package com.india.android.todo.create_todo

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.india.android.todo.R
import android.app.DatePickerDialog
import java.util.*
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.india.android.todo.notification.NotificationUtils


class CreateTodoActivity : AppCompatActivity() {

    lateinit var createViewModel:CreateTodoViewModel
    val etTitle:EditText by bind(R.id.tiTaskTitle)
    val startTimePicker: TextView by bind(R.id.tvStartTime)
    val submitTask: TextView by bind(R.id.tvSubmitTask)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_todo)
        createViewModel = ViewModelProviders.of(this).get(CreateTodoViewModel::class.java)

        startTimePicker.setOnClickListener {
           dateTimePicker()
        }

        submitTask.setOnClickListener {
            if (etTitle.text.isNullOrBlank()){
                Toast.makeText(this,"Please enter task title",Toast.LENGTH_SHORT).show()
            }else{
                createViewModel.saveTask(etTitle.text.toString())
            }
        }

        createViewModel.getTaskUploaded().observe(this,android.arch.lifecycle.Observer {
            if (it!=null && it){
                Toast.makeText(applicationContext,"Task uploaded",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(applicationContext,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dateTimePicker(){
        val c = Calendar.getInstance()
        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)
        var date=c.get(Calendar.DATE)
        var month=c.get(Calendar.MONTH)
        var year=c.get(Calendar.YEAR)


        val datePicker = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val calendar=Calendar.getInstance()
                    val timePicker = TimePickerDialog(this,
                            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                calendar.set(year,monthOfYear+1,dayOfMonth,hourOfDay,minute)
                                NotificationUtils.setNotification(calendar.timeInMillis,applicationContext)
                            },
                            hour, minute,true
                    )

                    timePicker.show()
                }, year, month, date)
        datePicker.datePicker.minDate=System.currentTimeMillis()
        datePicker.show()
    }


    fun <T : View> Activity.bind(@IdRes res : Int) : Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE){ findViewById<T>(res) }
    }
}
