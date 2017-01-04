package com.slinph.ihelmet.ihelmet_domestic.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.activity.CompareActivity;
import com.slinph.ihelmet.ihelmet_domestic.activity.ImagePagerActivity;
import com.slinph.ihelmet.ihelmet_domestic.activity.MainActivity;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.FollowUpVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/5.
 *
 */
public class EvaluationFragment extends Fragment{
    private MainActivity mActivity;
    private SimpleDraweeView new_img1;
    private SimpleDraweeView new_img2;
    private SimpleDraweeView new_img3;
    private SimpleDraweeView new_img4;
    private SimpleDraweeView new_img5;
    private SimpleDraweeView new_img6;
    private SimpleDraweeView new_img7;
    private SimpleDraweeView new_img8;
    private ViewPager viewpager;
    private List<String> paths;
    private ArrayList<View> views;
    private TextView tv_compare1;
    private TextView tv_compare2;
    private TextView tv_compare3;
    private TextView tv_compare4;
    private int Row = 1;
    private String coordinate;
    private Intent intent;
    private TextView tv_time_new;
    private TextView tv_time_old;
    private PicGroupAdapter picGroupAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mActivity = (MainActivity) getActivity();
        View MainView = inflater.inflate(R.layout.fragment_effectevaluation,container,false);
        new_img1 = (SimpleDraweeView) MainView.findViewById(R.id.new_img1);
        new_img2 = (SimpleDraweeView) MainView.findViewById(R.id.new_img2);
        new_img3 = (SimpleDraweeView) MainView.findViewById(R.id.new_img3);
        new_img4 = (SimpleDraweeView) MainView.findViewById(R.id.new_img4);
        tv_compare1 = (TextView) MainView.findViewById(R.id.tv_compare1);
        tv_compare2 = (TextView) MainView.findViewById(R.id.tv_compare2);
        tv_compare3 = (TextView) MainView.findViewById(R.id.tv_compare3);
        tv_compare4 = (TextView) MainView.findViewById(R.id.tv_compare4);
        viewpager = (ViewPager) MainView.findViewById(R.id.viewpager);

        tv_time_new = (TextView) MainView.findViewById(R.id.tv_time_new);
        tv_time_old = (TextView) MainView.findViewById(R.id.tv_time_old);
        return MainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        StaticVariables.PHOTOS_URL.clear();
        HttpUtils.postAsync(mActivity, Url.rootUrl+"/iheimi/treatmentPrograms/selectUserEffect", new HttpUtils.ResultCallback<ResultData<List<FollowUpVO>>>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                // Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<List<FollowUpVO>> response) {
                if (response.getSuccess()){
                    List<FollowUpVO> data = response.getData();
                    SimpleDateFormat Sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                    if (data != null){
                        for (int i = 0;i <data.size();i ++){
                            if (i == data.size() - 1){
                                StaticVariables.USER_HAIR_TOP_PHOTO_URL = data.get(i).getTopViewUrl();//头顶照片
                                StaticVariables.USER_HAIR_LINE_PHOTO_URL = data.get(i).getHairlineUrl();//发际线图片
                                StaticVariables.USER_HAIR_AFTER_PHOTO_URL = data.get(i).getAfterViewUrl();//后脑勺图片
                                StaticVariables.USER_HAIR_PARTIAL_PHOTO_URL = data.get(i).getPartialViewUrl();//侧面图片
                            }
                            String topViewUrl = data.get(i).getTopViewUrl();
                            String hairlineUrl = data.get(i).getHairlineUrl();
                            String afterViewUrl = data.get(i).getAfterViewUrl();
                            String partialViewUrl = data.get(i).getPartialViewUrl();
                            Date createDtm = data.get(i).getCreateDtm();
                            //将Date转换为year-month-day的形式
                            String format = Sdf.format(createDtm);
                            StaticVariables.PHOTOS_URL.add(topViewUrl);
                            StaticVariables.PHOTOS_URL.add(hairlineUrl);
                            StaticVariables.PHOTOS_URL.add(afterViewUrl);
                            StaticVariables.PHOTOS_URL.add(partialViewUrl);
                            StaticVariables.PHOTOS_URL.add(format);
                        }
                        InitView();
                    }
                    String s = StaticVariables.PHOTOS_URL.toString();
                    Log.e("PHOTOS_URL",s);
                }
            }
        });
    }

    ArrayList<String> strings = new ArrayList<>();
    public void InitView(){
        paths = StaticVariables.PHOTOS_URL;
        if (paths.size() > 0){
            new_img1.setImageURI(Uri.parse(paths.get(0)));
            new_img2.setImageURI(Uri.parse(paths.get(1)));
            new_img3.setImageURI(Uri.parse(paths.get(2)));
            new_img4.setImageURI(Uri.parse(paths.get(3)));
            tv_time_new.setText(paths.get(4));
        }

        picGroupAdapter = new PicGroupAdapter();
        viewpager.setAdapter(picGroupAdapter);

        if (paths.size() >= 10){
            new_img5.setImageURI(paths.get(5));
            new_img6.setImageURI(paths.get(6));
            new_img7.setImageURI(paths.get(7));
            new_img8.setImageURI(paths.get(8));
            tv_time_old.setText(paths.get(9));
        }
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Row = position + 1;
                if (paths.size() > 0){
                    Log.e("position",position+"");
                    new_img5.setImageURI(paths.get((position + 1) * 5));
                    new_img6.setImageURI(paths.get((position + 1) * 5 + 1));
                    new_img7.setImageURI(paths.get((position + 1) * 5 + 2));
                    new_img8.setImageURI(paths.get((position + 1) * 5 + 3));
                    tv_time_old.setText(paths.get((position + 1) * 5 + 4));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        intent = new Intent(mActivity, CompareActivity.class);
        if (paths.size() > 5){
            tv_compare1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coordinate = paths.get(Row * 5);
                    strings.clear();
                    strings.add(paths.get(0));
                    strings.add(coordinate);
                    imageBrower(0,strings);
                }
            });
            tv_compare2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coordinate = paths.get(Row * 5 + 1);
                    strings.clear();
                    strings.add(paths.get(1));
                    strings.add(coordinate);
                    imageBrower(0,strings);
                }
            });
            tv_compare3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coordinate = paths.get(Row * 5 + 2);
                    strings.clear();
                    strings.add(paths.get(2));
                    strings.add(coordinate);
                    imageBrower(0,strings);
                }
            });
            tv_compare4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    coordinate = paths.get(Row * 5 + 3);
                    strings.clear();
                    strings.add(paths.get(3));
                    strings.add(coordinate);
                    imageBrower(0,strings);
                }
            });
        }
    }

    /**
     * 打开图片查看器
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        String EXTRA_IMAGE_INDEX = "image_index";
        String EXTRA_IMAGE_URLS = "image_urls";
        Intent intent = new Intent(mActivity, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(EXTRA_IMAGE_INDEX, position);
        mActivity.startActivity(intent);
    }

    class PicGroupAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return paths.size() / 5 - 1; //一组四个，共有多少组,去掉固定显示的第一组
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View inflate = View.inflate(mActivity, R.layout.pic_group, null);
            container.addView(inflate);
            new_img5 = (SimpleDraweeView) inflate.findViewById(R.id.new_img5);
            new_img6 = (SimpleDraweeView) inflate.findViewById(R.id.new_img6);
            new_img7 = (SimpleDraweeView) inflate.findViewById(R.id.new_img7);
            new_img8 = (SimpleDraweeView) inflate.findViewById(R.id.new_img8);
            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(View.inflate(mActivity,R.layout.pic_group,null));
        }
    }

}
