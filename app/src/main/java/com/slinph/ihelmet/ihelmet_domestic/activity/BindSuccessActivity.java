package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;


/**
 * Created by Waiting on 2016/2/19.
 *
 */
public class BindSuccessActivity extends BaseActivity {

    private Button btn_next;
    private TextView tv_user_name,tv_user_sex,tv_user_age,tv_user_phone;
    private Button btn_goon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(0, "绑定成功", 0);
        initData();
    }

    @Override
    protected int addLayoutId() {
        return R.layout.activity_bind_success;
    }

    @Override
    protected void findViews() {
        tv_user_name=(TextView)findViewById(R.id.tv_user_name);
        tv_user_sex=(TextView)findViewById(R.id.tv_user_sex);
        tv_user_age=(TextView)findViewById(R.id.tv_user_age);
        tv_user_phone=(TextView)findViewById(R.id.tv_user_phone);
        btn_goon = (Button) findViewById(R.id.btn_goon);

        if (btn_goon != null){
            btn_goon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BindSuccessActivity.this.finish();
                    toAty(LoseHairInfoActivity.class);
                }
            });
        }
    }

    private void initData() {
        tv_user_name.setText(StaticVariables.USER_REALNAME);
        tv_user_sex.setText(StaticVariables.USER_SEX);
        tv_user_age.setText(StaticVariables.USER_AGE);
        tv_user_phone.setText(StaticVariables.USER_PHONE);
    }

}
