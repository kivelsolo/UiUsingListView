package com.kingz.four_components;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.BaseActivity;
import com.utils.ScreenTools;
import com.utils.ToastTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingZ on 2016/1/13.
 * Discription:获取系统的内容提供数据
 */
public class ObtainConnectPeopleActivity extends BaseActivity{

    private ArrayAdapter<String> adapter = null;
    private List<String> contactaList;

    private Button queryBtn;
    private Button deleteBtn;
    private Button insertBtn;
    private Button updateBtn;

    private Cursor cursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactaList = new ArrayList<>();
        initViews();
        readContacts();
    }

    private void initViews() {
        LinearLayout root = new LinearLayout(this);
        root.setGravity(Gravity.FILL_VERTICAL);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(-1,-1);
        root.setLayoutParams(lps);
        setContentView(root);

        queryBtn = new Button(this);
        queryBtn.setWidth(ScreenTools.Operation(100));
        queryBtn.setHeight(ScreenTools.Operation(10));
        queryBtn.setX(5);
        queryBtn.setY(5);
        queryBtn.setText("查询");

        deleteBtn = new Button(this);
        deleteBtn.setWidth(ScreenTools.Operation(150));
        deleteBtn.setHeight(ScreenTools.Operation(10));
        deleteBtn.setX(20);
        deleteBtn.setY(5);
        deleteBtn.setText("删除One");

        insertBtn = new Button(this);
        insertBtn.setWidth(ScreenTools.Operation(150));
        insertBtn.setHeight(ScreenTools.Operation(10));
        insertBtn.setX(20);
        insertBtn.setY(5);
        insertBtn.setText("InsertOne");

        updateBtn = new Button(this);
        updateBtn.setWidth(ScreenTools.Operation(150));
        updateBtn.setHeight(ScreenTools.Operation(10));
        updateBtn.setX(20);
        updateBtn.setY(5);
        updateBtn.setText("Update");


        ListView contactsView = new ListView(this);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,contactaList);
        contactsView.setAdapter(adapter);
        root.addView(queryBtn);
        root.addView(deleteBtn);
        root.addView(insertBtn);
        root.addView(updateBtn);
        root.addView(contactsView);
    }

    /**
     * 获取系统联系人
     */
    private void readContacts() {
        try {
            if(baseResolver == null){
                baseResolver = getContentResolver();
            }
            cursor = baseResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            while( null != cursor){
                String personName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String personNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactaList.add(personName+"\n"+personNum);
            }
            if(null == cursor){
                ToastTools.getInstance().showToast(this,"未找到联系人");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
