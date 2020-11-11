package com.pro.app.ui.views.activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pro.app.R
import com.pro.app.data.Status
import com.pro.app.data.models.ModelUser
import com.pro.app.data.models.ModelUserData
import com.pro.app.data.models.OnClick
import com.pro.app.extensions.roundTo
import com.pro.app.extensions.showLog
import com.pro.app.extensions.showMessage
import com.pro.app.ui.adapters.UsersListAdapter
import com.pro.app.ui.adapters.UsersLoadStateAdapter
import com.pro.app.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    lateinit var adapter: UsersListAdapter

    var toggle = "list"

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initialization() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }

        rvUsers.layoutManager = LinearLayoutManager(this)
        rvUsers.setHasFixedSize(true)
        adapter = UsersListAdapter(this, object : OnClick {

            override fun onUserClicked(modelUser: ModelUser) {
                mainViewModel.getUserData(modelUser.login)
            }
        })

        rvUsers.adapter = adapter.withLoadStateHeaderAndFooter(
            header = UsersLoadStateAdapter { adapter.retry() },
            footer = UsersLoadStateAdapter { adapter.retry() }
        )

        lifecycleScope.launch {
            mainViewModel.users.collectLatest { pagedData ->
                adapter.submitData(pagedData)
            }
        }

        mainViewModel.userDataLiveData.observe(this, Observer {
            "data posted".showLog()
            when (it?.status) {
                Status.SUCCESS -> {
                    hideLoading()
                    userDetailsBottomSheet(it.data)
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

    private fun userDetailsBottomSheet(modelUserData: ModelUserData?) {
        val view: View = layoutInflater.inflate(R.layout.bottomsheet_userdetails, null)
        var dialogPaymentOptions = BottomSheetDialog(this)
        dialogPaymentOptions?.setContentView(view)

        val imgUser = view.findViewById<ImageView>(R.id.imgUser)
        val txtOriginalName = view.findViewById<TextView>(R.id.txtOriginalName)
        val txtNickName = view.findViewById<TextView>(R.id.txtNickName)
        val txtFollowers = view.findViewById<TextView>(R.id.txtFollowers)
        val txtFollowing = view.findViewById<TextView>(R.id.txtFollowing)
        val txtRepos = view.findViewById<TextView>(R.id.txtRepos)
        val txtVisitProfile = view.findViewById<TextView>(R.id.txtVisitProfile)

        txtVisitProfile.paintFlags = txtVisitProfile.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        txtVisitProfile.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(modelUserData?.html_url)))
        }

        txtNickName.text = modelUserData?.login
        txtOriginalName.text = modelUserData?.name
        txtFollowers.text =
            if (modelUserData?.followers!!.toFloat() > 1000) "${(modelUserData?.followers!!.toFloat() / 1000).roundTo(
                1
            )}k" else modelUserData?.followers
        txtFollowing.text =
            if (modelUserData?.following!!.toFloat() > 1000) "${(modelUserData?.following!!.toFloat() / 1000).roundTo(
                1
            )}k" else modelUserData?.following
        txtRepos.text = modelUserData?.public_repos

        Glide.with(this)
            .load(modelUserData?.avatar_url).dontTransform()
            .placeholder(R.drawable.bg_placeholder)
            .into(imgUser)

        dialogPaymentOptions?.show()
    }
}