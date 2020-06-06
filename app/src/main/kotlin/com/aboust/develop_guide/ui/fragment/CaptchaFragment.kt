package com.aboust.develop_guide.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aboust.develop_guide.R
import com.aboust.develop_guide.widget.RichText
import kotlinx.android.synthetic.main.fragment_captcha.*


class CaptchaFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_captcha, null)
    }

    private val timerJob = object : CountDownTimer(60_000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val template = getString(R.string.re_send_captcha_code)
            val ts = millisUntilFinished / 1000
            val timesStr = "${ts}s"
            val v = String.format(template, timesStr)
            restart_time.text = RichText(requireActivity(), v).first(timesStr).textColor(R.color.colorAccent)
        }

        override fun onFinish() {
            restart_time.text = getString(R.string.re_send_captcha_action)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        timerJob.cancel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViews()
    }

    private fun initViews() {
        timerJob.start()
        val template = getString(R.string.fragment_captcha_description)
        val tel = "180 0000 0000"
        val desc = String.format(template, tel)
        description.text = RichText(requireActivity(), desc).first(tel).textColor(R.color.colorNormal).italic()
    }
}