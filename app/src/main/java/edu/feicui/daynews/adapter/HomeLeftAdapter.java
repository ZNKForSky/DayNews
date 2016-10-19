package edu.feicui.daynews.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.feicui.daynews.R;
import edu.feicui.daynews.entity.LeftItem;

/**
 * Created by Administrator on 2016/10/11.
 */
public class HomeLeftAdapter extends MyBaseAdapter<LeftItem> {

    Context mContext;
    ArrayList<LeftItem> mList;
    int mLayoutId;
    public HomeLeftAdapter(Context mContext, ArrayList<LeftItem> mList, int mLayoutId) {
        super(mContext, mList, mLayoutId);
        this.mContext=mContext;
        this.mList=mList;
        this.mLayoutId=mLayoutId;

    }

    @Override
    public void setView(int position, View convertView, MyHolder holder, LeftItem leftItem) {
        /**
         * 找控件
         */
        ImageView ivLeftItem= (ImageView) convertView.findViewById(R.id.img_item_home_left);
        TextView tvLeftChinese= (TextView) convertView.findViewById(R.id.tv_item_home_left_title_chinese);
        TextView tvLeftEnglish= (TextView) convertView.findViewById(R.id.tv_item_home_left_title_english);
        /**
         * 渲染
         */
        ivLeftItem.setImageResource(leftItem.mIv);
        tvLeftChinese.setText(leftItem.mChineseTv);
        tvLeftEnglish.setText(leftItem.mEnglishTv);
    }
}
