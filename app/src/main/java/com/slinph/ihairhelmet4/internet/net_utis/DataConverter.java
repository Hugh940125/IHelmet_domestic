package com.slinph.ihairhelmet4.internet.net_utis;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.slinph.ihairhelmet4.R;

/**
 * Created by Administrator on 2016/9/14.
 *
 */
public class DataConverter {

    public static String getHairLossDegree(int degreee){
        String HairLossDegree =null;
        switch (degreee){
            case 0:
                HairLossDegree = "I型";
                break;
            case 1:
                HairLossDegree = "II型";
                break;
            case 2:
                HairLossDegree = "IIa型";
                break;
            case 3:
                HairLossDegree = "III型";
                break;
            case 4:
                HairLossDegree = "IIIa型";
                break;
            case 5:
                HairLossDegree = "IIIvertex型";
                break;
            case 6:
                HairLossDegree = "IV型";
                break;
            case 7:
                HairLossDegree = "IVa型";
                break;
            case 8:
                HairLossDegree = "V型";
                break;
            case 9:
                HairLossDegree = "Va型";
                break;
            case 10:
                HairLossDegree = "VI型";
                break;
            case 11:
                HairLossDegree = "VII型";
                break;
            case 12:
                HairLossDegree = "弥漫性脱发";
                break;
            case 13:
                HairLossDegree = "I-1型";
                break;
            case 14:
                HairLossDegree = "I-2型";
                break;
            case 15:
                HairLossDegree = "I-3型";
                break;
            case 16:
                HairLossDegree = "I-4型";
                break;
            case 17:
                HairLossDegree = "II-1型";
                break;
            case 18:
                HairLossDegree = "II-2型";
                break;
            case 19:
                HairLossDegree = "III型";
                break;
            case 20:
                HairLossDegree = "Frontal型";
                break;
            case 21:
                HairLossDegree = "Advanced型";
                break;
            case 22:
                HairLossDegree = "弥漫性脱发";
                break;
        }
        return HairLossDegree;
    }

    public static String getDisease(int disease){
        String Disease = null;
        switch (disease){
            case 0:
                Disease = "无";
                break;
            case 1:
                Disease = "头皮红疹";
                break;
            case 2:
                Disease = "头皮炎症";
                break;
            case 3:
                Disease = "头皮伴有脓包";
                break;
        }
        return Disease;
    }

    public static String getType(int type){
        String Type = null;
        switch (type){
            case 0:
                Type = "男性型脱发（雄激素依赖性脱发）";
                break;
            case 1:
                Type = "女性雄激素性脱发";
                break;
            case 2:
                Type = "产后脱发";
                break;
            case 3:
                Type = "内分泌源型弥漫性脱发";
                break;
            case 4:
                Type = "休止期脱发";
                break;
            case 5:
                Type = "红斑狼疮性脱发";
                break;
            case 6:
                Type = "减肥引起脱发";
                break;
            case 7:
                Type = "拔毛癖";
                break;
            case 8:
                Type = "斑秃";
                break;
        }
        return Type;
    }

    public static AlertDialog showDialog(Context context,String tip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        View watting = View.inflate(context, R.layout.transitiondialog, null);
        TextView tv_progressdialog = (TextView) watting.findViewById(R.id.tv_progressdialog);
        tv_progressdialog.setText(tip);
        builder.setView(watting);
        return builder.show();
    }
}
