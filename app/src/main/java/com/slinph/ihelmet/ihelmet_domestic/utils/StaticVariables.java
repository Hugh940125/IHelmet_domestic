package com.slinph.ihelmet.ihelmet_domestic.utils;

import com.slinph.ihelmet.ihelmet_domestic.internet.Vo.TreatmentProgramsVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 静态变量
 */
public class StaticVariables {

    public static int CHECK_TYPE = 0;
    public static long REGIS_TIME = -1L;
    public static boolean IS_QUALIFIDE_EXIST = false;
    public static boolean IS_REPORT_EXIST = false;
    public static String USER_PHONE = "";
    public static String PATIENT_ID ="";
    public static String MID = "";
    public static boolean IS_UPLOAD_INFO = false;

    public static String ID = "";
    /******
     * 以下是用户诊断报告数据
     *****/
    public static String USER_SEX = "";//性别
    public static String USER_SEX_INT = "";//性别
    public static String USER_AGE= "";//年龄
    public static String USER_REALNAME = "";//真实姓名
    public static String USER_NATION = "";//民族
    public static String USER_USERNAME = "";//用户名
    public static String USER_REPORT_TIME = "";//报告时间YYYY-MM-DD
    public static String USER_HAIR_LOSS_TYPE = "";//脱发类型
    public static String USER_HAIR_LOSS_DEGREE = "";//脱发等级
    public static String USER_HAIR_LOSS_DISEASE = "";//脱发伴随症状:如红疹,脓包等
    public static String USER_EXPERT_SUGGEST = "";//专家建议
    public static int USER_SUGGEST_LIGHT_TIMES = 0;//建议每月治疗次数
    public static String USER_CLASSIFICATION_PHOTO_URL = "";//分级图
    public static String[] USER_ALL_TREATMENT_PROGRAMS;//所有治疗阶段
    public static int USER_CURRENT_TREATMENT_PROGRAMS_INT = 0;//当前治疗阶段在总阶段位置
    public static boolean USER_IS_SHOW_WAIT = false;//是否显示等待报告

    public static String USER_HAIR_TOP_PHOTO_URL = "";//头顶照片
    public static String USER_HAIR_LINE_PHOTO_URL = "";//发际线图片
    public static String USER_HAIR_AFTER_PHOTO_URL = "";//后脑勺图片
    public static String USER_HAIR_PARTIAL_PHOTO_URL = "";//侧面图片

    public static Set<String> ALL_HISTORY_SET = new HashSet<>();//总光照次数
    public static Set<Integer> LIGHT_HISTORY_SET = new HashSet<>();//光照记录
    public static List<Integer> LIGHT_PLAN_LIST = new ArrayList<>();//光照计划
    public static int USER_EQUIPMENT_VOLUME = 0;//蓝牙音量:0:不显示,1:静音,2:开声音
    public static boolean IS_MAINACTIVITY_LUNCHED = false;//mainactivity是否已经启动
    public static List<String> PHOTOS_URL = new ArrayList<>();  //效果评估的图片
    public static List<TreatmentProgramsVO> REPORT_LIST = new ArrayList<>();
}
