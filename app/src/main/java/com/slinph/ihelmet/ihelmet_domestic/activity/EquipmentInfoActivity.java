package com.slinph.ihelmet.ihelmet_domestic.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.slinph.ihelmet.ihelmet_domestic.R;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesConfig;
import com.slinph.ihelmet.ihelmet_domestic.utils.SharePreferencesUtils;
import com.slinph.ihelmet.ihelmet_domestic.view.DeviceInfoItem;

public class EquipmentInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_device_back;
    private DeviceInfoItem dii_number;
    private DeviceInfoItem dii_hardware;
    private DeviceInfoItem dii_software;
    //private DeviceInfoItem dii_bt_version;
    private DeviceInfoItem dii_laser;
    private DeviceInfoItem dii_battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);

        FindViews();
        InitEvent$Data();
    }

    public void FindViews(){
        ib_device_back = (ImageButton) findViewById(R.id.ib_deviceinfo_back);
        dii_number = (DeviceInfoItem) findViewById(R.id.Dii_number);
        dii_hardware = (DeviceInfoItem) findViewById(R.id.Dii_hardware);
        dii_software = (DeviceInfoItem) findViewById(R.id.Dii_software);
        //dii_bt_version = (DeviceInfoItem) findViewById(R.id.Dii_bt_version);
        dii_laser = (DeviceInfoItem) findViewById(R.id.Dii_laser);
        dii_battery = (DeviceInfoItem) findViewById(R.id.Dii_battery);
    }

    public void InitEvent$Data(){
        if (ib_device_back != null){
            ib_device_back.setOnClickListener(this);
        }
        String DeviceNumber = SharePreferencesUtils.getString(this, SharePreferencesConfig.DRIVACE_NUMBER);
        String DeviceHardeware = SharePreferencesUtils.getString(this, SharePreferencesConfig.DRIVACE_HARDWARE);
        String DeviceSoftware = SharePreferencesUtils.getString(this, SharePreferencesConfig.DRIVACE_SOfTWARE);
        dii_number.setValue(DeviceNumber);
        dii_software.setValue(DeviceSoftware);
        dii_hardware.setValue(DeviceHardeware);
        dii_battery.setValue("3400mAh");
        //dii_bt_version.setValue("BLE");
        dii_laser.setValue("200颗");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_deviceinfo_back:
                finish();
                break;
        }
    }
}
