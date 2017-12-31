package com.jay86.minibox.ui.activity.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.jay86.minibox.ui.activity.user.UserDetailActivity
import com.jay86.minibox.utils.MapHelper
import com.jay86.minibox.utils.extension.doPermissionAction
import com.jay86.minibox.utils.extension.reset
import com.jay86.minibox.utils.extension.uri
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.io.File


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        val OPEN_ALBUM = 0x1
        val OPEN_CAMERA = 0x2
    }

    private lateinit var nicknameView: TextView
    private lateinit var avatarView: ImageView
    private lateinit var mapHelper: MapHelper
    private lateinit var cameraCacheFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doPermissionAction(Manifest.permission.ACCESS_COARSE_LOCATION, getString(R.string.common_hint_request_location),
                doOnRefuse = { finish() }
        )
        toolbar.init(View.OnClickListener { drawerLayout.openDrawer(Gravity.START) })
        mapView.onCreate(savedInstanceState)
        initMap()
        initNavigationView()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        nicknameView.text = App.user?.nickname ?: getString(R.string.main_hint_unlogin)
//        avatarView.setImageUrl(App.user?.avatar, resources.getDrawable(R.drawable.default_avatar))
        avatarView.visibility = if (App.isLogin) View.VISIBLE else View.INVISIBLE
    }

    private fun initMap() {
        mapHelper = MapHelper(mapView.map)
        mapHelper.init()
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
                showSelectDialog()
            }
        }

        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun showSelectDialog() {
        val entries = listOf("拍照", "从相册选择")
        selector(items = entries) { _, which ->
            when (which) {
                0 -> getImageFromCamera()
                1 -> getImageFromAlbum()
            }
        }
    }

    private fun getImageFromAlbum() {
        doPermissionAction(Manifest.permission.WRITE_EXTERNAL_STORAGE, getString(R.string.common_hint_request_storage),
                action = {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
                    startActivityForResult(intent, OPEN_ALBUM)
                },
                doOnRefuse = { toast(getString(R.string.common_error_open_album_fail)) }
        )
    }

    private fun getImageFromCamera() {
        doPermissionAction(Manifest.permission.CAMERA, getString(R.string.common_hint_request_camera),
                action = {
                    cameraCacheFile = File("${App.fileHome}/pic", "${System.currentTimeMillis()}.png")
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraCacheFile.uri)
                    startActivityForResult(intent, OPEN_CAMERA)
                },
                doOnRefuse = { toast(getString(R.string.common_hint_open_camera_error)) }
        )
    }

    private fun checkLoginBeforeAction(actionIfLogin: (() -> Unit)) {
        if (App.isLogin) {
            actionIfLogin.invoke()
        } else {
            activityStart<LoginActivity>(false)
        }
    }

    private fun callService() {
        val phone = "15923565234"
        alert(message = getString(R.string.main_hint_call_service)) {
            yesButton {
                doPermissionAction(Manifest.permission.CALL_PHONE, getString(R.string.common_hint_request_call),
                        action = { makeCall(phone) },
                        doOnRefuse = { longToast(getString(R.string.common_hint_error_make_call_fail) + phone) }
                )
            }
            noButton { }
        }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                OPEN_CAMERA, OPEN_ALBUM -> {
                    val uri = if (requestCode == OPEN_CAMERA) cameraCacheFile.uri else data!!.data
                    val destinationUri = Uri.fromFile(File("${App.fileHome}/pic", "${App.user!!.id}.png").reset())
                    UCrop.of(uri, destinationUri)
                            .withAspectRatio(1f, 1f)
                            .withMaxResultSize(800, 800)
                            .start(this)
                }
                UCrop.REQUEST_CROP -> {
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(UCrop.getOutput(data!!)))
                    updateAvatar(bitmap)
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            longToast(UCrop.getError(data!!)?.message ?: getString(R.string.common_error_crop_image_fail))
        }
    }

    private fun updateAvatar(bitmap: Bitmap) {
        //todo updateAvatar
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

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.qr_scan -> {
            activityStart<QRScanActivity>(false)
            true
        }

        R.id.search -> {
            activityStart<SearchActivity>(false)
            toolbar.visibility = View.INVISIBLE
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
