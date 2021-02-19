package com.kingz.base

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

abstract class BaseVMFragment<V : BaseRepository, T : BaseViewModel<V>>
    : BaseSimpleFragment() {

    protected abstract val viewModel: T

    override fun initViewModel() {
        super.initViewModel()
        initViewModelActions()
    }

    private fun initViewModelActions() {
        viewModel.statusLiveData.observe(this, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {//协程开始
                        Log.d("coroutine-status:", "---> START")
                    }
                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
                        Log.d("coroutine-status:", "---> REFRESH")
                        activity?.run {}
                    }
                    CoroutineState.FINISH -> {//协程结束
                        Log.d("coroutine-status:", "<--- FINISH")
                    }
                    CoroutineState.ERROR -> {//协程异常
                        Log.d("coroutine-status:", "<--- ERROR")
                    }
                }
            }
        })
    }

    fun launch(blockCode: Block) {
        lifecycleScope.launch {
            try {
                blockCode()
            } catch (e: Exception) {
                //异常处理
            }
        }
    }

    fun launchMain(block: Block) {
        viewModel.launchMain { block() }
    }

    fun launchIO(block: Block) {
        viewModel.launchIO { block() }
    }

    fun launchDefault(block: Block) {
        viewModel.launchDefault { block() }
    }
}