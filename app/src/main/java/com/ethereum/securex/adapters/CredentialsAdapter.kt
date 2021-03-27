package com.ethereum.securex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ethereum.securex.R
import com.ethereum.securex.models.Credentials

class CredentialsAdapter internal constructor(
    private val context: Context,
    private val credentialsList: ArrayList<Credentials>,

    ): RecyclerView.Adapter<CredentialsAdapter.ViewHolder>(){

    class ViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        var appName = itemView.findViewById<TextView>(R.id.app_name)!!
        var username = itemView.findViewById<TextView>(R.id.username)!!
        var password = itemView.findViewById<TextView>(R.id.password)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CredentialsAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CredentialsAdapter.ViewHolder, position: Int) {
        holder.appName.text = credentialsList[position].appName
        holder.username.text = credentialsList[position].username
        holder.password.text = credentialsList[position].password
    }

    override fun getItemCount(): Int {
        return credentialsList.size
    }
}