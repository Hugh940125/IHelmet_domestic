package cn.aigestudio.datepicker.bizs.themes;

/**
 * 主题的默认实现类
 * 
 * The default implement of theme
 *
 * @author AigeStudio 2015-06-17
 */
public class DPBaseTheme extends DPTheme {
    @Override
    public int colorBG() {
        return 0xFFFFFFFF;
    }

    @Override
    public int colorBGCircle() {
        return 0x44000000;
    }

    @Override
    public int colorRecordToday() {
        return 0xFF00FF00;
    }

    //标题栏的背景色
    @Override
    public int colorTitleBG() {
        return 0xFF16A1D6;
    }

    @Override
    public int colorTitle() {
        return 0xEEFFFFFF;
    }

    //今天的背景色
    @Override
    public int colorToday() {
        return 0xEE16A1D6;
    }

    @Override
    public int colorG() {
        return 0xEE333333;
    }

    @Override
    public int colorF() {
        return 0xEEC08AA4;
    }

    //周末数字的颜色
    @Override
    public int colorWeekend() {
        return 0xEEF08080;
    }

    @Override
    public int colorHoliday() {
        return 0x80FED6D6;
    }
}
