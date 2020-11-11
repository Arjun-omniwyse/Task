package com.pro.app.ui.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mindorks.nybus.NYBus
import com.pro.app.R
import com.pro.app.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.layout_loader.*

abstract class BaseActivity : AppCompatActivity(),
    BaseMvpView {

    lateinit var mainViewModel: MainViewModel

    protected abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        mainViewModel = MainViewModel(application)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        }

        initialization()
        registerListeners()
    }

    protected abstract fun initialization()

    open fun registerListeners() {}

    override fun showLoading() {
        rlLoader.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rlLoader.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        NYBus.get().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        NYBus.get().register(this)
    }

}
