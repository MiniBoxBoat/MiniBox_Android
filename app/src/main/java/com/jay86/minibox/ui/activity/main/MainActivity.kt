package com.jay86.minibox.ui.activity.main

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.user.LoginActivity
import com.jay86.minibox.utils.extension.setImageUrl
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.init(View.OnClickListener { drawerLayout.openDrawer(Gravity.START) })
        mapView.onCreate(savedInstanceState)
        initNavigationView()
    }

    private fun initNavigationView() {
        val headerView = navigationView.inflateHeaderView(R.layout.nav_header_main)
        val nicknameView = headerView.find<TextView>(R.id.nickname)
        nicknameView.text = App.user?.nickname ?: getString(R.string.main_hint_unlogin)
        //todo 默认头像
        val avatarView = headerView.find<ImageView>(R.id.avatar)
        avatarView.setImageUrl(App.user?.avatar)

        fun onClick(actionIfLogin: (() -> Unit)?) {
            if (App.isLogin) {
                actionIfLogin!!.invoke()
            } else {
                activityStart<LoginActivity>(false)
            }
        }

        nicknameView.setOnClickListener {
            onClick {
                //todo 跳转到个人资料
            }
        }
        avatarView.setOnClickListener {
            onClick {
                //todo 换头像
            }
        }

        navigationView.setNavigationItemSelectedListener {
            //todo 列表点击事件
            when (it.itemId) {
                R.id.my_box -> {
                }
                R.id.user_detail -> {
                }
                R.id.discount -> {
                }
                R.id.call_service -> {
                }
                R.id.settings -> {
                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.qr_scan -> {
            activityStart<QRScanActivity>(false)
            true
        }
        else -> false
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        if (App.isLogin) {

        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
