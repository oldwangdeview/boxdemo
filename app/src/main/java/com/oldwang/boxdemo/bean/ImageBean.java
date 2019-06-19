package com.oldwang.boxdemo.bean;

import android.net.Uri;

import java.io.Serializable;


public class ImageBean implements Serializable {

    private boolean isDetele;
    private Uri uri;


    public ImageBean() {
    }

    public ImageBean(boolean isDetele, Uri uri) {
        this.isDetele = isDetele;
        this.uri = uri;
    }

    public boolean isDetele() {
        return isDetele;
    }

    public Uri getUri() {
        return uri;
    }
}
