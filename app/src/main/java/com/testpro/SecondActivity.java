package com.testpro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.testpro.baselib.MyClass;

/**
 * 测试界面
 */
public class SecondActivity extends Activity {

    private TextView tv_1, tv_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tv_1 = (TextView) findViewById(R.id.tv_1);

        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyClass.test1();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
