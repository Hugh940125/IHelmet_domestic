package com.slinph.ihairhelmet4.internet.Vo;

import com.slinph.ihairhelmet4.internet.model.Push;

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
