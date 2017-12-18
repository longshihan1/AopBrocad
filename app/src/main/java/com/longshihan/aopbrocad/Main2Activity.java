package com.longshihan.aopbrocad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends Activity {

    TextView textView;
    LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.mian2text);
         localBroadcastManager=LocalBroadcastManager.getInstance(this);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("123");
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }
}
