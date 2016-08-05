package com.example.jiangai.tttset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private TextView textView;
    private Button lastButton;
    private Button nextButton;
    private Button reStartButton;
    private int itemColumns = 5;
    private int boomCount = 5;
    private int openedCount;
    private TestAdapter adapter;
    private List<Bean> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.gridView);
        textView = (TextView) findViewById(R.id.textView);
        lastButton = (Button) findViewById(R.id.last);
        nextButton = (Button) findViewById(R.id.next);
        reStartButton = (Button) findViewById(R.id.reStart);

        rebuildData();

        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemColumns>3){
                    itemColumns--;
                    boomCount = itemColumns*itemColumns/5;
                    rebuildData();
                }else {
                    Toast.makeText(MainActivity.this,"到头了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemColumns<10){
                    itemColumns++;
                    boomCount = itemColumns*itemColumns/5;
                    rebuildData();
                }else {
                    Toast.makeText(MainActivity.this,"到头了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        reStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               rebuildData();
            }
        });
    }


    private void open(int position){
        Bean bean = (Bean) adapter.getItem(position);
        if (!bean.show){
            bean.show = true;
            openedCount++;
            if (bean.count==0){
                int l = position/itemColumns;
                int r = position%itemColumns;
                for (int i=l-1;i<=l+1;i++){
                    if (i<0||i>itemColumns-1){
                        continue;
                    }
                    for (int j=r-1;j<=r+1;j++){
                        if (j<0||j>itemColumns-1){
                            continue;
                        }
                        open(i*itemColumns+j);
                    }
                }
            }else if (bean.count==-1){
                openedCount--;
                textView.setText("BOOOOM!!!");
                for (Bean mBean: dataList){
                    mBean.show = true;
                }
            }
            if (openedCount==dataList.size()-boomCount){
                textView.setText("SUCCEED!!!");
                for (Bean mBean: dataList){
                    if (!mBean.show) {
                        mBean.show = true;
                        mBean.count = -2;
                    }
                }
            }
        }
    }

    private void rebuildData(){
        textView.setText("");
        openedCount = 0;
        int[][] sourceData = new int[itemColumns+2][itemColumns+2];
        for (int i=0;i<boomCount;i++){
            int x = (int)(1+Math.random()*(itemColumns));
            int y = (int)(1+Math.random()*(itemColumns));
            if (sourceData[x][y]==-1){
                i--;
                continue;
            }
            sourceData[x][y]=-1;
        }

        for (int i=1;i<itemColumns+1;i++){
            for (int j=1;j<itemColumns+1;j++){
                if (sourceData[i][j]==-1){
                    for (int k=i-1;k<=i+1;k++){
                        for (int n=j-1;n<=j+1;n++){
                            if (sourceData[k][n]!=-1){
                                sourceData[k][n]++;
                            }
                        }
                    }
                }
            }
        }


        dataList = new ArrayList<Bean>();

        for (int i=1;i<itemColumns+1;i++){
            for (int j=1;j<itemColumns+1;j++){
                Bean bean = new Bean(sourceData[i][j],false);
                dataList.add(bean);
            }
        }
        adapter = new TestAdapter(dataList,this);
        gridView.setNumColumns(itemColumns);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                open(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
