package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.adapter.AllReportAdapter;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.TreatmentProgramsVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.model.Page;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;
import com.slinph.ihelmet.ihelmet_domestic.view.MultiStateView;

import java.util.List;

public class AllReportsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private MultiStateView mMultiStateView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<TreatmentProgramsVO> mList;
    private AllReportAdapter mAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);
        mList = StaticVariables.REPORT_LIST;
        Log.e("mList",mList.toString());
        findViews();
    }

    protected void findViews() {
        ImageButton ib_all_back = (ImageButton) findViewById(R.id.ib_all_back);
        if (ib_all_back != null){
            ib_all_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        mMultiStateView = (MultiStateView) findViewById(R.id.multiStateView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        if (mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mAdapter =new AllReportAdapter(this, mList);
        listView = (ListView)findViewById(R.id.listView);
        if (listView != null){
            listView.setAdapter(mAdapter);  
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AllReportsActivity.this, ReportDetailActivity.class);
                intent.putExtra("id", mList.get(position).getId());
                intent.putExtra("type", mList.get(position).getType());
                intent.putExtra("patient_id", mList.get(position).getPatientId());
                startActivity(intent);
            }
        });
        Button btnRetry = (Button) findViewById(R.id.btnRetry);
        if (btnRetry != null){
            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    mList = StaticVariables.REPORT_LIST;
                }
            });
        }

        if (mList != null){
            if (mList.size() > 0){
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }else{
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        }else{
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mList = StaticVariables.REPORT_LIST;
        mAdapter.notifyDataSetChanged();

                HttpUtils.postAsync(AllReportsActivity.this, Url.rootUrl+"/iheimi/treatmentPrograms/selectByExample", new HttpUtils.ResultCallback<ResultData<Page<TreatmentProgramsVO>>>() {
                    @Override
                    public void onError(int statusCode, Throwable error) {

                    }

                    @Override
                    public void onResponse(ResultData<Page<TreatmentProgramsVO>> response) {
                        if (response.getSuccess()){
                            Page<TreatmentProgramsVO> data = response.getData();
                            List<TreatmentProgramsVO> list = data.getList();
                            mList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            AllReportsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(false); //执行
                                    Toast.makeText(AllReportsActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                },new String[]{"limit","10"});
    }
}
