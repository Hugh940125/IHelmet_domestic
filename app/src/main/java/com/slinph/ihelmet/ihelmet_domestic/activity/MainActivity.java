package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.junkchen.blelib.BleService;
import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.application.MyApplication;
import com.slinph.ihelmet.ihelmet_domestic.bluetooth.BlueToothInstructUtils;
import com.slinph.ihelmet.ihelmet_domestic.bluetooth.BlueToothManager;
import com.slinph.ihelmet.ihelmet_domestic.bluetooth.MyGattAttributes;
import com.slinph.ihelmet.ihelmet_domestic.fragment.AboutMeFragment;
import com.slinph.ihelmet.ihelmet_domestic.fragment.EvaluationFragment;
import com.slinph.ihelmet.ihelmet_domestic.fragment.QuestionFragment;
import com.slinph.ihelmet.ihelmet_domestic.fragment.RemindFragment;
import com.slinph.ihelmet.ihelmet_domestic.fragment.ReportFregment;
import com.slinph.ihelmet.ihelmet_domestic.fragment.TreatFragment;
import com.slinph.ihelmet.ihelmet_domestic.internet.Url;
import com.slinph.ihelmet.ihelmet_domestic.internet.model.BindInfo;
import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.FollowUpVO;
import com.slinph.ihelmet.ihelmet_domestic.internet.getData.LoadData;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.HttpUtils;
import com.slinph.ihelmet.ihelmet_domestic.internet.net_utis.ResultData;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesUtils;
import com.slinph.ihelmet.ihelmet_domestic.utils.StaticVariables;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> adapter;
    private ListView leftDrawer;
    private final String[] items = new String[]{MyApplication.getContext().getResources().getString(R.string.side_title_treatment), MyApplication.getContext().getResources().getString(R.string.side_title_report), MyApplication.getContext().getResources().getString(R.string.side_title_remind),
            MyApplication.getContext().getResources().getString(R.string.side_title_evaluation), MyApplication.getContext().getResources().getString(R.string.side_title_me),MyApplication.getContext().getResources().getString(R.string.question)};
    private ActionBarDrawerToggle mDrawerToggle;
    private FrameLayout fl_main;
    private FragmentManager mFragmentMan;
    private Fragment mContent;
    private Toolbar toolbar;
    private MainActivity mActivity;

    private static final String TAG = "MainActivity";

    private TreatFragment treatFragment;
    private RemindFragment remindFragment;
    private EvaluationFragment evaluationFragment;
    private AboutMeFragment aboutMeFragment;

    public static BlueToothManager mBlueToothManager;
    private Set<String> mDevices = new HashSet();
    private List<String> mDevicesName = new ArrayList();
    private Map<String, String> mDeviceMAC = new HashMap();
    private int CurrentFragment;


    private List<Map<String, Object>> deviceList;
    private List<String> serviceList;
    private List<String[]> characteristicList;
    private ProgressDialog progressDialog;

    private Boolean connstate;
    private String if_address;
    private String if_name;
    private ListView lv_scanresult;

    private BluetoothGattCharacteristic character;
    private int pro;
    private String charUuid;
    private BluetoothGattCharacteristic TransferCharacter;
    private BleService mBleService;

    //Constant
    public static final int SERVICE_BIND = 1;
    public static final int SERVICE_SHOW = 2;
    public static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    public static final int SCAN_FINISH = 3;
    public static final int RE_CONNECT = 4;
    private AlertDialog.Builder result_dialog;
    private List<BluetoothGattService> gattServiceList;
    private LinkedList<String> commands;
    private String recieve;
    private String[] datas;
    public static boolean isConnected;
    private AlertDialog alertDialog;
    private String coonAddr;
    private String last;
    private ReportFregment reportFregment;
    private QuestionFragment questionFragment;
    private Runnable reconn_runnable;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XGPushManager.registerPush(this, StaticVariables.ID, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.e("tag", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("tag", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //启动标识
        StaticVariables.IS_MAINACTIVITY_LUNCHED = true;
        mBlueToothManager = new BlueToothManager(this);
        MyApplication.mainActivity = this;//初始化MyApplication.mainActivity
        //初始化Fragment
        InitFragment();
        mFragmentMan = getFragmentManager();
        //布局和事件的初始化
        Initview();
        InnitEevent();
        //添加toobar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null){
            toolbar.setTitle(R.string.treatment);
        }
        setSupportActionBar(toolbar);
        //toolbar.inflateMenu(R.menu.menu_evaluation);
        toolbar.setNavigationIcon(R.drawable.main_menu);

        toolbar.setOnMenuItemClickListener(this);
        //控制侧边栏的展开和收起
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        // 通过代码：根据重力方向打开指定抽屉
        //mDrawerLayout.openDrawer(Gravity.LEFT);
        // 设置抽屉阴影
        // drawerLayout.setDrawerShadow(R.drawable.ic_launcher, Gravity.LEFT);
        // 设置抽屉空余处颜色
        mDrawerLayout.setScrimColor(Color.argb(200, 200, 200, 200));
        // 设置抽屉锁定模式 LOCK_MODE_LOCKED_OPEN:锁定 无法滑动； 只能通过代码取消锁定
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

        //下面是蓝牙相关的
        setFinishOnTouchOutside(false);
        deviceList = new ArrayList<>();
        serviceList = new ArrayList<>();
        registerReceiver(bleReceiver, makeIntentFilter());
        //加载网络数据
        LoadData.isReportBack(this);
        LoadData.getReportList(this,"10");
        HttpUtils.postAsync(MainActivity.this, Url.rootUrl+"/iheimi/treatmentPrograms/selectUserEffect", new HttpUtils.ResultCallback<ResultData<List<FollowUpVO>>>() {
            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(mActivity, R.string.net_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<List<FollowUpVO>> response) {
                if (response.getSuccess()){
                    List<FollowUpVO> data = response.getData();
                    if (data != null){
                        for (int i = 0;i <data.size();i ++){
                            if (i == data.size() - 1){
                                StaticVariables.USER_HAIR_TOP_PHOTO_URL = data.get(i).getTopViewUrl();//头顶照片
                                StaticVariables.USER_HAIR_LINE_PHOTO_URL = data.get(i).getHairlineUrl();//发际线图片
                                StaticVariables.USER_HAIR_AFTER_PHOTO_URL = data.get(i).getAfterViewUrl();//后脑勺图片
                                StaticVariables.USER_HAIR_PARTIAL_PHOTO_URL = data.get(i).getPartialViewUrl();//侧面图片
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        XGPushClickedResult clickedResult = XGPushManager.onActivityStarted(this);
        if(clickedResult != null){
            String title = clickedResult.getTitle();
            Log.v("TPush", "title:" + title);
            String id = clickedResult.getMsgId() + "";
            Log.v("TPush", "id:" + id);
            String content = clickedResult.getContent();
            Log.v("TPush", "content:" + content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CurrentFragment == 0) {
            doBindService();
        }
    }

    protected void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
/*--------------------------------蓝牙部分的分隔线------------------------------*/
    private boolean mIsBind;
    /**
     * 服务连接的回调
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBleService = ((BleService.LocalBinder) service).getService();
            if (mBleService != null) mHandler.sendEmptyMessage(SERVICE_BIND);
            if (mBleService != null&&mBleService.initialize()) {
                if (mBleService.enableBluetooth(true)) {
                    verifyIfRequestPermission();
                    //Toast.makeText(MainActivity.this, R.string.bt_open, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, R.string.not_surport_bt, Toast.LENGTH_SHORT).show();
            }
            mIsBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBleService = null;
            mIsBind = false;
        }
    };

    /**
     * 打开搜索的dialog,对6.0进行特殊的处理
     */
    private void verifyIfRequestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i(TAG, "onCreate: checkSelfPermission");
            if (this.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onCreate: Android 6.0 动态申请权限");

                if (this.shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_CONTACTS)) {
                    Log.i(TAG, "*********onCreate: shouldShowRequestPermissionRationale**********");
                    Toast.makeText(this, R.string.location_for_bt, Toast.LENGTH_SHORT).show();
                } else {
                    this.requestPermissions(
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ACCESS_COARSE_LOCATION);
                }
            } else {
                showDialog(getResources().getString(R.string.scanning));
                mBleService.scanLeDevice(true);
            }
        } else {
            showDialog(getResources().getString(R.string.scanning));
            mBleService.scanLeDevice(true);
        }
    }

    /**
     * 显示Dialog
     */
    private void showDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    /**
     * 隐藏Dialog
     */
    private void dismissDialog() {
        if (progressDialog == null) return;
        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    protected void onDestroy() {
        //取消连接
        mBleService.disconnect();
        doUnBindService();
        unregisterReceiver(bleReceiver);
        unregisterReceiver(mBlueToothManager.mSearchDevicesReceiver);
        StaticVariables.IS_MAINACTIVITY_LUNCHED = false;
        //取消网络状态的广播接收
        super.onDestroy();
    }

    /**
     * 绑定服务
     */
    private void doBindService() {
        String lastdevice = SharePreferencesUtils.getString(MainActivity.this, "lastdevice", "");
        last = SharePreferencesUtils.getString(MainActivity.this, "last", lastdevice);
        if (!last.isEmpty()){
            Log.e("上次连接的设备的地址", last);
        }

        Intent serviceIntent = new Intent(this, BleService.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(serviceIntent);
    }

    /**
     * 解绑服务
     */
    private void doUnBindService() {
        if (mIsBind) {
            unbindService(serviceConnection);
            mBleService = null;
            mIsBind = false;
        }
    }

    private BroadcastReceiver bleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BleService.ACTION_BLUETOOTH_DEVICE)) {
                String tmpDevName = intent.getStringExtra("name");
                String tmpDevAddress = intent.getStringExtra("address");
                Log.i(TAG, "name: " + tmpDevName + ", address: " + tmpDevAddress);
                HashMap<String, Object> deviceMap = new HashMap<>();
                deviceMap.put("name", tmpDevName);
                deviceMap.put("address", tmpDevAddress);
                deviceMap.put("isConnect", false);
                deviceList.add(deviceMap);
            } else if (intent.getAction().equals(BleService.ACTION_GATT_CONNECTED)) {
                deviceList.get(0).put("isConnect", true);
                dismissDialog();
            } else if (intent.getAction().equals(BleService.ACTION_GATT_DISCONNECTED)) {
                deviceList.get(0).put("isConnect", false);
                /**
                 * 加个判断防止空指针崩溃
                 */
                if (serviceList!=null)
                {
                    serviceList.clear();
                }
                /**
                 * 加个判断防止空指针崩溃
                 */
                if (characteristicList!=null)
                {
                    characteristicList.clear();
                }
                dismissDialog();
            } else if (intent.getAction().equals(BleService.ACTION_SCAN_FINISHED)) {
                /**
                 * 扫描结束发消息
                 */
                Message message = mHandler.obtainMessage();
                message.what = SCAN_FINISH;
                mHandler.sendMessage(message);
                dismissDialog();
            }
            //收到写数据结束的广播
            else if(intent.getAction().equals(BleService.WRITE_COMPLETE))
            {
                String address = intent.getStringExtra("address");

                switch (Integer.valueOf(address))
                {
                    case 38:
                        //发送养护发指令
                        sendData("0000584D");
                        break;
                    case 32:
                        //弥漫性脱发
                        Log.e("弥漫性脱发",32+"");
                        sendData(treatFragment.second_part);
                        break;
                    case 34:
                        sendData(treatFragment.end_part);
                        break;
                    case 36:
                        sendData(treatFragment.secondPart);
                        break;
                    default:
                        sendData("5A010101360000584D");
                        break;
                }
            }else if (intent.getAction().equals("Sound_Button")){
                invalidateOptionsMenu();
            }
        }
    };

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_BLUETOOTH_DEVICE);
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_SCAN_FINISHED);
        intentFilter.addAction(BleService.WRITE_COMPLETE);
        intentFilter.addAction("Sound_Button");
        return intentFilter;
    }

    private double trycount;
    private Handler mHandler = new Handler() {

        private boolean isShowResult = true;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SERVICE_BIND:
                    setBleServiceListener();
                    break;
                case SERVICE_SHOW://获取到服务
                    //.makeText(MainActivity.this, R.string.acquire_ble_service, Toast.LENGTH_SHORT).show();
                    if (alertDialog != null){
                        alertDialog.dismiss();
                    }
                    break;
                case SCAN_FINISH://扫描结束
                    if (deviceList.size()>0){
                        for (int i = 0; i<deviceList.size();i++){
                            String address = (String) deviceList.get(i).get("address");
                            Log.e("这次搜索到的设备的地址",address);
                            if (address.equals(last)){
                                mBleService.connect(last);
                                isShowResult = false;
                                break;
                            }
                        }
                    }
                    if (isShowResult){
                        if (CurrentFragment == 0){
                            showResult();
                        }
                    }
                    break;
                case RE_CONNECT:
                        reconn_runnable = new Runnable() {
                            @Override
                                public void run() {
                                    if (trycount < 10){
                                        if (alertDialog != null){
                                            alertDialog.dismiss();
                                        }
                                        Log.e("reconn_runnable","正在尝试重新连接:"+ trycount);
                                        mBleService.scanLeDevice(true);
                                        trycount++;
                                        mHandler.postDelayed(this, 10000);
                                    }
                            }
                        };
                        mHandler.postDelayed(reconn_runnable, 10000);//每10秒执行一次runnable.
                    break;
            }
        }
    };

    /**
     * Dialog显示扫描的结果,Listview adapter没有进行优化,数据量不大
     */
    private void showResult()
    {
        result_dialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog = result_dialog.create();
        //View inflate = View.inflate(BleScanActivity.this,R.layout.item_device,null);
        View inflate = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_result, null);
        alertDialog.setView(inflate);
        alertDialog.setTitle(getString(R.string.chose_device));
        alertDialog.setIcon(R.mipmap.remind_logo);
        lv_scanresult = (ListView) inflate.findViewById(R.id.lv_scanresult);
        Button bt_ok = (Button) inflate.findViewById(R.id.bt_ok);
        final List<Map<String, Object>> ihelmetList = new ArrayList<>();

        for (int dex = 0;dex < deviceList.size();dex++){
            Map<String, Object> infMap = deviceList.get(dex);
            if_name = (String) infMap.get("name");
            if (if_name != null && if_name.startsWith("iHelmet")){
                ihelmetList.add(infMap);
            }
        }

        lv_scanresult.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                if (ihelmetList.size() != 0){
                    return ihelmetList.size();
                }else {
                    return 1;
                }
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
            public View getView(final int position, View convertView, ViewGroup parent) {
                //没有搜索到蓝牙设备的情况
                if (ihelmetList.size() == 0){
                    View notfound = View.inflate(MainActivity.this, R.layout.notfound, null);
                    return notfound;
                }else{
                    final Map<String, Object> infMap = ihelmetList.get(position);
                    if_name = (String) infMap.get("name");
                    if_address = (String) infMap.get("address");
                    connstate = (Boolean) infMap.get("isConnect");

                    View device_item = View.inflate(MainActivity.this, R.layout.item_device, null);
                    TextView txtv_name = (TextView) device_item.findViewById(R.id.txtv_name);
                    TextView txtv_address = (TextView) device_item.findViewById(R.id.txtv_address);
                    TextView txtv_connState = (TextView) device_item.findViewById(R.id.txtv_connState);
                    Button btn_connect = (Button) device_item.findViewById(R.id.btn_connect);
                    btn_connect.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            coonAddr = (String) ihelmetList.get(position).get("address");
                            String coonName = (String) ihelmetList.get(position).get("name");
                            mBleService.connect(coonAddr);

                            //Toast.makeText(MainActivity.this, coonAddr+coonName+""+position, Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    });

                    txtv_name.setText(if_name);
                    txtv_address.setText(if_address);
                    if(connstate)
                    {
                        txtv_connState.setText(getString(R.string.connected));
                    }
                    txtv_connState.setText(getString(R.string.discounected));
                    return device_item;
                }
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (!mBleService.isScanning()) {
                    verifyIfRequestPermission();
                    deviceList.clear();
                    mBleService.scanLeDevice(true);
                }
            }
        });
        alertDialog.show();
    }

    /**
     * 发送数据的方法
     */
    public void sendData(String data) {
        if (data != null){
        mBleService.writeCharacteristic(TransferCharacter,hexStringToByte(data));
        mBleService.setCharacteristicNotification(TransferCharacter, true);
        Log.e("单独发送",data+"--"+new Date().getTime());
        }
    }

    /**
     *十六进制数据转化为字节数组
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] chars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(chars[pos]) << 4 | toByte(chars[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * BLE的一系列监听
     */
    private void setBleServiceListener() {
        mBleService.setOnServicesDiscoveredListener(new BleService.OnServicesDiscoveredListener() {

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS) {

                    gattServiceList = gatt.getServices();
                    characteristicList = new ArrayList<>();
                    /// serviceList.clear();
                    for (BluetoothGattService service : gattServiceList)
                    {
                        String serviceUuid = service.getUuid().toString();
                        serviceList.add(MyGattAttributes.lookup(serviceUuid, "Unknown") + "\n" + serviceUuid);
                        Log.i(TAG, MyGattAttributes.lookup(serviceUuid, "Unknown") + "\n" + serviceUuid);

                        List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                        String[] charArra = new String[characteristics.size()];
                        for (int i = 0; i < characteristics.size(); i++) {
                            charUuid = characteristics.get(i).getUuid().toString();

                            character = characteristics.get(i);
                            pro = character.getProperties();
                            //Log.e("properties",charUuid+"---"+pro+"---"+characteristics.size());
                            //获取读写数据的Characteristic
                            if (((pro | BluetoothGattCharacteristic.PROPERTY_WRITE)>0)&&((pro | BluetoothGattCharacteristic.PROPERTY_NOTIFY)>0))
                            {
                                if("0000ffe1-0000-1000-8000-00805f9b34fb".equals(charUuid))
                                {
                                    TransferCharacter = character;
                                }
                            }
                            charArra[i] = MyGattAttributes.lookup(charUuid, "Unknown") + "\n" + charUuid;
                        }
                        characteristicList.add(charArra);
                    }
                    mHandler.sendEmptyMessage(SERVICE_SHOW);//获取服务以后给handler发消息
                    //此处调用了发送数据的方法
                    sendData("5A010101360000584D");//5A010101010000584D
                }
            }
        });

        /**
         * 设置数据监听
         */
        commands = new LinkedList<>();
        mBleService.setOnDataAvailableListener(new BleService.OnDataAvailableListener() {
            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                String comm;
                if (commands != null&& commands.size()>0) {
                    Log.e("Size", commands.size()+"_"+ commands.toString());
                    while ((comm = commands.poll())!=null)
                    {
                        treatFragment.disposeData(comm);
                        Log.e("处理",comm);
                    }
                }
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                byte[] value = characteristic.getValue();
                recieve = bin2hex(new String(value));

                if (!BlueToothInstructUtils.checkString(recieve)) {

                    Log.e("onCharacteristicChanged", recieve);

                    //不完整
                    datas = BlueToothInstructUtils.formatString(recieve);
                    if (datas != null && datas.length > 0) {//格式化后得到的数据
                        for (int i = 0; i < datas.length; i++) {//分别执行里面的执行
                          //  Log.e("拼接后", datas[i]);
                            if (datas[i] != null){
                                commands.offer(datas[i]);
                            }
                            gatt.readCharacteristic(characteristic);
                        }
                        datas = null;
                    }
                }
                else
                {
                    commands.offer(recieve);
                    gatt.readCharacteristic(characteristic);
                    Log.e("接收", recieve);
                }
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                byte[] value = descriptor.getValue();
                String s = value.toString();
                Log.e("onDescriptorRead",s);
            }

        });


        /**
         * 监听连接状态的改变
         */
        mBleService.setOnConnectListener(new BleService.OnConnectionStateChangeListener() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    //断开连接后将光照界面初始化
                    treatFragment.HUMIDITY_DEGREEE = "";
                    treatFragment.TEMPERATURE_VALUE = "";
                    treatFragment.LIGHT_STATE = getResources().getString(R.string.discounected);
                    //断开连接后将连接状态设置为false
                    isConnected = false;
                    treatFragment.TIME_LEFT = "";
                    treatFragment.BATTERAY_LEFT = 0;
                    treatFragment.NOWPROGRESS_VALUE = 0;
                    treatFragment.activity_handler.post(treatFragment);
                    TreatFragment.mIsLighting = false;
                    invalidateOptionsMenu();
                    Log.e("onConnectionStateChange","Ble连接已断开");
                    //断开连接以后尝试进行自动连接上次的设备
                        Message message = mHandler.obtainMessage();
                        message.what = RE_CONNECT;
                        mHandler.sendMessage(message);
                        trycount = 0;
                } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                    //Ble正在连接
                    Log.e("onConnectionStateChange","Ble正在连接");
                } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                    //Ble已连接
                    treatFragment.ChangeState();
                    //连接以后取消定时器
                    if (reconn_runnable != null){
                        mHandler.removeCallbacks(reconn_runnable);
                    }
                    if (alertDialog != null){
                        alertDialog.dismiss();
                    }

                    isConnected = true;
                    if (coonAddr != null){
                        SharePreferencesUtils.putString(MainActivity.this,"lastdevice",coonAddr);
                        SharePreferencesUtils.putString(MainActivity.this,"last",coonAddr);
                        Log.e("保存的地址",coonAddr);
                    }
                    treatFragment.LIGHT_STATE = getString(R.string.connected);
                    treatFragment.activity_handler.post(treatFragment);
                    Log.e("onConnectionStateChange","Ble已连接");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    //Ble正在断开连接
                    Log.e("onConnectionStateChange","Ble正在断开连接");
                }
            }
        });

        mBleService.setOnReadRemoteRssiListener(new BleService.OnReadRemoteRssiListener() {
            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                Log.i(TAG, "onReadRemoteRssi: rssi = " + rssi);
            }
        });
    }

    /**
     * 数据转换的方法
     */
/*   public static String bin2hex(String bin) {
        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }*/

    //将字符串编码成16进制数字,适用于所有字符（包括中文）
    private static String hexString="0123456789ABCDEF";
    public static String bin2hex(String str)
    {
        byte[] bytes=str.getBytes();
        StringBuilder sb=new StringBuilder(bytes.length*2);
        for(int i=0;i<bytes.length;i++)
        {
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
        }
        return sb.toString();
    }


/*--------------------------------蓝牙部分的分隔线------------------------------*/

    /**
     * 添加toolbar的菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_evaluation, menu);
        return true;
    }

    /**
     * 动态切换toolbar菜单的图标
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        switch (CurrentFragment){
            case 0:
                if (TreatFragment.mIsLighting){
                    if (StaticVariables.USER_EQUIPMENT_VOLUME == 0){
                        menu.findItem(R.id.action_sound).setVisible(false);
                        menu.findItem(R.id.action_camera).setVisible(false);
                        menu.findItem(R.id.action_reports).setVisible(false);
                        menu.findItem(R.id.action_nosound).setVisible(true);
                    } else{
                        menu.findItem(R.id.action_sound).setVisible(true);
                        menu.findItem(R.id.action_camera).setVisible(false);
                        menu.findItem(R.id.action_reports).setVisible(false);
                        menu.findItem(R.id.action_nosound).setVisible(false);
                    }
                }
                else{
                    menu.findItem(R.id.action_sound).setVisible(false);
                    menu.findItem(R.id.action_camera).setVisible(false);
                    menu.findItem(R.id.action_reports).setVisible(false);
                    menu.findItem(R.id.action_nosound).setVisible(false);
                }
                break;
            case 1:
                menu.findItem(R.id.action_sound).setVisible(false);
                menu.findItem(R.id.action_camera).setVisible(false);
                menu.findItem(R.id.action_reports).setVisible(true);
                menu.findItem(R.id.action_nosound).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.action_sound).setVisible(false);
                menu.findItem(R.id.action_camera).setVisible(false);
                menu.findItem(R.id.action_reports).setVisible(false);
                menu.findItem(R.id.action_nosound).setVisible(false);
                break;
            case 3:
                menu.findItem(R.id.action_sound).setVisible(false);
                menu.findItem(R.id.action_camera).setVisible(false);
                menu.findItem(R.id.action_reports).setVisible(false);
                menu.findItem(R.id.action_nosound).setVisible(false);
                break;
            case 4:
                menu.findItem(R.id.action_sound).setVisible(false);
                menu.findItem(R.id.action_camera).setVisible(false);
                menu.findItem(R.id.action_reports).setVisible(false);
                menu.findItem(R.id.action_nosound).setVisible(false);
                break;
            case 5:
                menu.findItem(R.id.action_sound).setVisible(false);
                menu.findItem(R.id.action_camera).setVisible(false);
                menu.findItem(R.id.action_reports).setVisible(false);
                menu.findItem(R.id.action_nosound).setVisible(false);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     *  menu的点击时间
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_camera:
                Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sound:
                MyApplication.mainActivity.sendData("5A01010225000000584D");
                StaticVariables.USER_EQUIPMENT_VOLUME = 0;
                invalidateOptionsMenu();
                SharePreferencesUtils.putInt(this,"volume",0);
                break;
            case R.id.action_reports:
                startActivity(new Intent(this, AllReportsActivity.class));
                break;
            case R.id.action_nosound:
                MyApplication.mainActivity.sendData("5A01010225010000584D");
                StaticVariables.USER_EQUIPMENT_VOLUME = 1;
                invalidateOptionsMenu();
                SharePreferencesUtils.putInt(this,"volume",1);
                break;
                }
        return true;
    }

    /**
     * 实例化fragment
     */
    private void InitFragment() {
        treatFragment = TreatFragment.newInstance();
        //modeFragment = new ModeFragment();
        reportFregment = new ReportFregment();
        remindFragment = new RemindFragment();
        evaluationFragment = new EvaluationFragment();
        aboutMeFragment = new AboutMeFragment();
        questionFragment = new QuestionFragment();
    }

    private void BindOrNot(final int type) {
        HttpUtils.postAsync(this, Url.rootUrl+"/iheimi/code/selectOne", new HttpUtils.ResultCallback<ResultData<List<BindInfo>>>(){

            @Override
            public void onError(int statusCode, Throwable error) {
                Toast.makeText(MainActivity.this, R.string.net_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultData<List<BindInfo>> response) {
                if (response.getSuccess()){
                    List<BindInfo> data = response.getData();
                    if (data.size()>0){
                        if (type == 1){
                            startActivity(new Intent(MainActivity.this,LoseHairInfoActivity.class));
                            finish();
                        }else if (type == 2){
                            swichcontent(mContent, reportFregment);
                            toolbar.setTitle(R.string.report);
                            CurrentFragment = 1;
                            mDrawerLayout.closeDrawers();
                            invalidateOptionsMenu();
                        }
                    }else {
                        Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
                        intent.putExtra("from",555);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 监听侧边栏的点击事件
     */
    private void InnitEevent() {
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    /*点击不同的菜单item给出不同的响应*/
                    case 0:
                        swichcontent(mContent, treatFragment);
                        toolbar.setTitle(R.string.treat);
                        CurrentFragment = 0;
                        //判断是否已经连接,未连接弹出搜索Dialog
                        if (!isConnected){
                            doUnBindService();
                            deviceList.clear();
                            doBindService();
                        }
                        invalidateOptionsMenu();
                        mDrawerLayout.closeDrawers();
                        break;
                    case 1:
                        if (!StaticVariables.IS_REPORT_EXIST&&!StaticVariables.IS_QUALIFIDE_EXIST){
                            BindOrNot(1);
                        }else if (!StaticVariables.IS_REPORT_EXIST||StaticVariables.IS_QUALIFIDE_EXIST){
                            BindOrNot(2);
                        } else {
                            swichcontent(mContent, reportFregment);
                            toolbar.setTitle(R.string.report);
                            CurrentFragment = 1;
                            mDrawerLayout.closeDrawers();
                            invalidateOptionsMenu();
                        }
                        break;
                    case 2:
                        swichcontent(mContent, remindFragment);
                        toolbar.setTitle(R.string.remind);
                        CurrentFragment = 2;
                        mDrawerLayout.closeDrawers();
                        invalidateOptionsMenu();
                        break;
                    case 3:
                        swichcontent(mContent, evaluationFragment);
                        toolbar.setTitle(R.string.evaluation);
                        CurrentFragment = 3;
                        mDrawerLayout.closeDrawers();
                        invalidateOptionsMenu();
                        break;
                    case 4:
                        swichcontent(mContent, aboutMeFragment);
                        toolbar.setTitle(R.string.about);
                        CurrentFragment = 4;
                        mDrawerLayout.closeDrawers();
                        invalidateOptionsMenu();
                        break;
                    case 5:
                        swichcontent(mContent, questionFragment);
                        toolbar.setTitle(R.string.question);
                        CurrentFragment = 5;
                        mDrawerLayout.closeDrawers();
                        invalidateOptionsMenu();
                        break;
                }
            }
        });
    }

    /**
     * 初始化页面
     */
    private void Initview() {
        fl_main = (FrameLayout) findViewById(R.id.fl_main);
        mContent = treatFragment;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawer = (ListView) findViewById(R.id.drawer_left);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // 设置背填充内容背景色
                TextView tView = (TextView) super.getView(position,
                        convertView, parent);
                tView.setTextColor(Color.BLACK);
                return super.getView(position, convertView, parent);
            }
        };
        leftDrawer.setAdapter(adapter);
        leftDrawer.setBackgroundColor(Color.WHITE);

        FragmentTransaction mFt = mFragmentMan.beginTransaction();
        mFt.add(R.id.fl_main, mContent);
        mFt.commit();
        CurrentFragment = 0;
    }

    /**
     * 切换不同的fragment,从from跳到to
     *
     * @param from
     * @param to
     */
    public void swichcontent(Fragment from, Fragment to) {
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = mFragmentMan.beginTransaction();
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fl_main, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    /**
     * 双击退出应用
     */
    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, R.string.double_press_exit, Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
            } else{//退出程序
            this.finish();
            System.exit(0);
            }
    }
}
