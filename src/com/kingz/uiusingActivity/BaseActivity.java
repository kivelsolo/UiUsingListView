package com.kingz.uiusingActivity;

import android.app.Activity;
import android.content.Context;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 18:26
 * description: 基础Activity
 */
public class BaseActivity extends Activity{

    /**	 * 全局变量
	 */
	public static Context mContext;

    public static Context getAppContext() {
		return mContext;
	}

    public void showLoadingDialog(){

    }

}
