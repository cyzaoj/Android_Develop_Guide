package com.aboust.develop_guide.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.aboust.develop_guide.R
import com.aboust.develop_guide.widget.toast
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.common_toolbar.*
import java.util.*

data class Item(val id: Long, val icon: String?, val name: String, val clazz: Class<*>)


class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.item_text)
}

class MainActivity : BaseActivity() {

    private val dataSource = dataSourceTypedOf(
        Item(1, null, "Notification", NotificationActivity::class.java)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        views()
    }

    private fun views() {
        toolbar_left.visibility = View.INVISIBLE

        rv_apis.setup {
            withDataSource(dataSource)
            val self = this@MainActivity
            withItem<Item, ItemViewHolder>(R.layout.item_field_single_line_icon) {
                onBind(::ItemViewHolder) { _, item -> name.text = item.name }
                onClick { index ->
                    "Clicked $index: ${item.name}".toast(self)
                    val i = Intent(self, item.clazz)
                    ContextCompat.startActivity(self, i, null)
                }
                onLongClick { index -> "Long clicked $index: ${item.name}".toast(self) }
            }
        }


    }
}
