package com.oldwang.boxdemo.interfice;

import com.oldwang.boxdemo.adpater.ChoicemoreAdpater;

import java.util.List;

public interface ChoicePathReturnLister<T> {

        public void getChoicedata(T Objetc, int clickposition, ChoicemoreAdpater dapater);

}
