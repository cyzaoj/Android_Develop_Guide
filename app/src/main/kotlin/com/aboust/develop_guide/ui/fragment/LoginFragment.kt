package com.aboust.develop_guide.ui.fragment

import android.graphics.Color
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.aboust.develop_guide.R
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.LinkBuilder
import com.klinker.android.link_builder.applyLinks
import timber.log.Timber

class LoginFragment : Fragment() {

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

        val linked = Link(getString(R.string.register)).setTextColor(R.color.colorNormal)
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
    }
}