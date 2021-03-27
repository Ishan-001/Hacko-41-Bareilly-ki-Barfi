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
import kotlinx.android.synthetic.main.layout_key.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class KeyFragment() : SupportBlurDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root=inflater.inflate(R.layout.layout_key, container, false)

        sharedPreferences = context!!.getSharedPreferences("My Preferences", Context.MODE_PRIVATE)
        val hash = sharedPreferences.getString("Hash", null)

        root.key.text = hash

        return root
    }

}