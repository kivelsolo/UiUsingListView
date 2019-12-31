package com.kingz.pages.photo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.holder.CommonViewHolder;
import com.kingz.pages.photo.PhotosActivity;
import com.zeke.kangaroo.adapter.CommonListAdapter;

import java.util.List;

/**
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/25 19:15 <br>
 * description: bitmap页面的适配器 <br>
 */
public class BitmapPageAdapter extends CommonListAdapter<PhotosActivity.ItemInfo> {

    public BitmapPageAdapter( List<PhotosActivity.ItemInfo> mDatas) {
        super( mDatas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.create(position,convertView,
                parent, R.layout.simplelist_every_item);
        TextView nameItem = holder.getView(R.id.list_item);
        PhotosActivity.ItemInfo itemInfo = mData.get(position);
        String text = itemInfo != null ? itemInfo.getName() : "";
        nameItem.setText(text);
        return holder.getInflateView();
    }
}
