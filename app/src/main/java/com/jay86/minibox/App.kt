package com.jay86.minibox

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Environment
import com.jay86.minibox.bean.User
import com.jay86.minibox.config.SP_USER_KEY
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.utils.extension.getPreference
import java.io.File

/**
 * Created by Jay68 on 2017/11/20.
 */
class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        private val activityContainer = HashMap<String, Activity>()

        var user: User? = null
        val isLogin get() = user != null
        val fileHome = "${Environment.getExternalStorageDirectory()}/minibox"

        fun addActivity(activity: Activity) {
            activityContainer += activity.javaClass.name to activity
        }

        fun finishActivity(name: String) {
            val activity = activityContainer.remove(name)
            activity?.finish()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initFile()
        login()
    }

    private fun initFile() {
        val homeFile = File(fileHome)
        val picFile = File(homeFile, "pic")
        if (!homeFile.exists()) homeFile.mkdir()
        if (!picFile.exists()) picFile.mkdir()
    }

    private fun login() {
        val user = User.fromJson(context.getPreference(SP_USER_KEY, "")!!)
        if (user != null) {
            App.user = user
            RequestManager.login(user.phoneNumber, user.password, object : BaseObserver<User>() {
                override fun onNext(_object: User) {
                    super.onNext(_object)
                    App.user = user
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    //todo 错误处理：密码错误、网络问题
                }
            })
        }
    }
}