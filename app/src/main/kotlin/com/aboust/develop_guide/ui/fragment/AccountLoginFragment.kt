package com.aboust.develop_guide.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aboust.develop_guide.R
import com.aboust.develop_guide.ui.activity.MainActivity
import com.aboust.develop_guide.widget.OnDialog
import com.aboust.develop_guide.widget.RichText
import com.aboust.develop_guide.widget.recyclerview.LinearLayoutOffsetsItemDecoration
import com.aboust.develop_guide.widget.recyclerview.LinearLayoutOffsetsItemDecoration.Companion.LINEAR_OFFSETS_HORIZONTAL
import com.aboust.develop_guide.widget.translateBottomInBottomOut
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.materialdesigniconic.MaterialDesignIconic
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import com.mikepenz.iconics.view.IconicsImageView
import kotlinx.android.synthetic.main.fragment_account_login.*
import timber.log.Timber


class AccountLoginFragment : Fragment() {

    private val oauthSource = dataSourceOf(
            OauthSourceItem(1, "微信", "adg_wechat"),
            OauthSourceItem(2, "支付宝", "adg_zhifubao_default")
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        titleView(view)
        editViews(view)
        submitView(view)

        forget_password.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_from_account_to_captcha)
        }
        oauthView()

    }

    private fun oauthView() {
        val iconSize = resources.getDimension(R.dimen.fragment_account_login_oauth_icon_size).toInt()
        val emptyView = layoutInflater.inflate(R.layout.layout_empty, null)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        val decoration = LinearLayoutOffsetsItemDecoration(LINEAR_OFFSETS_HORIZONTAL)
        decoration.itemOffsets(20)
        from_oauth.addItemDecoration(decoration)
        from_oauth.setup {
            withLayoutManager(linearLayoutManager)
            withEmptyView(emptyView)
            withDataSource(oauthSource)
            withItem<OauthSourceItem, OauthSourceViewHolder>(R.layout.item_oauth) {
                onBind(::OauthSourceViewHolder) { index, item ->
                    Timber.d("index => %s", index)
                    title.text = item.name
                    icon.icon = IconicsDrawable(requireContext(), item.icon).apply {
                        sizeDp = iconSize
                    }
                }
            }
        }
    }

    private fun titleView(view: View) {
        val subTitleView = view.findViewById<TextView>(R.id.sub_title)
        subTitleView.text = RichText(requireActivity(), getString(R.string.fragment_account_login_sub_title))
                .first(getString(R.string.register))
                .size(19).onClick(subTitleView) {
                    Timber.d("---  linked ---")
                }.textColor("#0D3D0C").underline().bold()
    }

    private fun submitView(view: View) {
        val submit = view.findViewById<AppCompatButton>(R.id.login)
        submit.setOnClickListener {
            OnDialog(this.requireContext())
                    .layout(R.layout.dialog_alert)
                    .with()
                    .cancel(R.id.action_1)
                    .text(R.id.title, R.string.error_password)
                    .onClickListener(View.OnClickListener {
                        when (it.id) {
                            R.id.action_2 -> {
                                loading.visibility = View.VISIBLE

                                Handler().postDelayed({
                                    loading.visibility = View.GONE
                                    val c = requireContext()
                                    val i = Intent(c, MainActivity::class.java)
                                    ContextCompat.startActivity(requireContext(), i, translateBottomInBottomOut(c).toBundle())
                                    activity?.finish()
                                }, 3000)
                            }
                        }
                    }, R.id.action_2)
                    .gravity(Gravity.CENTER, 0, 0)
                    .show()
        }
    }

    private fun editViews(view: View) {
        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val iconSize = resources.getDimension(R.dimen.fragment_account_login_edit_icon).toInt()
        val compoundDrawablePadding = resources.getDimension(R.dimen.fragment_account_login_edit_icon_padding).toInt()
        usernameEditText.setCompoundDrawables(IconicsDrawable(requireContext(), MaterialDesignIconic.Icon.gmi_account).apply {
            sizeDp = iconSize
            colorInt = R.color.colorNormal
        }, null, null, null)
        usernameEditText.compoundDrawablePadding = compoundDrawablePadding

        val passwordEditText = view.findViewById<EditText>(R.id.password)
        passwordEditText.setCompoundDrawables(null, null, IconicsDrawable(requireContext(), MaterialDesignIconic.Icon.gmi_eye_off).apply {
            sizeDp = iconSize
            colorInt = R.color.colorNormal
        }, null)
        usernameEditText.compoundDrawablePadding = compoundDrawablePadding
    }
}


class OauthSourceViewHolder(itemView: View) : ViewHolder(itemView) {
    val icon: IconicsImageView = itemView.findViewById(R.id.icon)
    val title: AppCompatTextView = itemView.findViewById(R.id.title)

}

data class OauthSourceItem(val id: Int, val name: String, val icon: String)