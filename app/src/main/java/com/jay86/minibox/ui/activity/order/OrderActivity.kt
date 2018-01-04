package com.jay86.minibox.ui.activity.order

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.main.QRScanActivity
import com.jay86.minibox.ui.fragment.AppointFragment
import com.jay86.minibox.ui.fragment.ImmediatelyFragment
import com.jay86.minibox.utils.LocationUtils
import com.jay86.minibox.utils.extension.moveCamera
import com.jay86.minibox.utils.extension.replaceFragment
import com.jay86.minibox.utils.extension.showMarker
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_order.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

class OrderActivity : BaseActivity(), View.OnClickListener {
    companion object {
        private const val MAX_DISTANCE = 100

        fun activityStart(activity: Activity, groupId: String) {
            activity.startActivity<OrderActivity>("groupId" to groupId)
        }

        fun activityStart(activity: Activity, boxGroup: BoxGroup) {
            activity.startActivity<OrderActivity>("boxGroup" to boxGroup)
        }
    }

    override val title: String = ""

    private var groupId: String? = null
    private var boxGroup: BoxGroup? = null
    private var isAppointOrder: Boolean = true

    private val appointFragment by lazy { AppointFragment() }
    private val immediatelyFragment by lazy { ImmediatelyFragment() }

    private lateinit var openTimeName: Array<String>
    private lateinit var useTimeName: Array<String>

    private val openTimeApi = arrayOf(15, 30, 45, 60, 120, 180)
    private val useTimeApi = arrayOf(30, 60, 120, 180)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        openTimeName = resources.getStringArray(R.array.order_open_time)
        useTimeName = resources.getStringArray(R.array.order_use_time)
        boxGroup = intent.getParcelableExtra("boxGroup")
        toolbar.init(View.OnClickListener { finish() })

        if (boxGroup == null) {
            groupId = intent.getStringExtra("groupId")
            RequestManager.showBoxGroup(groupId!!, object : ProgressObserver<BoxGroup>(this) {
                override fun onNext(_object: BoxGroup) {
                    super.onNext(_object)
                    boxGroup = _object
                    initView()
                    initMap(savedInstanceState)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    longToast("数据异常，请重试")
                    activityStart<QRScanActivity>()
                }
            })
        } else {
            initView()
            initMap(savedInstanceState)
        }
    }

    private fun initView() {
        if (allowImmediately()) {
            loadImmediately()
        } else {
            loadAppoint()
        }
        immediatelyButton.setOnClickListener(this)
        appointButton.setOnClickListener(this)
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        val amap = mapView.map
        amap.apply {
            moveCamera(boxGroup!!.lat, boxGroup!!.lng, 16f)
            showMarker(boxGroup!!)
        }
        amap.uiSettings.apply {
            isZoomControlsEnabled = false
            isMyLocationButtonEnabled = false
            setAllGesturesEnabled(false)
        }
    }

    private fun loadAppoint() {
        isAppointOrder = true
        immediatelyButton.setTextColor(resources.getColor(R.color.colorWhite))
        appointButton.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        replaceFragment(appointFragment, "boxGroup" to boxGroup!!)
    }

    private fun loadImmediately() {
        if (!allowImmediately()) {
            longToast("即时订单需要您距离箱子位置不超过 $MAX_DISTANCE 米")
            return
        }
        isAppointOrder = false
        immediatelyButton.setTextColor(resources.getColor(R.color.colorPrimaryDark))
        appointButton.setTextColor(resources.getColor(R.color.colorWhite))
        replaceFragment(immediatelyFragment, "boxGroup" to boxGroup!!)
    }

    private fun allowImmediately(): Boolean {
        val curLatLng = LocationUtils.getCurLatLng() ?: return false
        val boxLatLng = LatLng(boxGroup!!.lat, boxGroup!!.lng)
        val distance = AMapUtils.calculateLineDistance(curLatLng, boxLatLng)
        return distance <= MAX_DISTANCE
    }

    private fun performAppoint() {
        val params = appointFragment.getOrderInfo()

        val useDate = GregorianCalendar()
        useDate.add(Calendar.MINUTE, useTimeApi[useTimeName.indexOf(params["useTime"])])
        val useTime = "${useDate.get(Calendar.YEAR)}-${useDate.get(Calendar.MONTH) + 1}-" +
                "${useDate.get(Calendar.DAY_OF_MONTH)} ${useDate.get(Calendar.HOUR)}:" +
                "${useDate.get(Calendar.MINUTE)}:${useDate.get(Calendar.SECOND)}"

        val openDate = GregorianCalendar()
        openDate.add(Calendar.MINUTE, openTimeApi[openTimeName.indexOf(params["openTime"])])
        val openTime = "${openDate.get(Calendar.YEAR)}-${openDate.get(Calendar.MONTH) + 1}-" +
                "${openDate.get(Calendar.DAY_OF_MONTH)} ${openDate.get(Calendar.HOUR)}:" +
                "${openDate.get(Calendar.MINUTE)}:${openDate.get(Calendar.SECOND)}"

        RequestManager.appoint(App.user!!.id, App.user!!.nickname, App.user!!.phoneNumber,
                boxGroup!!.groupId, params["boxSize"]!!, openTime, useTime, object : BaseObserver<String>() {
            override fun onNext(_object: String) {
                super.onNext(_object)
                toast("预约成功")
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                toast(e.message ?: "网络异常")
            }
        })
    }

    private fun performImmediately() {
        if (!allowImmediately()) {
            longToast("即时订单需要您距离箱子位置不超过 $MAX_DISTANCE 米")
            return
        }

        val params = immediatelyFragment.getOrderInfo()
        RequestManager.order(App.user!!.id, boxGroup!!.groupId, params["boxSize"]!!, App.user!!.token, object : BaseObserver<String>() {
            override fun onNext(_object: String) {
                super.onNext(_object)
                toast("下单成功，您的箱子号码：$_object")
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                toast(e.message ?: "网络异常")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_order_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == R.id.sure) {
            if (isAppointOrder) performAppoint() else performImmediately()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) = when (v!!.id) {
        R.id.appointButton -> loadAppoint()
        R.id.immediatelyButton -> loadImmediately()
        else -> Unit
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
