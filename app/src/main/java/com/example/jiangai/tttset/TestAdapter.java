package com.example.jiangai.tttset;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by jiangai on 16/8/2.
 */
public class TestAdapter extends BaseAdapter {
    private List<Bean> dataList;
    private Context context;

    TestAdapter(List<Bean> dataList,Context context){
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(context,R.layout.layout,null);
            TextView textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(textView);
        }
        TextView textView = (TextView)convertView.getTag();
        Bean bean = dataList.get(position);
        if (bean.show){
            switch (bean.count){
                case -1:{
                    textView.setText("💣");
                    textView.setBackgroundColor(Color.parseColor("#ffaaaa"));
                    break;
                }
                case -2:{
                    textView.setText("🚩");
                    textView.setBackgroundColor(Color.parseColor("#ffff00"));
                    break;
                }
                default:{
                    textView.setText(bean.count+"");
                    int blue = 255*bean.count/16+127;
                    String blueStr = Integer.toHexString(blue);
                    textView.setBackgroundColor(Color.parseColor("#AACC"+blueStr));
                    break;
                }
            }
        }else {
            textView.setText("");
            textView.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        return convertView;
    }

}


class Bean{
    public int count;
    public boolean show;
    Bean(int count,boolean show){
        this.count = count;
        this.show = show;
    }
}
