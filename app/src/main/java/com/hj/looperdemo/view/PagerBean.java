package com.hj.looperdemo.view;

public class PagerBean {
    private String title;
    private Integer picResId;

    public PagerBean(String title, Integer picResId) {
        this.title = title;
        this.picResId = picResId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPicResId() {
        return picResId;
    }

    public void setPicResId(Integer picResId) {
        this.picResId = picResId;
    }
}
