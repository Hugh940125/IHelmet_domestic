package com.slinph.ihairhelmet4.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.activity.ChangePasswordActivity;
import com.slinph.ihairhelmet4.activity.CustomerServiceActivity;
import com.slinph.ihairhelmet4.activity.DownloadShareActivity;
import com.slinph.ihairhelmet4.activity.EquipmentInfoActivity;
import com.slinph.ihairhelmet4.activity.MainActivity;
import com.slinph.ihairhelmet4.activity.ScanActivity;
import com.slinph.ihairhelmet4.utils.SharePreferencesUtils;

/**
 * Created by Administrator on 2016/7/5.
 * dd
 */
public class AboutMeFragment extends Fragment{
    private MainActivity mActivity;
    private TextView aboutMe1;
    private TextView aboutMe2;
    private TextView aboutMe3;
    private TextView aboutMe4;
    //private TextView aboutMe5;
    private TextView aboutMe6;
    private TextView aboutMe7;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mActivity = (MainActivity) getActivity();
        View MainView = View.inflate(mActivity, R.layout.fragment_aboutme,null);
        FindView(MainView);
        InitView();
        return MainView;
    }

    private void InitView() {
        aboutMe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EquipmentInfoActivity.class));
            }
        });
        aboutMe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
        });
        aboutMe3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(mActivity.getString(R.string.tips))
                        .setMessage(mActivity.getString(R.string.sure_logout))
                        .setPositiveButton(mActivity.getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharePreferencesUtils.putString(getActivity() ,"SP_PHONE","");
                                SharePreferencesUtils.putString(getActivity() ,"SP_PSW","");
                                SharePreferencesUtils.putBoolean(getActivity() ,"helmet_version",false);
                                //startActivity(new Intent(getActivity(), LoginActivity.class));
                                mActivity.finish();
                                final Intent intent = mActivity.getApplicationContext().getPackageManager().getLaunchIntentForPackage(mActivity.getApplicationContext().getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                //退出以清空内存的数据防止各个账号之间的数据混在一起
                                System.exit(0);
                            }
                        })
                        .setNegativeButton(mActivity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
        aboutMe4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ScanActivity.class));
            }
        });
        /*aboutMe5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getActivity(), FollowUpActivity.class));
            }
        });*/
        aboutMe6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DownloadShareActivity.class));
            }
        });
        aboutMe7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerServiceActivity.class));
            }
        });
    }

    private void FindView(View mainView) {
        aboutMe1 = (TextView) mainView.findViewById(R.id.aboutMe1);
        aboutMe2 = (TextView) mainView.findViewById(R.id.aboutMe2);
        aboutMe3 = (TextView) mainView.findViewById(R.id.aboutMe3);
        aboutMe4 = (TextView) mainView.findViewById(R.id.aboutMe4);
        //aboutMe5 = (TextView) mainView.findViewById(R.id.aboutMe5);
        aboutMe6 = (TextView) mainView.findViewById(R.id.aboutMe6);
        aboutMe7 = (TextView) mainView.findViewById(R.id.aboutMe7);
    }
}
