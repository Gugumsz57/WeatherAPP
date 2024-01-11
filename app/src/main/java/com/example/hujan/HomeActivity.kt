package com.example.hujan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hujan.adapter.CuacaAdapter
import com.example.hujan.api.ApiConfig
import com.example.hujan.response.ResponseCuaca
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    private val CHANNEL_ID = "MyChannel"
    private lateinit var databaseReference: DatabaseReference
    private lateinit var cuaca: TextView
    private lateinit var suhu: TextView
    private lateinit var btn_jadwal: ImageView
    private lateinit var profile: ImageView
    private lateinit var backgroundImageView: ImageView
    private lateinit var tv_suhuhariini : TextView
    private lateinit var tv_kelembapanhariini : TextView
    private lateinit var cuacaperjam : TextView
    private lateinit var tv_waktu: TextView
    private lateinit var iv_awan : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        getcuacaperhari()
        setDateIndo()


    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun setDateIndo() {
        // Mendapatkan tanggal dan waktu saat ini
        val currentDate = Date()

        // Membuat objek SimpleDateFormat dengan pola dan locale yang diinginkan
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
        // Mengonversi tanggal dan waktu ke format Indonesia
        val formattedDate = dateFormat.format(currentDate)

        tv_waktu = findViewById(R.id.tv_waktu)
        tv_waktu.text = formattedDate.toString()
    }

    fun getcuacaperhari(){

        ApiConfig.ApiConfig.instanceRetrofit.getCuaca().enqueue(object : retrofit2.Callback<ResponseCuaca>{
            override fun onResponse(call: Call<ResponseCuaca>, response: Response<ResponseCuaca>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()

                    // mengambil data cuaca perjam
                    val params = weatherData!!.data!!.params
                    if (params != null){
                        for (param in params){
                            if(param!!.id == "t"){
                                val suhuperhari = param.times
                                val suhuhariini = suhuperhari!!.take(5)
                                val recyclerView: RecyclerView = findViewById(R.id.rv_cuacaperhari)
                                val layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
                                recyclerView.layoutManager = layoutManager

                                // Set adapter untuk RecyclerView
                                val adapter = CuacaAdapter(suhuhariini)
                                recyclerView.adapter = adapter

                            }

                            if(param.id == "weather"){
                                val calendar = Calendar.getInstance()
                                val currentDateTime = getCurrentDateTime()
                                calendar.time = currentDateTime

                                // Menghitung waktu yang akan dibandingkan berdasarkan selisih jam
                                calendar.add(Calendar.HOUR_OF_DAY, -6)
                                val targetDateTime = calendar.time

                                // Iterasi pada list times
                                for (time in param.times!!) {
                                    // Mengonversi datetime JSON ke tipe data Date
                                    val jsonDateTime = SimpleDateFormat("yyyyMMddHHmm").parse(time!!.datetime)

                                    // Memeriksa apakah datetime JSON berada dalam rentang waktu yang diinginkan
                                    if (jsonDateTime.after(targetDateTime) && jsonDateTime.before(currentDateTime)) {
                                        cuacaperjam = findViewById(R.id.tv_cuacaperjam)
                                        iv_awan = findViewById(R.id.iv_awan)
                                        cuacaperjam.text = time.name.toString()
                                        when (time.name) {
                                            "Berawan", "Kabut" -> iv_awan.setImageResource(R.drawable.cloudy)
                                            "Cerah Berawan" -> iv_awan.setImageResource(R.drawable.cloudy_sunny)
                                            "Hujan Petir", "Hujan Sedang" -> {
                                                iv_awan.setImageResource(R.drawable.rainy)
                                                showNotification()
                                                playNotificationSound()
                                            }
                                            "Sunny" -> iv_awan.setImageResource(R.drawable.sunny)
                                            // Tambahkan kondisi lain jika diperlukan
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // mengambil data humidity dan cuaca hari ini
                    val currentDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
                    if (weatherData != null) {
                        val weatherDetails = weatherData.data
                        weatherDetails?.params
                            ?.filter { it?.id == "humin" }
                            ?.flatMap { it?.times.orEmpty() }
                            ?.find { it?.day == currentDate }
                            ?.let { time ->
                                val kelembapan = time.value
                                tv_kelembapanhariini = findViewById(R.id.tv_kelembapan)
                                tv_kelembapanhariini.text = kelembapan.toString()
                            }
                    }

                    // mengambil data suhu sesuai jam saat ini :
                    if (weatherData != null) {
                        val weatherDetails = weatherData.data
                        weatherDetails?.params
                            ?.filter { it?.id == "tmin"}
                            ?.flatMap { it?.times.orEmpty() }
                            ?.find { it?.day == currentDate }
                            ?.let { time ->
                                val suhu = time.celcius
                                tv_suhuhariini = findViewById(R.id.tv_suhuhariini)
                                tv_suhuhariini.text = suhu.toString()
                            }
                    }
                } else {
                    // Handle response error
                }

//                        if (params != null) {
//                            for (param in params) {
//                                // Akses data cuaca sesuai kebutuhan
//                                if (param != null) {
//                                    if (param.id == "tmin"){
//                                        val times = param?.times
//                                        if (times != null) {
//                                            for (time in times) {
//                                                if (time?.day == currentDate){
//                                                    // Akses data cuaca per jam sesuai kebutuhan
//                                                    val suhu = time?.celcius
//                                                    Log.d("Suhu", suhu.toString())
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }

            }

            override fun onFailure(call: Call<ResponseCuaca>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

//    private fun fetchDataFromFirebase() {
//        // Referensi ke node tertentu di dalam database
//        val yourNodeRef = databaseReference.child("cuaca")
//
//        // Tambahkan listener untuk mendengarkan perubahan pada data
//        databaseReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val cuacadata = dataSnapshot.child("cuaca").getValue(String::class.java)
//                val suhudata = dataSnapshot.child("suhu").getValue(Int::class.java)
//
//                // Gunakan nilai sesuai kebutuhan Anda
//                if (cuacadata != null) {
//                    // Lakukan sesuatu dengan nilai
//                    // Contoh: tampilkan di logcat
//                    Log.d("value : ",cuacadata);
////                    cuaca.text = cuacadata
//                    if (cuacadata == "Hujan" || cuacadata == "hujan"){
//                        showNotification()
//                        playNotificationSound()
////                        backgroundImageView.setImageResource(R.drawable.bg_hujan)
//                    } else if (cuacadata == "Cerah" || cuacadata == "cerah"){
////                        backgroundImageView.setImageResource(R.drawable.bg_cerah)
//                    }
//                }
//
//                if (suhudata != null) {
//                    // Lakukan sesuatu dengan nilai
//                    // Contoh: tampilkan di logcat
//                    Log.d("value : ",suhudata.toString());
////                    suhu.text = suhudata.toString()
//                }
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error
//                println("Firebase Error: ${databaseError.message}")
//            }
//        })
//
//    }

    private fun showNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Hujan Turun!")
            .setContentText("Segera angkat jemuran anda bila ada.")
            .setSmallIcon(R.drawable.cloudy_sunny)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(1, notificationBuilder.build())
    }

    private fun playNotificationSound() {
        try {
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val soundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.raining)
            val ringtone = RingtoneManager.getRingtone(this, soundUri)
            ringtone.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}