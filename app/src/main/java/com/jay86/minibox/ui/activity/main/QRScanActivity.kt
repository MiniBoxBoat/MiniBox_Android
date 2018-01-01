package com.jay86.minibox.ui.activity.main

import android.Manifest
import android.os.Bundle
import android.view.View
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.jay86.minibox.R
import com.jay86.minibox.config.QR_KEY
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.order.OrderActivity
import com.jay86.minibox.utils.extension.doPermissionAction
import kotlinx.android.synthetic.main.activity_qrscan.*
import kotlinx.android.synthetic.main.toolbar_common.*
import org.jetbrains.anko.longToast

class QRScanActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.qrscan_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscan)
        toolbar.init(View.OnClickListener { finish() })

        qrScanner.setDelegate(object : QRCodeView.Delegate {
            override fun onScanQRCodeSuccess(result: String?) {
                qrScanner.startSpot()

                if (result != null && result.startsWith(QR_KEY)) {
                    OrderActivity.activityStart(this@QRScanActivity, result.split("|")[1])
                    finish()
                }
            }

            override fun onScanQRCodeOpenCameraError() {
                longToast(resources.getString(R.string.common_hint_open_camera_error))
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        doPermissionAction(Manifest.permission.CAMERA, getString(R.string.common_hint_request_camera),
                action = { startScan() },
                doOnRefuse = {
                    longToast(resources.getString(R.string.common_hint_open_camera_error))
                    finish()
                }
        )
    }

    override fun onStop() {
        super.onStop()
        qrScanner.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        qrScanner.onDestroy()
    }

    private fun startScan() {
        qrScanner.startCamera()
        qrScanner.startSpot()
        qrScanner.showScanRect()
    }
}
