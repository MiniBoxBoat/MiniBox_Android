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
import com.jay86.minibox.utils.extension.setPreference
import org.jetbrains.anko.toast
import java.io.File

/**
 * Created by Jay
 */
class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        private val activityContainer = HashMap<String, Activity>()

        var user: User? = null
        val isLogin get() = user != null
        var onLoginStateChangeListener: ((isLogin: Boolean) -> Unit)? = null

        val fileHome = "${Environment.getExternalStorageDirectory()}/minibox"

        fun logout() {
            user = null
            onLoginStateChangeListener?.invoke(false)
            context.setPreference(SP_USER_KEY, "")
        }

        fun addActivity(activity: Activity) {
            activityContainer += activity.javaClass.name to activity
        }

        fun <T : Activity> getActivity(key: Class<T>) =
                if (activityContainer.containsKey(key.name)) activityContainer[key.name] as T else null

        fun finishActivity(key: Class<Activity>) {
            val activity = removeActivity(key)
            activity?.finish()
        }

        fun removeActivity(key: Class<Activity>) = activityContainer.remove(key.name)
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
            RequestManager.login(user.phoneNumber, user.password, object : BaseObserver<User>() {
                override fun onNext(_object: User) {
                    super.onNext(_object)
                    App.user = _object
                    onLoginStateChangeListener?.invoke(true)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    toast("登陆过期，请重新登陆")
                    App.user = null
                    onLoginStateChangeListener?.invoke(false)
                }
            })
        }
    }
}