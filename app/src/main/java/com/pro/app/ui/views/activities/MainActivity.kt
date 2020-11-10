package com.pro.app.ui.views.activities

import android.os.Build
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.pro.app.R
import com.pro.app.data.Status
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.OnClick
import com.pro.app.extensions.showLog
import com.pro.app.extensions.showMessage
import com.pro.app.ui.adapters.UsersListAdapter
import com.pro.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    lateinit var list: ArrayList<ModelUser>
    lateinit var adapter: UsersListAdapter

    var toggle = "list"

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initialization() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        list = ArrayList()

        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.setHasFixedSize(true)
        adapter = UsersListAdapter(this, list, object : OnClick {

            override fun onUserClicked(modelUser: ModelUser) {

            }
        })

        rvUsers.adapter = adapter

        mainViewModel.usersListLiveData.observe(this, Observer {
            "data posted".showLog()
            when (it?.status) {
                Status.SUCCESS -> {
                    hideLoading()
                    list.clear()
                    list.addAll(it.data!!)
                    adapter.notifyDataSetChanged()

                }
                Status.LOADING -> {
                    showLoading()
                }
                Status.ERROR -> {
                    hideLoading()
                    it.message?.let { showMessage(it) }
                }
            }
        })

        mainViewModel.getUsersList("0", "30")

    }

    override fun registerListeners() {
        imgToggle.setOnClickListener {
            toggle = if (toggle == "list") {
                rvUsers.layoutManager = GridLayoutManager(this, 2)
                imgToggle.setImageResource(R.drawable.ic_list)
                "grid"
            } else {
                rvUsers.layoutManager = LinearLayoutManager(this)
                imgToggle.setImageResource(R.drawable.ic_grid)
                "list"
            }
        }
    }
}