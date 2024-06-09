//
//package com.example.myalarm
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Intent
//import android.media.Ringtone
//import android.media.RingtoneManager
//import android.net.Uri
//import android.os.Build
//import android.os.Handler
//import android.os.IBinder
//import android.os.Looper
//import androidx.core.app.NotificationCompat
//
//class AlarmService : Service() {
//
//    private lateinit var ringtone: Ringtone
//    private val handler = Handler(Looper.getMainLooper())
//    private val repeatInterval = 10000L // 10 seconds
//
//    private val playRingtoneRunnable = object : Runnable {
//        override fun run() {
//            if (!ringtone.isPlaying) {
//                ringtone.play()
//            }
//            handler.postDelayed(this, repeatInterval)
//        }
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
//            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//
//        ringtone = RingtoneManager.getRingtone(this, alarmUri)
//        ringtone.play()
//        handler.post(playRingtoneRunnable)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel("alarm_service", "Alarm Service", NotificationManager.IMPORTANCE_HIGH)
//            val manager = getSystemService(NotificationManager::class.java)
//            manager.createNotificationChannel(channel)
//        }
//
//        val notificationIntent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
//
//        val stopIntent = Intent(this, AlarmService::class.java).apply {
//            action = "STOP_ALARM"
//        }
//        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)
//
//        val notification = NotificationCompat.Builder(this, "alarm_service")
//            .setContentTitle("Alarm Ringing")
//            .setContentText("Tap to snooze")
//            .setSmallIcon(R.drawable.ic_alarm)
//            .setContentIntent(pendingIntent)
//            .addAction(R.drawable.ic_cancel, "Stop", stopPendingIntent)
//            .build()
//
//        startForeground(1, notification)
//
//        if (intent?.action == "STOP_ALARM") {
//            stopSelf()
//        }
//
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        handler.removeCallbacks(playRingtoneRunnable)
//        ringtone.stop()
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return null
//    }
//}
package com.example.myalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private lateinit var ringtone: Ringtone
    private val handler = Handler(Looper.getMainLooper())
    private val repeatInterval = 10000L // 10 seconds

    private val playRingtoneRunnable = object : Runnable {
        override fun run() {
            if (!ringtone.isPlaying) {
                ringtone.play()
            }
            handler.postDelayed(this, repeatInterval)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        ringtone = RingtoneManager.getRingtone(this, alarmUri)
        ringtone.play()
        handler.post(playRingtoneRunnable)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("alarm_service", "Alarm Service", NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = Intent(this, AlarmService::class.java).apply {
            action = "STOP_ALARM"
        }
        val stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "alarm_service")
            .setContentTitle("Alarm Ringing")
            .setContentText("Tap to snooze")
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_cancel, "Stop", stopPendingIntent)
            .build()

        startForeground(1, notification)

        if (intent?.action == "STOP_ALARM") {
            stopSelf()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(playRingtoneRunnable)
        ringtone.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
