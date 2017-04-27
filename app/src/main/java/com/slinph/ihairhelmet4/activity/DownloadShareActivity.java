package com.slinph.ihairhelmet4.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.slinph.ihairhelmet4.R;

public class DownloadShareActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_share_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_share);
        InitView();
        initEvent();
    }

    public void InitView(){
        ib_share_back = (ImageButton) findViewById(R.id.ib_share_back);
    }

    public void initEvent(){
        if (ib_share_back != null){
            ib_share_back.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_share_back:
                finish();
                break;
        }
    }
}
