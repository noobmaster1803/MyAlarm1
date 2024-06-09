//package com.example.myalarm
//
//import android.app.AlarmManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TimePicker
//import android.widget.Toast
//import java.util.Calendar
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val timePicker = findViewById<TimePicker>(R.id.timePicker)
//        val setAlarmButton = findViewById<Button>(R.id.setAlarmButton)
//        val cancelAlarmButton = findViewById<Button>(R.id.cancelAlarmButton)
//
//
//        val alarmIntent = Intent(this, AlarmReceiver::class.java)
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
//
//        setAlarmButton.setOnClickListener {
//
//            val calendar = Calendar.getInstance()
//            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
//            val currentMinute = calendar.get(Calendar.MINUTE)
//
//            val selectedHour = timePicker.hour
//            val selectedMinute = timePicker.minute
//
//            if (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute <= currentMinute)) {
//                Toast.makeText(this, "Please choose a time in the future", Toast.LENGTH_SHORT).show()
//            } else {
//                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
//                calendar.set(Calendar.MINUTE, selectedMinute)
//                calendar.set(Calendar.SECOND, 0)
//
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
//                Toast.makeText(this, "Alarm set for ${selectedHour}:${selectedMinute}", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        cancelAlarmButton.setOnClickListener {
//            alarmManager.cancel(pendingIntent)
//            val serviceIntent = Intent(this, AlarmService::class.java)
//            stopService(serviceIntent)
//        }
//
//    }
//}
package com.example.myalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.util.*
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val setAlarmButton = findViewById<Button>(R.id.setAlarmButton)
        val cancelAlarmButton = findViewById<Button>(R.id.cancelAlarmButton)
       // viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.alarmSet.observe(this, Observer { isSet ->
            setAlarmButton.isEnabled = !isSet
            cancelAlarmButton.isEnabled = isSet
        })

        setAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute

            if (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute <= currentMinute)) {
                Toast.makeText(this, "Please choose a time in the future", Toast.LENGTH_SHORT).show()
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                calendar.set(Calendar.SECOND, 0)

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
                Toast.makeText(this, "Alarm set for ${selectedHour}:${selectedMinute}", Toast.LENGTH_SHORT).show()
                viewModel.setAlarm(true)
            }
        }

        cancelAlarmButton.setOnClickListener {
            alarmManager.cancel(pendingIntent)
            val serviceIntent = Intent(this, AlarmService::class.java)
            stopService(serviceIntent)
            viewModel.setAlarm(false)
        }
    }
}
