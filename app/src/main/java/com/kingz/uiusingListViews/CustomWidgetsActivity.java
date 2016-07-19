package com.kingz.uiusingListViews;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ListBillData;
import com.customview.pages.CircleViewActivity;
import com.customview.pages.FingerBallActivity;
import com.customview.NetworkSpeed.NetworkSpeedActivity;
import com.customview.pages.WaveLoadingActivity;
import com.customview.listview.CustomListViewActivity;
import com.customview.scroll.SliderListViewActivity;
import com.customview.scroll.ScrollTestActivity;
import com.customview.slider.wzviewpager.OriginViewPagerActivity;
import com.customview.slider.wzviewpager.WZViewPagerActicity;
import com.kingz.controls.SpansActivity;
import com.kingz.uiusingActivity.LableTextView_Act;
import com.kingz.uiusingCanvas.CustomCanvasSeekBarAct;
import com.kingz.uiusingWidgets.KingWebViewActivity;
import com.kingz.uiusingWidgets.ToastTestActivity;
import com.kingz.uiusingWidgets.UsingCustomSeekBar;

/**
 * 自定义View汇总主界面
 */
public class CustomWidgetsActivity extends  Activity implements OnItemClickListener{

	private ArrayAdapter<ListBillData> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("CustomWidgetsActivity","CustomWidgetsActivity onCreate");

 		//1:加载ListView布局
		setContentView(R.layout.customview_main);
		ListView listView = (ListView) findViewById(R.id.widgetsListView_id);
		//2:初始化适配器
		mAdapter = new ArrayAdapter<ListBillData>(this, R.layout.list_bill);
		//3：为LisView设置数据适配器
		listView.setAdapter(mAdapter);
		addData();
		listView.setOnItemClickListener(this);
	}

	private void addData() {
		mAdapter.add(new ListBillData(this,"Customer TitleView",new Intent(this,CircleViewActivity.class)));
		mAdapter.add(new ListBillData(this,"Customer SeekBar",new Intent(this,UsingCustomSeekBar.class)));
		mAdapter.add(new ListBillData(this,"Canvas SeekBar",new Intent(this,CustomCanvasSeekBarAct.class)));
		mAdapter.add(new ListBillData(this,"LableText",new Intent(this,LableTextView_Act.class)));
		mAdapter.add(new ListBillData(this,"TrackBall",new Intent(this,FingerBallActivity.class)));
		mAdapter.add(new ListBillData(this,"Custom ListView",new Intent(this,CustomListViewActivity.class)));
		mAdapter.add(new ListBillData(this,"左右滑动删除的ListView",new Intent(this,SliderListViewActivity.class)));
		mAdapter.add(new ListBillData(this,"滚动View的测试",new Intent(this,ScrollTestActivity.class)));
		mAdapter.add(new ListBillData(this,"滚动View_2",new Intent(this,ScrollTestActivity.class)));
		mAdapter.add(new ListBillData(this,"滚动View_3",new Intent(this,ScrollTestActivity.class)));
		mAdapter.add(new ListBillData(this,"滚动View_4",new Intent(this,ScrollTestActivity.class)));
		mAdapter.add(new ListBillData(this,"SpanLable",new Intent(this,SpansActivity.class)));
		mAdapter.add(new ListBillData(this,"WebAPP",new Intent(this,KingWebViewActivity.class)));
		mAdapter.add(new ListBillData(this,"Toast",new Intent(this,ToastTestActivity.class)));
		mAdapter.add(new ListBillData(this,"NetworkSpeed",new Intent(this,NetworkSpeedActivity.class)));
		mAdapter.add(new ListBillData(this,"原生ViewPager",new Intent(this,OriginViewPagerActivity.class)));
		mAdapter.add(new ListBillData(this,"WZViewPager",new Intent(this,WZViewPagerActicity.class)));
		mAdapter.add(new ListBillData(this,"WaveLoading",new Intent(this,WaveLoadingActivity.class)));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		/** 窗口切换自定义动画设置 */
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(this,R.anim.zoom_enter,R.anim.zoom_enter);
		ListBillData data = mAdapter.getItem(position);
		data.startActivity(opts);
	}

}