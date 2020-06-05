package com.aboust.develop_guide.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import timber.log.Timber

open class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("${javaClass.simpleName}.onAttach")
    }


    override fun onPause() {
        super.onPause()
        Timber.d("${javaClass.simpleName}.onPause")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("${javaClass.simpleName}.onViewCreated")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Timber.d("${javaClass.simpleName}.onActivityCreated")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("${javaClass.simpleName}.onCreate")

    }

    override fun onLowMemory() {
        super.onLowMemory()
        Timber.d("${javaClass.simpleName}.onLowMemory")

    }

    override fun onResume() {
        super.onResume()
        Timber.d("${javaClass.simpleName}.onResume")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("${javaClass.simpleName}.onDetach")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("${javaClass.simpleName}.onCreateView")

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        Timber.d("${javaClass.simpleName}.onAttachFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("${javaClass.simpleName}.onDestroyView")

    }

    override fun onStart() {
        super.onStart()
        Timber.d("${javaClass.simpleName}.onStart")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("${javaClass.simpleName}.onStop")
    }


}