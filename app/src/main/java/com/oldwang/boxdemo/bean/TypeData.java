package com.oldwang.boxdemo.bean;

import java.io.Serializable;

public class TypeData implements Serializable{


    /**类型ID**/
    private String typeId;
    /**类型名称**/
    private String typeName;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
