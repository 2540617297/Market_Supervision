package com.example.constant;

public class PageInfo {
    private int nowpage;
    private int page;
    private int startpage;
    private int endpage;
    private int allpage;

    public int getAllpage() {
        return allpage;
    }

    public void setAllpage(int allpage) {
        this.allpage = allpage;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "nowpage=" + nowpage +
                ", page=" + page +
                ", startpage=" + startpage +
                ", endpage=" + endpage +
                '}';
    }

    public int getNowpage() {
        return nowpage;
    }

    public void setNowpage(int nowpage) {
        this.nowpage = nowpage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStartpage() {
        return startpage;
    }

    public void setStartpage(int startpage) {
        this.startpage = startpage;
    }

    public int getEndpage() {
        return endpage;
    }

    public void setEndpage(int endpage) {
        this.endpage = endpage;
    }
}
