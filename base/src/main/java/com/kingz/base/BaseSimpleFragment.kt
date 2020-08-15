package com.kingz.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseSimpleFragment : Fragment() {
    lateinit var rootView: View
    var mActivity: Activity? = null
    var isActive = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onPause() {
        super.onPause()
        isActive = false
    }

    override fun onResume() {
        super.onResume()
        isActive = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutResID(), container, false)
        onCreateViewReady()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    open fun initViewModel() {}

    abstract fun getLayoutResID(): Int

    /** 进行初始化操作，在onCreateView中调用*/
    open fun onCreateViewReady() {}

    open fun onViewCreated(){}

    abstract fun initData(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)
}