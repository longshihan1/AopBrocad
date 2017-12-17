package com.longshihan.aopbrocad;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.longshihan.broca_annotation.Bind;
import com.longshihan.broca_annotation.LocalBind;
import com.longshihan.broca_api.ViewInjector;

public class MainActivity extends AppCompatActivity {


    @Bind(R.id.teeeeeeee)
    TextView teeeee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.injectView(this);
    }

    @LocalBind({"123","345"})
    public void startvie(String name){

    }
}
