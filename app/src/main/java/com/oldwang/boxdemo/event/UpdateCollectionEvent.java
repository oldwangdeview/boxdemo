package com.oldwang.boxdemo.event;

public class UpdateCollectionEvent  {
    public int position = -1;
    public int updatatype = -1;// updatatype==1 全选 updatatype==2 删除选中
    public UpdateCollectionEvent(int position,int updatatype){
        this.position = position;
        this.updatatype = updatatype;
    }
}
