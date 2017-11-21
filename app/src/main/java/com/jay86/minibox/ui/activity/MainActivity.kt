package com.jay86.minibox.ui.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.utils.setImageUrl
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity() {
    companion object {
        fun activityStart(context: Context, finishBefore: Boolean = true) {
            context.startActivity<MainActivity>()
            if (finishBefore && context is Activity) context.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.init { drawerLayout.openDrawer(Gravity.START) }
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
                LoginActivity.activityStart(this, false)
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
            QRScanActivity.activityStart(this, false)
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
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
