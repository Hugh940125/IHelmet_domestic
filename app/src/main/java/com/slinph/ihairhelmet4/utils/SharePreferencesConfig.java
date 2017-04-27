package com.slinph.ihairhelmet4.utils;

/**
 * Created by Waiting on 2016/1/23.
 */
public class SharePreferencesConfig {
    /**
     * 保存最后一次连接的设备MAC
     */
    public static final String SP_CONNED_MAC ="conned_mac";
    /**
     * 保存最后一次连接的设备名
     */
    public static final String SP_CONNED_NAME ="conned_name";
    /**
     * 保存登陆过用户的id
     */
    public static final String SP_USER_ID="userId";
    /**
     * 保存登陆过用户的手机号
     */
    public static final String SP_USER_PHONE ="user_phone";
    /**
     * 用户病例
     */
    public static final String SP_USER_FOLLOWUP_ID ="USER_FOLLOWUP_ID";
    /**
     * 下次打开软件是否自动登录
     */
    public static final String SP_SAVE_USER="saveUser";
    /**
     * 病例id
     */
    public static final String SP_LIGHT_START_TIME="sp_light_start_time";

    /******以下是用户诊断报告数据*****/
//    public static final String SP_USER_REPORT_TIME="user_report_time";//报告时间
//    public static final String SP_USER_HAIR_LOSS_TYPE="user_hair_loss_type";//脱发类型
//    public static final String SP_USER_HAIR_LOSS_DEGREE="user_hair_loss_degree";//脱发等级
//    public static final String SP_USER_HAIR_LOSS_DISEASE="user_hair_loss_disease";//脱发伴随症状:如红疹,脓包等
//    public static final String SP_USER_EXPERT_SUGGEST="user_expert_suggest";//专家建议
//    public static final String SP_USER_SUGGEST_LIGHT_TIMES="user_suggest_light_times";//建议每月治疗次数
//    public static final String SP_USER_CLASSIFICATION_PHOTO="user_classification_photo";//分级图
//    public static final String SP_USER_ALL_TREATMENT_PROGRAMS="user_all_treatment_programs";//所有治疗阶段
//    public static final String SP_USER_CURRENT_TREATMENT_PROGRAMS="user_current_treatment_programs";//当前治疗阶段
//    public static final String SP_USER_NAME="user_name";
//    public static final String SP_USER_SEX="user_sex";
//    public static final String SP_USER_AGE="user_age";

    //推送时间保存
    public static final String ALARM_LIGHT_MILLIS ="alarm_light_millis";
    public static final String ALARM_LIGHT_HHMMSS ="alarm_light_hhmmss";
    public static final String ALARM_DRINK_MILLIS ="alarm_drink_millis";
    //推送时间保存
    public static final String ALARM_LIGHT_ID ="alarm_light_id";
    public static final String ALARM_DRINK_ID ="alarm_drink_id";
    //是否推送
    public static final String ALARM_LIGHT_STATE="alarm_light_state";
    public static final String ALARM_DRINK_STATE="alarm_drink_state";

    //SharedPreferences配置名称
    public static final String SharedPreferences_CONFIG="config";
    //SharedPreferences获取蓝牙设备名称key值
    public static final String DRIVACE_NAME="drivace_name";
    //SharedPreferences获取蓝牙设备硬件版本key值
    public static final String DRIVACE_HARDWARE="drivace_hardware";
    //SharedPreferences获取蓝牙设备软件版本key值
    public static final String DRIVACE_SOfTWARE="drivace_software";
    //SharedPreferences获取蓝牙设备软件版本key值
    public static final String DRIVACE_NUMBER="drivace_number";
    //设备治疗每次时间的key值
    public static final String LIGHT_DATE_MILLIS="light_date_millis";

}
