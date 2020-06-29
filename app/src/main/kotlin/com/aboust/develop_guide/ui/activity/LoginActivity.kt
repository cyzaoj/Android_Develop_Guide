package com.aboust.develop_guide.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aboust.develop_guide.R
import io.sentry.core.Sentry
import timber.log.Timber


/**
 * 登录权限
 */
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.d("onActivityResult -> requestCode: %s, resultCode: %s data: %s", requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach { fragment ->
            onActivityFragmentResult(fragment, requestCode, resultCode, data)
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     *
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private fun onActivityFragmentResult(fragment: Fragment, requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.onActivityResult(requestCode, resultCode, data)
        val childFragment = fragment.childFragmentManager.fragments
        for (f in childFragment) f?.let {
            onActivityFragmentResult(it, requestCode, resultCode, data)
        }
    }
}

