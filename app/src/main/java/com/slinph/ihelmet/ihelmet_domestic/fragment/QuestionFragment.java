package com.slinph.ihelmet.ihelmet_domestic.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.activity.QuestionDeteilActivity;
import com.slinph.ihelmet.ihelmet_domestic.adapter.SimpleTreeAdapter;
import com.slinph.ihelmet.ihelmet_domestic.utils.FileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 *
 */
public class QuestionFragment extends Fragment implements AdapterView.OnItemClickListener{

    private List<FileBean> mDatas = new ArrayList<FileBean>();
    private ListView mTree;
    private SimpleTreeAdapter mAdapter;
    private Activity mContext;
    private ListView lv_groups;
    private ArrayList<String> groupNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        groupNames = new ArrayList<>();
        groupNames.add("一、效果及使用方法");
        groupNames.add("二、APP使用相关");
        groupNames.add("三、头盔使用相关");
        groupNames.add("四、安全性");
        groupNames.add("五、服务");
    }

   /* private void initDatas()
    {
        // id , pid , label , 其他属性
        mDatas.add(new FileBean(1, 0, "一、效果及使用方法"));
        mDatas.add(new FileBean(2, 0, "二、APP使用相关"));
        mDatas.add(new FileBean(3, 0, "三、头盔使用相关"));
        mDatas.add(new FileBean(4, 0, "四、安全性"));
        mDatas.add(new FileBean(5, 0, "五、服务"));
        //效果及使用方法
        mDatas.add(new FileBean(6, 1, "1.使用i黑密期间可以使用其它药物治疗吗？"));
        mDatas.add(new FileBean(119, 6, "i黑密采用医学软激光照射，刺激萎缩的毛囊重新发育，是纯物理的治疗方式，安全无任何副作用，也不与其他治疗方法冲突。" +
                "因此可以在使用i黑密期间使用其它药物联合治疗。通常采用的药物为米诺地尔（外用）和非那雄胺（口服/限男性），如这两种药物产生了明显副作用时，" +
                "可根据医生的建议，停用药物。在采用外用药物或营养液喷涂时，建议在使用头盔后半小时内进行。因为此时头皮经过照射后，有一定热量留存。毛孔打开，" +
                "更有利于成分吸收。"));

        mDatas.add(new FileBean(7, 1, "2.是要洗头以后用么？"));
        mDatas.add(new FileBean(30, 7, "保持头发干净，头皮也无水分即可。"));

        mDatas.add(new FileBean(8, 1, "3.完整的治疗周期是多久？"));
        mDatas.add(new FileBean(111, 8, "平均的治疗周期是1个月控油，2-3个月止脱粗发，3-6个月生发。连续使用1年至1年半达到最佳效果。之后也需要每周使用2次保持效果。"));

        mDatas.add(new FileBean(9, 1, "4.为什么用了这么长时间，都没有感觉变化？"));
        mDatas.add(new FileBean(32, 9, "防脱生发是一个比较漫长的过程，毛囊从休止期到生长期至少需要3到4个月，所以一定要有耐心。坚持规范使用，一般连续使用1年至1年半可以达到最佳效果。"));

        mDatas.add(new FileBean(10, 1, "5.如何露出脱发部位的头发？"));
        mDatas.add(new FileBean(33, 10, "如能理短发，长度不超过3厘米最佳。" +
                "如不能理短发，则需要以下方法：" + "对于M型脱发者，头顶不脱发的用户，建议向后撸起头发后，使用随产品附带的发夹或头发贴固定。" +
                "对于M型脱发者，头顶脱发的用户，建议向后部两侧撸起头发后，使用随产品附带的发夹或头发贴固定，注意露出脱发部位的头皮，不要被向后的头发重新遮挡。" +
                "对于仅头顶脱发者，尽量用手拨开头顶头发，露出脱发部位接受光照。"));

        mDatas.add(new FileBean(11, 1, "6.植发后多久可以用？"));
        mDatas.add(new FileBean(34, 11, "在清理完创口血痂后三天后使用（约7-10天），确保头部没有伤口。"));

        mDatas.add(new FileBean(12, 1, "7.为什么使用头盔后反而掉头发更多了？"));
        mDatas.add(new FileBean(35, 12, "头发的生长是有周期的，分别要经历生长期（2-6年）-退行期（2-3周）-休止期（2-3个月）-生长期（2-6年）....循环不断" +
                "低能量激光能够穿透真皮层3-5mm处并直接刺激线粒体，促进线粒体产生三磷酸腺苷（ATP毛囊细胞生长的能量单位）。更多的ATP意味着更高效的毛囊新陈代谢，从而使毛囊加速脱离退行期和休止期，加快进入并尽可能停留在生长期。" +
                "如果使用者处在脱发的稳定期，并且头发有较多处在退行期和休止期，激光会加速毛囊脱离退行期和休止期，进入生长期。具体表现就是极少数人短时间（约2-3周）部分区域内掉发更多。待毛囊完成加速赶底的周期过程，进入生长期后，头发就会重新生长出来。"));

        //APP使用相关
        mDatas.add(new FileBean(13, 2, "1.油脂度是怎么测定的？为什么连续使用很久都没有变化？"));
        mDatas.add(new FileBean(36, 13, "i黑密采用的是高精度传感器以及独有算法进行油脂度测量的，反应非常灵敏，能感知头皮油脂度的任何变化。头皮油脂度容易受到头皮头发上的水分影响而产生偏差，有以下情况：\n" +
                "1)刚洗完头发，虽然头发干了，但是头发根部和头皮并没有干，还残留少量水分，这样便会导致油脂度读数偏高，可能会超过90%；\n" +
                "2)刚进行完体力活动或者在环境温度超过28度情况下使用，头皮会分泌很多汗液，这些汗液的水分也会导致油脂度读数偏高，可能会超过90%。"));

        mDatas.add(new FileBean(14, 2, "2.为什么蓝牙连接不上？"));
        mDatas.add(new FileBean(37, 14, "首先确认您的手机版本：\n" +
                "安卓手机须在4.3以上,安卓手机还需确认您下载的版本为：i黑密BLE\n" +
                "苹果手机须在7.0.1以上\n" +
                "打开头盔，确认绿色电源指示灯常亮，头盔前部蓝色指示灯闪烁；\n" +
                "打开APP，切换至“光照”模块，会自动搜索头盔，出现ihelmet，点击连接\n" +
                "如反复搜索不到，请重新启动手机和头盔做再次尝试或卸载APP重新下载安装。"));

        mDatas.add(new FileBean(15, 2, "3.APP在使用中会失灵？"));
        mDatas.add(new FileBean(38, 15, "由于头盔与APP之间采用无线信号连接，受距离，阻隔等环境影响较大，因此会存在断开连接，数据不能及时刷新的情况。但是只要在一开始连接后开始治疗，头盔就会自动保存全部的治疗记录，在下一次与手机连接时自动上传至后台服务器保存。"));

        mDatas.add(new FileBean(16, 2, "4.如何查看光照记录"));
        mDatas.add(new FileBean(39, 16, "点击治疗界面下方治疗进度条区域，即可查看当月治疗记录及总治疗次数。通过点击月份两侧的箭头，可以查看相邻月份的治疗记录。"));

        //头盔使用相关
        mDatas.add(new FileBean(17, 3, "1.为什么每次光照时间长度会变的？"));
        mDatas.add(new FileBean(40, 17, "激光的功率受环境温度影响较大，温度越高，功率就越低，冬夏季可以相差30%以上。\n" +
                "简单来说，激光能量=激光功率*照射时间，要保证治疗效果，就要保证激光能量恒定，当激光功率下降时，就要增加照射时间补偿，当激光功率提高时，就要减少照射时间。因此i黑密内置高精度传感器，每次开机检测环境温度，自动决定光照时长，保证总能量恒定。这是i黑密相对传统激光生发产品独特的优势之一。"));

        mDatas.add(new FileBean(18, 3, "2.每天用可以么？"));
        mDatas.add(new FileBean(41, 18, "不需要每天使用，每2天使用一次即可。毛囊细胞组织吸收激光能量后，需要一定的时间进行一系列生化反应来促进细胞的生长，因此毛囊的发育是一个逐步累积的过程，每2天光照一次完全可以满足细胞生长的需求。"));

        mDatas.add(new FileBean(19, 3, "3.一年后就不需要使用了么？"));
        mDatas.add(new FileBean(42, 19, "在达到最佳生发效果后，依然需要每周使用两次进行保持，既然已经养成了习惯，就像刷牙一样成为您生活的一部分，呵护好您的头发。"));

        mDatas.add(new FileBean(20, 3, "4.充一次电用多久？"));
        mDatas.add(new FileBean(43, 20, "充满电后可以用两次。"));

        mDatas.add(new FileBean(21, 3, "5.头盔声音可以变小或者关闭么？"));
        mDatas.add(new FileBean(44, 21, "在APP连接头盔后，光照界面左上角会出现喇叭标志，点击可以打开/关闭头盔声音提示，头盔声音大小不能改变。"));

        mDatas.add(new FileBean(22, 3, "6.使用头盔很热，会流汗？"));
        mDatas.add(new FileBean(45, 22, "激光照射附带有热效应，会加速头部血液循环，从而刺激头发的生长。如使用环境温度超过26度，这种热效应也会引起头皮汗腺的分泌，尤其夏天，导致汗液增多，这是正常现象，不必担心。建议在空调环境下使用更舒适。"));

        //安全性
        mDatas.add(new FileBean(23, 4, "1.什么人不能用？"));
        mDatas.add(new FileBean(46, 23, "未成年人，黑色人种，孕妇，癫痫，光敏感，头部有伤口以及头部有其他皮肤类疾病者不适合使用。哺乳期女性，使用时要避免照射到婴儿眼睛。"));

        mDatas.add(new FileBean(24, 4, "2.什么人适合用？"));
        mDatas.add(new FileBean(47, 24, "患有雄性激素性脱发，植发后的恢复和保持，压力性脱发、产后脱发，化疗后脱发，斑秃（在头盔光照范围内）\n" +
                "对于非病理性脱发，例如头发先天发质不佳，头发营养不良，精神压力过大等造成的脱发，也非常合适的。"));

        mDatas.add(new FileBean(25, 4, "3.戴在头上会对大脑有影响吗？有没有其他隐藏的风险？"));
        mDatas.add(new FileBean(48, 25, "本产品安全无副作用，通过了国际医疗安全标准IEC60601-1和家用医疗安全标准IEC60601-1-11的严格测试。它使用的是功率为650nm，5mw的低能量激光，激光能量仅能到达头皮的真皮层，不会穿透真皮层，更到达不了头骨。经过广泛临床实验，激光头盔可以加速头皮血液循环，改善头皮的血液供应，有利于舒缓紧张的大脑头皮。迄今为止，国内外的临床应用未发现一例副作用的报告。"));

        mDatas.add(new FileBean(26, 4, "4.产品有什么认证？"));
        mDatas.add(new FileBean(49, 26, "产品已整体通过欧盟CE认证和FDA认证检测，所有检测报告都是委托国际三大权威独立检测机构SGS出具的，包括IEC60601-1（医疗设备系统通用安全标准）、IEC60601-1-2（医疗设备电磁兼容性标准）、IEC60825-1（医疗激光标准）、IEC60601-1-11B（家用医疗设备安全与性能要求标准）、按照最严格的标准进行测试认证的。"));
        //服务
        mDatas.add(new FileBean(27, 5, "1.什么是定制的光照方案？"));
        mDatas.add(new FileBean(50, 27, "我们的头盔有200颗高密度激光器，被分为了7片区域，每片区域都可以单独调节光照强度。因此可以为每个用户定制个性化的光照方案，在脱发部位加强光照，不脱发部位减弱，大大提升光照的效果，精准治疗。"));

        mDatas.add(new FileBean(28, 5, "2.怎么感觉不到定制的光照方案？"));
        mDatas.add(new FileBean(51, 28, "当定制的光照方案发回用户APP后，可以在专家建议中查看。此时再次连接头盔，光照模式相对于之前的体验模式会有变化，从外部蓝色半透明罩壳可以看出激光灯的闪烁方式有变化，同时光照时间也会由20分钟固定时间长度变为25-35分钟变化时长。在光照界面上也会出现一个脱发级别的标志（用拉丁字母表示），代表属于此种脱发模式的光照方案。"));

        mDatas.add(new FileBean(29, 5, "3.什么是随诊服务？"));
        mDatas.add(new FileBean(52, 29, "用户在购买产品后需要通过手机APP注册上传自身报告及照片，我们的专家在后台进行诊断并给出定制的光照方案，在随后的治疗期间，用户还需要每个月上传报告和新的照片。专家根据情况变化会提供指导意见，也可能会调整治疗方案，这就是随诊服务，目前是连续六个月免费。"));

        mDatas.add(new FileBean(113, 5, "4.产品有什么保修政策？"));
        mDatas.add(new FileBean(53, 113, "i黑密提供三年质保服务，非人为质量问题，提供免费保修。"));

        mDatas.add(new FileBean(31, 5, "5.什么是黑密君？"));
        mDatas.add(new FileBean(54, 31, "黑密君是我们的微信专家服务号，提供一对一的产品售后支持与咨询服务。"));
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //注释掉的部分是三级展开的方式，打开这里和xml文件的注释即可使用
        /*View inflate = inflater.inflate(R.layout.fragmrnt_question, null);
        mTree = (ListView) inflate.findViewById(R.id.id_tree);
        initDatas();
        mTree = (ListView) inflate.findViewById(R.id.id_tree);
        try
        {
            mAdapter = new SimpleTreeAdapter<>(mTree,mContext, mDatas, 0);

            mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener()
            {
                @Override
                public void onClick(Node node, int position)
                {
                    if (node.isLeaf())
                    {
                        //Toast.makeText(mContext, node.getName(),Toast.LENGTH_SHORT).show();
                    }
                }

            });
            mTree.setAdapter(mAdapter);
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return inflate;*/

        View inflate = inflater.inflate(R.layout.fragmrnt_question, null);
        lv_groups = (ListView) inflate.findViewById(R.id.lv_groups);
        if (lv_groups != null){
            lv_groups.setAdapter(new QuestionGroupAdapter());
            lv_groups.setOnItemClickListener(this);
        }
        return inflate;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, QuestionDeteilActivity.class);
        switch (position){
            case 0:
                intent.putExtra("item_number",0);
                startActivity(intent);
                break;
            case 1:
                intent.putExtra("item_number",1);
                startActivity(intent);
                break;
            case 2:
                intent.putExtra("item_number",2);
                startActivity(intent);
                break;
            case 3:
                intent.putExtra("item_number",3);
                startActivity(intent);
                break;
            case 4:
                intent.putExtra("item_number",4);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private class QuestionGroupAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return groupNames.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View inflate = View.inflate(mContext, R.layout.questiongroup_item, null);
            TextView tv_question_group = (TextView) inflate.findViewById(R.id.tv_question_group);
            if (tv_question_group != null){
                tv_question_group.setText(groupNames.get(position));
            }
            return inflate;
        }
    }
}
