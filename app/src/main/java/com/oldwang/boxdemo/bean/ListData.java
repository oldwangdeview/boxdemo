package com.oldwang.boxdemo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListData<T> implements Serializable{

    private int currentPage;
    private int pageSize;
    private int total;
    private List<T> list = new ArrayList<>();

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
