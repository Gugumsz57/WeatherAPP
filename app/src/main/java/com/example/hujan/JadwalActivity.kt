package com.example.hujan

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker 
import android.widget.TimePicker.OnTimeChangedListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.util.Calendar
import java.util.Date


class JadwalActivity : AppCompatActivity() {
    private lateinit var timePicker: TimePicker
    private lateinit var setAlarmButton: Button
    private lateinit var tv_jam: TextView
    private var jam = 0
    private var menit = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)
        timePicker = findViewById(R.id.timePicker)
        setAlarmButton = findViewById(R.id.setAlarmButton)
        tv_jam = findViewById(R.id.tv_jam)
        timePicker.setOnTimeChangedListener(OnTimeChangedListener { view, hourOfDay, minute ->
            jam = hourOfDay
            menit = minute
        })
        setAlarmButton.setOnClickListener(View.OnClickListener {
            tv_jam.setText("Set Jadwal $jam : $menit")
            Toast.makeText(this@JadwalActivity, "Set Jadwal $jam : $menit", Toast.LENGTH_SHORT).show()
            setTimer()
            notification()
        })
    }

    private fun notification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Jemuran!!"
            val description = "Waktunya mengangkat jemuran kamu!!"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Notify", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setTimer() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val date = Date()
        val cal_alarm = Calendar.getInstance()
        val cal_now = Calendar.getInstance()
        cal_now.time = date
        cal_alarm.time = date
        cal_alarm[Calendar.HOUR_OF_DAY] = jam
        cal_alarm[Calendar.MINUTE] = menit
        cal_alarm[Calendar.SECOND] = 0
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1)
        }
        val i = Intent(this@JadwalActivity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this@JadwalActivity,
            0,
            i,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager[AlarmManager.RTC_WAKEUP, cal_alarm.timeInMillis] = pendingIntent
    }
}