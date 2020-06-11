package com.kingz.mvvm.demo.vm

import androidx.lifecycle.MutableLiveData
import com.kingz.mvvm.base.BaseViewModel
import com.kingz.mvvm.demo.MvvmRepository
import com.kingz.mvvm.demo.entity.LoginEntity
import com.zeke.kangaroo.utils.ZLog

/**
 * LiveData 没有公开可用的方法来更新存储的数据。MutableLiveData 类将公开 setValue(T) 和 postValue(T) 方法，
 * 如果需要修改存储在 LiveData 对象中的值，则必须使用这些方法。
 *
 * 通常情况下会在 ViewModel 中使用 MutableLiveData，
 * 然后 ViewModel 只会向观察者公开不可变的 LiveData 对象。
 */
class LoginViewModel(private val mockRepository: MvvmRepository) : BaseViewModel() {
    val tipsText = "测试DataBinding的数据连接"
    val result = "登陆成功"
    val loginData: MutableLiveData<MutableList<LoginEntity>> = MutableLiveData()

    fun fetchMockData() {
        ZLog.d("MVVM","ViewModel ---> fetchMockData")
        launchIO {
            when (val result = mockRepository.fetchMockLoginData()) {
                else -> {
                    ZLog.d("MVVM","ViewModel <--- Get MockData. PostValue by LiveData.")
                    loginData.postValue(result)
                }
            }
        }
    }

    override fun onCleared() {
        ZLog.d("MVVM","ViewModel ---> onCleared")
        super.onCleared()
    }

}