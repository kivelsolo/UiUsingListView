package com.photo;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.BaseActivity;
import com.kingz.customdemo.R;
import com.utils.BitMapUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/8/6 23:05
 * description:
 *
 *   一个采用recycleView的布局页面来显示图片特效（还未完成）.
 *       LayoutManager: 管理RecyclerView的结构.
         Adapter: 处理每个Item的显示.
         ItemDecoration: 添加每个Item的装饰.
         ItemAnimator: 负责添加\移除\重排序时的动画效果.

        LayoutManager\Adapter是必须, ItemDecoration\ItemAnimator是可选.
 *
 *
 *   tips:8月7号-----打算用GridView来显示。  或者listView  点击后变换图片
 *
 *
 */
public class PhotosActivity extends BaseActivity {

    public static final String TAG = "PhotosActivity";

    private Bitmap srcBitmap;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView img7;

    private RecyclerView recyclerView;
    private DemoRecyclerAdapter mAdapter;

    @Override
    protected void findID() {
        super.findID();
        setContentView(R.layout.photos_activity);
//        recyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
//        initRecyclerView(recyclerView);

        img1 = (ImageView) findViewById(R.id.normal_pic);
        img2 = (ImageView) findViewById(R.id.translate_pic);
        img3 = (ImageView) findViewById(R.id.scale_pic);
        img4 = (ImageView) findViewById(R.id.rotate_pic);
        img5 = (ImageView) findViewById(R.id.corner_pic);
        img6 = (ImageView) findViewById(R.id.circle_pic);
        img7 = (ImageView) findViewById(R.id.skew_pic);
        setImageView();
    }

    /**
     * 初始化RecyclerView
     * @param recyclerView
     */
    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);         // 设置固定大小
        initRecyclerLayoutManager(recyclerView);    // 初始化布局
        initRecyclerAdapter(recyclerView);          // 初始化适配器
        initItemDecoration(recyclerView);           // 初始化装饰
//        initItemAnimator(recyclerView);           // 初始化动画效果
    }

    /**
     * 管理RecyclerView的布局结构
     * @param recyclerView
     */
    private void initRecyclerLayoutManager(RecyclerView recyclerView) {
        //LayoutManager:GridLayoutManager(网格)/LinearLayoutManager(线性)/StaggeredGridLayoutManager(错列网格)
        // 错列网格布局 spanCount：方向垂直，就是列数  方向水平，就是行数
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
    }

    /**
     * 初始化适配器
     * @param recyclerView
     */
    private void initRecyclerAdapter(RecyclerView recyclerView) {
        //创建初始化数据
        mAdapter = new DemoRecyclerAdapter(getData());
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 项的装饰, 比如ListView中的分割线, 在本例中, 左右两条粉线.
     * @param recyclerView
     */
    private void initItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new MyItemDecoration(this));
    }


    private void setImageView() {
        srcBitmap = BitMapUtils.drawable2Bitmap(getResources().getDrawable(R.drawable.wang), 320, 180);
        //加载原始图片
        img1.setImageDrawable(getResources().getDrawable(R.drawable.wang));
        //加载平移的图片
        img2.setImageBitmap(srcBitmap);

        //加载剪切的图片
        img3.setImageBitmap(BitMapUtils.zoomImg(srcBitmap,160,90));

        //旋转(顺时针)
        img4.setImageBitmap(BitMapUtils.rotateImage(180, srcBitmap));

        //圆角矩形
        img5.setImageBitmap(BitMapUtils.setRoundCorner(srcBitmap,45));

        //圆形图片
        img6.setImageBitmap(BitMapUtils.setCircle(srcBitmap));

        //X/Y轴倾斜图片
        img7.setImageBitmap(BitMapUtils.setSkew(srcBitmap,-0.3f,0));
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();

    }

    @Override
    protected void Listener() {
        super.Listener();
    }

    @Override
    protected void initIntent() {
        super.initIntent();
    }

    /**
     * 模拟的数据
     *
     * @return 数据
     */
    private ArrayList<PhotoDataModel> getData() {
        int count = 10;
        ArrayList<PhotoDataModel> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            PhotoDataModel model = new PhotoDataModel();
            model.setDateTime(getBeforeDay(new Date(), i));
            model.setLabel("No. " + i);
            data.add(model);
        }
        return data;
    }
    /**
     * 获取日期的前一天
     *
     * @param date 日期
     * @param i    偏离
     * @return 新的日期
     */
    private static Date getBeforeDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, i * (-1));
        return calendar.getTime();
    }


}
