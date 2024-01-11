package com.example.hujan

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val i = Intent(context, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(2000)
        val builder = NotificationCompat.Builder(context, "Notify")
            .setSmallIcon(R.drawable.cloudy_sunny)
            .setContentText("Jemuran!!")
            .setContentText("Hey, Sudah waktunya mengangkat jemuran!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.notify(200, builder.build())
        val sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.jadwal)
        val r = RingtoneManager.getRingtone(context, sound)
        r.play()
    }
}