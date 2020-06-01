package com.aboust.develop_guide.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aboust.develop_guide.R
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.applyLinks
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.iconics.view.IconicsImageView
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber


class LoginFragment : Fragment() {


    private val oauthSource = dataSourceOf(
            OauthSourceItem(1, "微信", "wechat"),
            OauthSourceItem(2, "支付宝", "zhifubao1")
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.login)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)

        val linked = Link(getString(R.string.register))
                .setTextColor(R.color.colorNormal)
                .setTextColorOfHighlightedLink(Color.parseColor("#0D3D0C"))
                .setHighlightAlpha(.4f)
                .setUnderlined(true)
                .setBold(true)
                .setOnClickListener(object : Link.OnClickListener {
                    override fun onClick(clickedText: String) {
                        Timber.d("---  linked --- %s ", clickedText)
                    }
                })

        val subTitleView = view.findViewById<TextView>(R.id.sub_title)
        subTitleView.applyLinks(linked)

        val emptyView = layoutInflater.inflate(R.layout.layout_empty, null)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        from_oauth.setup {
            withLayoutManager(linearLayoutManager)
            withEmptyView(emptyView)
            withDataSource(oauthSource)
            withItem<OauthSourceItem, OauthSourceViewHolder>(R.layout.item_oauth) {
                onBind(::OauthSourceViewHolder) { index, item ->
                    Timber.d("index => %s", index)
                    title.text = item.name
                    icon.icon = IconicsDrawable(requireContext(), item.icon).apply {
                        colorInt = Color.RED
                        sizeDp = 75
                    }
                }
            }
        }
    }
}

class OauthSourceViewHolder(itemView: View) : ViewHolder(itemView) {
    val icon: IconicsImageView = itemView.findViewById(R.id.icon)
    val title: AppCompatTextView = itemView.findViewById(R.id.title)

}

data class OauthSourceItem(val id: Int, val name: String, val icon: String)