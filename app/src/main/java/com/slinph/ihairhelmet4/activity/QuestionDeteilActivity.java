package com.slinph.ihairhelmet4.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;
import com.slinph.ihairhelmet4.adapter.MyExpandAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionDeteilActivity extends AppCompatActivity {

    private ExpandableListView el_ques;
    private ArrayList<String> group_head;
    private ArrayList<List<String>> child;
    private TextView tv_group_name;
    private int item_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_deteil);

        Intent intent = getIntent();
        item_number = intent.getIntExtra("item_number", -1);
        group_head = new ArrayList<>();
        child = new ArrayList<>();
        initViews();
    }

    private void initContent(int item_number) {
        switch (item_number){
            case 0:
            tv_group_name.setText("效果及使用方法");
            addGroup("1.使用i黑密期间可以使用其它药物治疗吗？");
            addGroup("2.是要洗头以后用么？");
            addGroup("3.完整的治疗周期是多久？");
            addGroup("4.为什么用了这么长时间，都没有感觉变化？");
            addGroup("5.如何露出脱发部位的头发？");
            addGroup("6.植发后多久可以用？");
            addGroup("7.为什么使用头盔后反而掉头发更多了？");
            addChild(0,"i黑密采用医学软激光照射，刺激萎缩的毛囊重新发育，是纯物理的治疗方式，安全无任何副作用，也不与其他治疗方法冲突。" +
                    "因此可以在使用i黑密期间使用其它药物联合治疗。一般联合治疗效果更佳。通常采用的药物为米诺地尔（外用）和非那雄胺（口服/限男性），" +
                    "如这两种药物产生了明显副作用时，可根据医生的建议，逐渐停用药物。在采用外用药物或营养液喷涂时，建议在使用头盔后半小时内进行。" +
                    "因为此时头皮经过照射后，有一定热量留存。毛孔打开，更有利于成分吸收。");
            addChild(1,"保持头发干净，头皮也无水分即可。");
            addChild(2,"平均的治疗周期是1个月控油，2-3个月止脱粗发，3-6个月生发。连续使用1年至1年半达到最佳效果。之后也需要每周使用2次保持效果。");
            addChild(3,"防脱生发是一个比较漫长的过程，毛囊从休止期到生长期至少需要3到4个月，所以一定要有耐心。坚持规范使用，一般连续使用1年至1年半可以达到最佳效果。");
            addChild(4,"如能理短发，长度不超过2厘米最佳。\n" +
                    "如不能理短发，则需要以下方法：\n" +
                    "对于M型脱发者，头顶不脱发的用户，建议向后撸起头发后，使用发夹或头发贴/刘海贴固定。\n" +
                    "对于M型脱发者，头顶脱发的用户，建议向后部两侧撸起头发后，使用发夹或头发贴/刘海贴固定，注意露出脱发部位的头皮，不要被向后的头发重新遮挡。\n" +
                    "对于仅头顶脱发者，尽量用手拨开头顶头发，露出脱发部位接受光照。");
            addChild(5,"在清理完创口血痂后三天后使用（约7-10天），确保头部没有伤口。");
            addChild(6,"头发的生长是有周期的，分别要经历生长期（2-6年）-退行期（2-3周）-休止期（2-3个月）-生长期（2-6年）....循环不断," +
                    "低能量激光能够穿透真皮层3-5mm处并直接刺激线粒体，促进线粒体产生三磷酸腺苷（ATP毛囊细胞生长的能量单位）。更多的ATP意味着更高效的毛囊新陈代谢，" +
                    "从而使毛囊加速脱离退行期和休止期，加快进入并尽可能停留在生长期。" +
                    "如果使用者处在脱发的稳定期，并且头发有较多处在退行期和休止期，激光会加速毛囊脱离退行期和休止期，" +
                    "进入生长期。具体表现就是极少数人短时间（约2-3周）部分区域内掉发更多。待毛囊完成加速赶底的周期过程，进入生长期后，头发就会重新生长出来。");
                break;
            case 1:
                tv_group_name.setText("APP相关");
                addGroup("1.油脂度是怎么测定的？为什么连续使用很久都没有变化？");
                addGroup("2.为什么蓝牙连接不上？");
                addGroup("3.APP在使用中会失灵？");
                addGroup("4.如何查看光照记录？");
                addGroup("5.提醒功能不灵？");
                addChild(0,"i黑密采用的是高精度传感器以及独有算法进行油脂度测量的，反应非常灵敏，能感知头皮油脂度的任何变化。头皮油脂度容易受到空气湿度或头皮头发上的水分影响而产生偏差，有以下情况：\n" +
                        "1)刚洗完头发，虽然头发干了，但是头发根部和头皮并没有干，还残留少量水分，这样便会导致油脂度读数偏高，可能会超过90%；\n" +
                        "2)刚进行完体力活动或者在环境温度超过28度情况下使用，头皮会分泌很多汗液，这些汗液的水分也会导致油脂度读数偏高，可能会超过90%。");
                addChild(1,"首先确认您的手机系统版本：\n" +
                        "安卓手机须在4.3以上； \n" +
                        "苹果手机须在7.1.2以上；\n" +
                        "打开头盔，确认绿色电源指示灯常亮，头盔前部蓝色指示灯闪烁；\n" +
                        "打开APP，切换至“光照”模块，会自动搜索头盔，出现ihelmet，点击连接；\n" +
                        "如反复搜索不到，请重新启动手机和头盔做再次尝试或卸载APP重新下载安装。");
                addChild(2,"由于头盔与APP之间采用无线信号连接，受距离，阻隔等环境影响较大，因此会存在断开连接，数据不能及时刷新的情况。" +
                        "但是只要在一开始连接后开始光照，头盔就会自动完成光照并保存全部的光照记录，在下一次与手机连接时自动上传至后台服务器保存。" +
                        "无需一直保持手机在头盔旁边。");
                addChild(3,"点击光照界面下方治疗进度条区域，即可查看当月光照记录及总光照次数。通过左右滑动，可以查看过往所有月份的光照记录。该功能与您的手机网络状态有关系，可能会有记录不准确的情况发生。");
                addChild(4,"由于各个手机厂家的定制系统差异，安全软件拦截以及网络服务提供商的服务设置等问题，我们不能保证所有机型的手机都能够准确的收到提醒推送通知。");
                break;
            case 2:
                tv_group_name.setText("头盔使用相关");
                addGroup("1.为什么每次光照时间长度会变的？");
                addGroup("2.每天用可以么？");
                addGroup("3.一年后就不需要使用了么？");
                addGroup("4.充一次电用多久？充满电能用几次？");
                addGroup("5.头盔声音可以变小或者关闭么？");
                addGroup("6.使用头盔很热，会流汗？");
                addChild(0,"激光的功率受环境温度影响较大，温度越高，功率就越低，冬夏季可以相差30%以上。" +
                        "简单来说，激光能量=激光功率x照射时间，要保证治疗效果，就要保证激光能量输出恒定，当激光功率下降时，" +
                        "就要增加照射时间补偿，当激光功率提高时，就要减少照射时间。因此i黑密内置高精度传感器，每次开机检测环境温度，" +
                        "自动决定光照时长，保证输出能量恒定。这是i黑密相对传统激光生发产品独特的优势之一。");
                addChild(1,"如头发特别厚，无法有效拨开接受光照，则需要加大照射频率至一天一次。但最多一天只能使用一次。");
                addChild(2,"在达到最佳生发效果后，依然需要每周使用两次进行保持，既然已经养成了习惯，就像刷牙一样成为您生活的一部分，呵护好您的头发。");
                addChild(3,"充电时间大约需要3-4小时，充满电后可以用2-3次。");
                addChild(4,"在APP连接头盔后，光照界面右上角会出现喇叭标志，点击可以打开/关闭头盔声音提示，头盔声音大小不能改变。");
                addChild(5,"激光照射附带有热效应，会加速头部血液循环，从而刺激头发的生长。如使用环境温度超过26度，这种热效应也会引起头皮汗腺的分泌，" +
                        "尤其夏天，导致汗液增多，这是正常现象，不必担心。因此建议在空调环境下使用更舒适。");
                break;
            case 3:
                tv_group_name.setText("安全性");
                addGroup("1.什么人不能用？");
                addGroup("2.什么人适合用？");
                addGroup("3.戴在头上会对大脑有影响吗？有没有其他隐藏的风险？");
                addGroup("4.产品有什么认证？");
                addChild(0,"未成年人，黑色人种，孕妇，癫痫，光敏感，头部有伤口以及头部有其他皮肤类疾病者不适合使用。哺乳期女性，使用时要避免照射到婴儿眼睛。");
                addChild(1,"患有雄性激素性脱发，植发后的恢复和保持，压力性脱发、产后脱发，化疗后脱发，斑秃（在头盔光照范围内）" +
                        "对于非病理性脱发，例如头发先天发质不佳，头发营养不良，精神压力过大等造成的脱发，也非常合适的。");
                addChild(2,"本产品安全无副作用，通过了国际医疗安全标准IEC60601-1和家用医疗安全标准IEC60601-1-11的严格测试。它使用的是功率为650nm，5mw的低能量激光，" +
                        "激光能量仅能到达头皮的真皮层，不会穿透真皮层，更到达不了头骨。经过广泛临床实验，激光头盔可以加速头皮血液循环，改善头皮的血液供应，" +
                        "有利于舒缓紧张的大脑头皮。迄今为止，国内外的临床应用未发现一例副作用的报告。");
                addChild(3,"产品已整体通过欧盟CE认证和FDA认证检测，所有检测报告都是委托国际三大权威独立检测机构SGS出具的，包括IEC60601-1（医疗设备系统通用安全标准）" +
                        "、IEC60601-1-2（医疗设备电磁兼容性标准）、IEC60825-1（医疗激光标准）、IEC60601-1-11B（家用医疗设备安全与性能要求标准）、" +
                        "全部按照最严格的标准进行测试认证的。");
                break;
            case 4:
                tv_group_name.setText("服务");
                addGroup("1.什么是定制的光照方案？");
                addGroup("2.怎么感觉不到定制的光照方案？");
                addGroup("3.什么是随诊服务？");
                addGroup("4.产品有什么保修政策？");
                addGroup("5.什么是黑密君？");
                addChild(0,"我们的头盔有200颗高密度激光器，被分为了7片区域，每片区域都可以单独调节光照强度。因此可以为每个用户定制个性化的光照方案，" +
                        "在脱发部位加强光照，不脱发部位减弱，大大提升光照的效果，精准治疗。");
                addChild(1,"当定制的光照方案发回用户APP后，可以在专家建议中查看。此时再次连接头盔，光照模式相对于之前的体验模式会有变化，" +
                        "从外部蓝色半透明罩壳可以看出激光灯的闪烁方式有变化，同时光照时间也会由20分钟固定时间长度变为25-35分钟变化时长。" +
                        "在光照界面上也会出现一个脱发级别的标志替换掉原先的体验模式（用拉丁字母表示），代表属于此种脱发模式的光照方案。");
                addChild(2,"用户在购买产品后需要通过手机APP注册，APP注册，完成扫码绑定专家服务，上传自身报告及照片，我们的专家在后台进行诊断并给出定制的光照方案，" +
                        "在随后的治疗期间，用户还需要每90天上传新的报告反馈和照片。专家根据情况变化会提供指导意见，也可能会调整治疗方案，" +
                        "这就是随诊服务，目前是一共5次免费的专家服务，覆盖用户一整年的跟踪服务。");
                addChild(3,"i黑密提供三年质保服务，非人为质量问题，提供免费保修。");
                addChild(4,"黑密君是我们的微信专家服务号，提供一对一的产品售后支持与咨询服务。");
                break;
        }
    }

    private void initViews(){
        tv_group_name = (TextView) findViewById(R.id.tv_group_name);
        el_ques = (ExpandableListView) findViewById(R.id.el_ques);
        ImageButton ib_question_detail_back = (ImageButton) findViewById(R.id.ib_question_detail_back);
        if (ib_question_detail_back != null){
            ib_question_detail_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (el_ques != null){
            el_ques.setAdapter(new MyExpandAdapter(this, group_head, child));
            el_ques.setCacheColorHint(0);
            if (item_number != -1){
                initContent(item_number);
            }
        }
    }

    //el的箭头移到右边去
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            el_ques.setIndicatorBounds(widthPixels-40, widthPixels);
        } else {
            el_ques.setIndicatorBoundsRelative(widthPixels-40, widthPixels);
        }
    }

    //添加组列表项
    public void addGroup(String group){
        group_head.add(group);
        child.add(new ArrayList<String>()); //child中添加新数组
    }

    //添加对应组的自列表项
    public void addChild(int position,String child){
        List<String> it = this.child.get(position);
        if(it != null){
            it.add(child);
        }else{
            it = new ArrayList<>();
            it.add(child);
        }
    }

    //子列表项被选中的响应方法
    public void childSelect(int groupPosition,int childPosition){
        /*Toast.makeText(getBaseContext(), Integer.toString(groupPosition)+":" +
                Integer.toString(childPosition), Toast.LENGTH_SHORT).show();*/
    }
}
