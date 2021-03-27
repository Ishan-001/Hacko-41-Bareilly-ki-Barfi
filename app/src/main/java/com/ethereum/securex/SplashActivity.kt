package com.ethereum.securex

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ethereum.securex.api.Api
import kotlinx.android.synthetic.main.layout_splash.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = Color.parseColor("#20111111");
        setContentView(R.layout.layout_splash)

        sharedPreferences = getSharedPreferences("My Preferences", Context.MODE_PRIVATE)
        val hash = sharedPreferences.getString("Hash", null)

        if(hash==null)
            Handler().postDelayed({
                startActivity(Intent(baseContext, MainActivity::class.java))
            }, 2000)

        else {
            progress_bar.visibility = View.VISIBLE
            getDataFromEtheruem(hash)
        }

    }

    private fun getDataFromEtheruem(hash: String?) {
        Api.retrofitService2.getCredentials(hash!!).enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {

                val intent = Intent(baseContext, MainActivity::class.java)
                val data = response.body()
                intent.putExtra("Credentials", data!!.substring(0, data.length-2))
                startActivity(intent)
                finish()
            }
        })
    }
}
