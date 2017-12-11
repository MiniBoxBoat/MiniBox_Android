package com.jay86.minibox.ui.activity.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.MyLocationStyle
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.login.LoginActivity
import com.jay86.minibox.ui.activity.user.UserDetailActivity
import com.jay86.minibox.utils.extension.setImageUrl
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var nicknameView: TextView
    private lateinit var avatarView: ImageView
    private lateinit var aMap: AMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.init(View.OnClickListener { drawerLayout.openDrawer(Gravity.START) })
        mapView.onCreate(savedInstanceState)
        initNavigationView()
        initMap()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        nicknameView.text = App.user?.nickname ?: getString(R.string.main_hint_unlogin)
        avatarView.setImageUrl(App.user?.avatar, resources.getDrawable(R.drawable.default_avatar))
        avatarView.visibility = if (App.isLogin) View.VISIBLE else View.INVISIBLE
    }

    private fun initMap() {
        aMap = mapView.map
    }

    private fun startLocation() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)
        myLocationStyle.interval(2000)
        aMap.setMyLocationStyle(myLocationStyle)
        aMap.isMyLocationEnabled = true
    }

    private fun initNavigationView() {
        val headerView = navigationView.inflateHeaderView(R.layout.nav_header_main)
        nicknameView = headerView.find(R.id.nicknameView)
        avatarView = headerView.find(R.id.avatar)

        nicknameView.setOnClickListener {
            checkLoginBeforeAction { activityStart<UserDetailActivity>(false) }
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
            R.id.user_detail -> checkLoginBeforeAction { activityStart<UserDetailActivity>(false) }
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
