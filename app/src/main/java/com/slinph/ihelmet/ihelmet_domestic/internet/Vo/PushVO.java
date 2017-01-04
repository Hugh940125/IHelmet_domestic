package com.slinph.ihelmet.ihelmet_domestic.internet.Vo;

import com.slinph.ihelmet.ihelmet_domestic.internet.model.Push;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class PushVO extends Push{

        private List<Push> pushs;

        public void setPushs(List<Push> patients){
            this.pushs=patients;
        }

        public List<Push>  getPushs(){
            return pushs;
        }

}
