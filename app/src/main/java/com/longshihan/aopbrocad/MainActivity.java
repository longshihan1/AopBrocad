package com.longshihan.aopbrocad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.longshihan.broca_api.ViewInjector;

public class MainActivity extends Activity {

//
//    @Bind(R.id.teeeeeeee)
//    TextView teeeee;
//
//    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.injectView(this);
//        unbinder= BroadcastBindInjector.injectBrocast(this);

        startActivity(new Intent(this,Main2Activity.class));
    }
//    @LocalBind({"123", "345"})
//    public void startvie(Intent intent) {
//        Log.d("LLL",intent.getAction());
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//       unbinder.unbind();
    }
}
