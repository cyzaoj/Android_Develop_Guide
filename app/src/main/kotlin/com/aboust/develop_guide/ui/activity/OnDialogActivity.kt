package com.aboust.develop_guide.ui.activity

import android.os.Bundle
import com.aboust.develop_guide.R
import com.aboust.develop_guide.widget.OnDialog
import kotlinx.android.synthetic.main.activity_on_dialog.*

class OnDialogActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_on_dialog)

        val onDialog = OnDialog(this)

        dialog_default.setOnClickListener {

            onDialog
                    .layout(R.layout.dialog_confirm).with()
                    .cancel(R.id.tv_cancel)
                    .show()

        }
    }
}