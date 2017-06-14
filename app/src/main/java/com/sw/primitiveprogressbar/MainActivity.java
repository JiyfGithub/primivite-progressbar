package com.sw.primitiveprogressbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //找到控件
        TextView tv = (TextView) findViewById(R.id.tv_start);
        final CustomView customview = (CustomView) findViewById(R.id.customview);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始下载
                customview.startDownload();
            }
        });
    }
}
