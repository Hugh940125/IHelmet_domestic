package com.slinph.ihairhelmet4.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.slinph.ihairhelmet4.R;

public class CustomerServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        ImageButton ib_customer_back = (ImageButton) findViewById(R.id.ib_customer_back);
        if (ib_customer_back != null){
            ib_customer_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }
}
