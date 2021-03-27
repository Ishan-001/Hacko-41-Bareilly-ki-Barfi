package com.ethereum.securex.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.ethereum.securex.MainActivity
import com.ethereum.securex.R
import com.ethereum.securex.api.Api
import com.ethereum.securex.models.Block
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment
import kotlinx.android.synthetic.main.layout_add.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddFragment(private val text: String?) : SupportBlurDialogFragment() {

    private lateinit var mContext: Context
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.layout_add, container, false)
        root.save.setOnClickListener { dismiss() }

        mContext = context!!
        sharedPreferences = mContext.getSharedPreferences("My Preferences", Context.MODE_PRIVATE)

        root.save.setOnClickListener {
            val app = root.app_name.text
            val user = root.username.text
            val pass = root.password.text

            root.prompt.visibility = View.INVISIBLE

            if(app.isEmpty() || user.isEmpty() || pass.isEmpty())
                root.prompt.visibility = View.VISIBLE
            else{
                root.save.isClickable = false
                root.progress_bar.visibility = View.VISIBLE

                val newSetOfCredentials = "$text*$app*$user*$pass"

                val filePart = MultipartBody.Part.createFormData(
                    "file",
                    "app.txt",
                    RequestBody.create(MediaType.parse("text/plain"), newSetOfCredentials)
                )

                sendDataToEtheruem(filePart)
            }
        }

        return root
    }

    private fun sendDataToEtheruem(filePart: MultipartBody.Part) {
        Api.retrofitService.postCredentials(filePart).enqueue(object : Callback<Block> {
            override fun onResponse(call: Call<Block>, response: Response<Block>) {

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("Hash", response.body()?.Hash)
                editor.apply()

                getDataFromEtheruem(response.body()?.Hash)
            }

            override fun onFailure(call: Call<Block>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getDataFromEtheruem(hash: String?) {
        Api.retrofitService2.getCredentials(hash!!).enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val intent = Intent(context, MainActivity::class.java)
                val data = response.body()
                intent.putExtra("Credentials", data!!.substring(0, data.length-2))
                startActivity(intent)
                activity!!.finish()
            }
        })
    }
}