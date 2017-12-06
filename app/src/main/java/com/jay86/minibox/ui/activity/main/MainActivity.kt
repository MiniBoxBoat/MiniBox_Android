package com.jay86.minibox.ui.activity.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.login.LoginActivity
import com.jay86.minibox.utils.extension.setImageUrl
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var nicknameView: TextView
    lateinit var avatarView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.init(View.OnClickListener { drawerLayout.openDrawer(Gravity.START) })
        mapView.onCreate(savedInstanceState)
        initNavigationView()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        nicknameView.text = App.user?.nickname ?: getString(R.string.main_hint_unlogin)
        //todo 默认头像
        avatarView.setImageUrl(App.user?.avatar)
    }

    private fun initNavigationView() {
        val headerView = navigationView.inflateHeaderView(R.layout.nav_header_main)
        nicknameView = headerView.find(R.id.nicknameView)
        avatarView = headerView.find(R.id.avatar)

        nicknameView.setOnClickListener {
            checkLoginBeforeAction {
                //todo 跳转到个人资料
            }
        }

        avatarView.setOnClickListener {
            checkLoginBeforeAction {
                //todo 换头像
            }
        }

        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun checkLoginBeforeAction(actionIfLogin: (() -> Unit)) {
        if (App.isLogin) {
            actionIfLogin.invoke()
        } else {
            activityStart<LoginActivity>(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //todo 列表点击事件
        when (item.itemId) {
            R.id.my_box -> checkLoginBeforeAction { }
            R.id.user_detail -> checkLoginBeforeAction { }
            R.id.discount -> checkLoginBeforeAction { }
            R.id.call_service -> {
            }
            R.id.settings -> {
            }
        }
        drawerLayout.closeDrawers()
        return true
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

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
