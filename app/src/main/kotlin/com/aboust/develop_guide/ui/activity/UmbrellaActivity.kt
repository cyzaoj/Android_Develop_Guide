package com.aboust.develop_guide.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.aboust.develop_guide.R
import com.aboust.develop_guide.widget.toast
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import kotlinx.android.synthetic.main.activity_umbrella.*

class UmbrellaActivity : BaseActivity() {
    private val dataSource = dataSourceTypedOf(
            Item(1, "二维码", CodeScanActivity::class.java, R.mipmap.ic_launcher)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_umbrella)

        rv_list.setup {
            val self = this@UmbrellaActivity
            withDataSource(dataSource)
            withLayoutManager(GridLayoutManager(baseContext, 4))
            withEmptyView(View.inflate(baseContext, R.layout.layout_empty, null))

            withItem<Item, ItemViewHolder>(R.layout.item_rv_umbrella) {
                onBind(::ItemViewHolder) { _, item ->
                    name.text = item.name
                    icon.setImageResource(item.resId)
                }
                onClick { index ->
                    "Clicked $index: ${item.name}".toast(self)
                    val i = Intent(self, item.clazz)
                    ContextCompat.startActivity(self, i, null)
                }
                onLongClick { index -> "Long clicked $index: ${item.name}".toast(self) }
            }

        }
    }

    data class Item(val id: Number, val name: String, val clazz: Class<*>, @DrawableRes val resId: Int)

    class ItemViewHolder(itemView: View) : ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.logo)
        val name: TextView = itemView.findViewById(R.id.title)
    }
}

