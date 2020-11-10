package com.pro.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pro.app.R
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.OnClick
import java.util.*

class UsersListAdapter(
    private var context: Context,
    private var list: ArrayList<ModelUser>,
    private var onClick: OnClick
) : RecyclerView.Adapter<UsersListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return MyViewHolder(itemView)
    }

    private fun bindViews(holder: MyViewHolder, position: Int) {
        val data = list[position]

        holder.txtName.text = data.login
        Glide.with(context)
            .load(data.avatar_url).dontTransform()
            .placeholder(R.drawable.bg_placeholder)
            .into(holder.imgUser)

        holder.rlMain.setOnClickListener {
            onClick.onUserClicked(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        bindViews(holder, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rlMain = itemView.findViewById<View>(R.id.rlMain) as LinearLayout
        val imgUser = itemView.findViewById<View>(R.id.imgUser) as ImageView
        val txtName = itemView.findViewById<View>(R.id.txtName) as TextView
    }

}