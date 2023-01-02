package com.leihao.wiki.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

public class PageRequest {
    @NotNull(message = "页码不能为空")
    private int pageNum;
    @NotNull(message = "每页条数不能为空")
    @Max(value = 999,message = "每条页数不能超过999条")
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
