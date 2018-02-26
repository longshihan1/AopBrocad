package com.longshihan.aopbrocad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.longshihan.broca_annotation.Bind;
import com.longshihan.broca_annotation.LocalBind;
import com.longshihan.broca_api.BroadcastBindInjector;
import com.longshihan.broca_api.Unbinder;
import com.longshihan.broca_api.ViewInjector;

public class MainActivity extends Activity {

    @Bind(R.id.maintext)
    TextView maintext;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.injectView(this);
        unbinder = BroadcastBindInjector.injectBrocast(this);

        maintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });
    }

    @LocalBind({"123", "345","4586"})
    public void startvie(Intent intent) {
        Log.d("LLL", intent.getAction());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
