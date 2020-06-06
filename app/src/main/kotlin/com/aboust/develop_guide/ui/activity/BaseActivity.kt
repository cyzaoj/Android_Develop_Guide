package com.aboust.develop_guide.ui.activity

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        title = ""
        setupToolbar()
    }

    private fun setupToolbar() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!onBackPressedDispatcher.hasEnabledCallbacks()) {
            finish()
        }
    }

}