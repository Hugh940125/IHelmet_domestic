package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.slinph.ihelmet.ihelmet_domestic.R;

public class CompareActivity extends AppCompatActivity {

    private SimpleDraweeView com1;
    private SimpleDraweeView com2;
    private String secondPic;
    private String firstPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Intent intent = getIntent();
        secondPic = intent.getStringExtra("SecondPic");
        firstPic = intent.getStringExtra("FirstPic");

        findView();
        InitView();
    }

    private void findView() {
        com1 = (SimpleDraweeView) findViewById(R.id.com1);
        com2 = (SimpleDraweeView) findViewById(R.id.com2);
    }

    private void InitView() {
        com1.setImageURI("file://" + firstPic);
        com2.setImageURI("file://" + secondPic);
        com1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.parse("file://" + firstPic), "image*//*");
                startActivity(it);*/

                Intent intent = new Intent(CompareActivity.this, ImageZoomActivity.class);
                intent.putExtra("path1", firstPic);
                intent.putExtra("path2",secondPic);
                startActivity(intent);
            }
        });
        com2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(Uri.parse("file://" + secondPic), "image*//*");
                startActivity(it);*/
                Intent intent = new Intent(CompareActivity.this, ImageZoomActivity.class);
                intent.putExtra("path1", firstPic);
                intent.putExtra("path2",secondPic);
                startActivity(intent);
            }
        });
    }
}
