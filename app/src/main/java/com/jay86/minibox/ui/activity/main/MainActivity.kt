package com.jay86.minibox.ui.activity.main

import android.Manifest
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
import com.jay86.minibox.utils.extension.doPermissionAction
import com.jay86.minibox.utils.extension.setImageUrl
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var nicknameView: TextView
    private lateinit var avatarView: ImageView
    private lateinit var aMap: AMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //todo 说明请求定位原因的对话框

        doPermissionAction(Manifest.permission.ACCESS_COARSE_LOCATION, doOnRefuse = { finish() })
        toolbar.init(View.OnClickListener { drawerLayout.openDrawer(Gravity.START) })
        mapView.onCreate(savedInstanceState)
        initNavigationView()
        initMap()
        //todo 定位及箱子地点显示
        startLocation()
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
        aMap.uiSettings.isMyLocationButtonEnabled = true
    }

    private fun startLocation() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        myLocationStyle.interval(2000)
        aMap.setMyLocationStyle(myLocationStyle)
        aMap.isMyLocationEnabled = true
    }

    private fun initNavigationView() {
        val headerView = navigationView.inflateHeaderView(R.layout.nav_header_main)
        nicknameView = headerView.find(R.id.nicknameView)
        avatarView = headerView.find(R.id.avatarView)

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
        when (item.itemId) {
            R.id.my_box -> checkLoginBeforeAction { activityStart<MyBoxActivity>(false) }
            R.id.user_detail -> checkLoginBeforeAction { activityStart<UserDetailActivity>(false) }
            R.id.discount -> checkLoginBeforeAction { activityStart<DiscountActivity>(false) }
            R.id.call_service -> callService()
            R.id.settings -> activityStart<SettingActivity>(false)
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun callService() {
        val phone = "15923565234"
        alert(message = getString(R.string.main_hint_call_service)) {
            yesButton {
                doPermissionAction(Manifest.permission.CALL_PHONE, { makeCall(phone) }) {
                    longToast(getString(R.string.common_hint_error_make_call_fail) + phone)
                }
            }
            noButton { }
        }.show()
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
