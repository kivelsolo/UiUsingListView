package com.kingz.base

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.zeke.kangaroo.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Top level abstract activity.
 * [MVVM]
 */
abstract class BaseSimpleActivity : AppCompatActivity() {

    private var progress: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        initViewModel()
        initImmersionBar()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    /**
     * Get layout id.
     */
    abstract fun getContentView(): Int

    /**
     * Init immersion style bar.
     */
    private fun initImmersionBar() {
//        immersionBar {
//            barColor(R.color.colorPrimary)
//        }
    }

    /**
     * Init viewmodel
     */
    open fun initViewModel() {}

    /**
     * Init data logic.
     */
    abstract fun initData(savedInstanceState: Bundle?)

    open fun initView(savedInstanceState: Bundle?) {
        progress = ProgressBar(baseContext)
        val progressParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        progressParams.gravity = Gravity.CENTER
        progress?.visibility = View.GONE
        addContentView(progress, progressParams)
    }

    fun showLoading() {
        progress?.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        progress?.visibility = View.GONE
    }


    fun showToast(msg: String) {
        ToastUtils.show(this, msg)
    }

    /**
     * 必须在组合挂起函数中使用
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observeSuspend(onChanged: (t: T) -> Unit) =
        withContext(Dispatchers.Main) {
            observe(this@BaseSimpleActivity, Observer { onChanged(it) })
        }

     /**
     * 不要求组合挂起函数
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) =
        withContext(Dispatchers.Main) {
            observe(this@BaseSimpleActivity, Observer { onChanged(it) })
        }
}