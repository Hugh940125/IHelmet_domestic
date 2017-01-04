package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;

import com.slinph.ihelmet.ihelmet_domestic.internet.model.UploadBack;
import java.util.List;
/**
 * Created by Administrator on 2016/9/26.
 */
public class UploadBackVO extends UploadBack {
    private List<UploadBack> uploadBacks;

    public void setUploadBacks(List<UploadBack> uploadBacks){
        this.uploadBacks=uploadBacks;
    }

    public List<UploadBack> uploadBacks(){
        return uploadBacks;
    }
}
