package com.slinph.ihairhelmet4.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.utils.StaticVariables;


/**
 * Created by Waiting on 2016/2/19.
 *
 */
public class ScanSuccessActivity extends BaseActivity {

    private Button btn_confirm;
    private TextView tv_user_name,tv_user_sex,tv_user_age,tv_user_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar("绑定成功");
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.equip, menu);
        return true;
    }

    @Override
    protected int addLayoutId() {
        return R.layout.activity_scan_success;
    }

    @Override
    protected void findViews() {
        tv_user_name=(TextView)findViewById(R.id.tv_user_name);
        tv_user_sex=(TextView)findViewById(R.id.tv_user_sex);
        tv_user_age=(TextView)findViewById(R.id.tv_user_age);
        tv_user_phone=(TextView)findViewById(R.id.tv_user_phone);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanSuccessActivity.this.finish();
                //toAty(LoseHairInfoActivity.class);
            }
        });
    }

    private void initData() {
        tv_user_name.setText(StaticVariables.USER_REALNAME);
        tv_user_sex.setText(StaticVariables.USER_SEX);
        tv_user_age.setText(StaticVariables.USER_AGE);
        tv_user_phone.setText(StaticVariables.USER_PHONE);
    }

}
