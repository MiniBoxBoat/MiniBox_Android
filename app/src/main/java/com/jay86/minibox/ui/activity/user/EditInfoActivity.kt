package com.jay86.minibox.ui.activity.user

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.utils.extension.*
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_edit_info.*
import kotlinx.android.synthetic.main.toolbar_common.*
import org.jetbrains.anko.longToast

class EditInfoActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.edit_info)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        toolbar.init(View.OnClickListener { finish() })

        val user = App.user!!
        avatarView.setImageUrl(user.avatar, resources.getDrawable(R.drawable.default_avatar))
        nicknameView.setText(user.nickname)
        phoneView.setText(user.phoneNumber)
        genderView.prompt = user.phoneNumber
        emailView.setText(user.email)
        creditView.text = "${user.credibility ?: 0}"
        useTimeView.text = "${user.useTime ?: 0}"
    }

    override fun onResume() {
        super.onResume()
        if (!nicknameView.isFocused) nicknameView.requestFocus()
    }

    private fun performSubmit() {
        if (!check()) return

        val user = App.user!!
        RequestManager.updateUserInfo(nicknameView.text.toString(), phoneView.text.toString(),
                emailView.text.toString(), genderView.selectedItem.toString(), user.token, user.trueName, object : ProgressObserver<User>(this) {
            override fun onNext(_object: User) {
                super.onNext(_object)
                App.user = _object
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                longToast(e.message ?: getString(R.string.common_hint_network_error))
            }
        })
    }

    private fun check() = when {
        nicknameView.length() !in User.NICKNAME_LENGTH -> {
            val format = getString(R.string.common_hint_error_nickname_length)
            nicknameView.snackbar(String.format(format, User.NICKNAME_LENGTH.first, User.NICKNAME_LENGTH.last))
            false
        }

        nicknameView.text.containsEmpty() -> {
            nicknameView.snackbar(getString(R.string.common_hint_error_empty_char))
            false
        }

        nicknameView.text[0] in '0'..'9' -> {
            nicknameView.snackbar(getString(R.string.common_hint_error_number_start))
            false
        }

        !phoneView.text.isPhoneNumber() -> {
            phoneView.snackbar(getString(R.string.common_hint_error_phone_number))
            false
        }

        emailView.text.toString().isEmail() -> {
            emailView.snackbar(getString(R.string.common_hint_error_email))
            false
        }

        else -> true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_order_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sure) {
            nicknameView.hideKeyBoard()
            performSubmit()
        }
        return super.onOptionsItemSelected(item)
    }
}
