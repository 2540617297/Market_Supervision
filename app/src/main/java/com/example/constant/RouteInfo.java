package com.example.constant;

public class RouteInfo {
    private String routeId;
    private Double mLatitude;
    private Double mLongitude;
    private Float mHorizontalAccuracyMeters;
    private String address;
    private String addTime;
    private String addUser;
    private String userNameCN;
    private PageInfo pageInfo;
    private String search;

    @Override
    public String toString() {
        return "RouteInfo{" +
                "routeId='" + routeId + '\'' +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mHorizontalAccuracyMeters=" + mHorizontalAccuracyMeters +
                ", address='" + address + '\'' +
                ", addTime='" + addTime + '\'' +
                ", addUser='" + addUser + '\'' +
                ", userNameCN='" + userNameCN + '\'' +
                ", pageInfo=" + pageInfo +
                ", search='" + search + '\'' +
                '}';
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUserNameCN() {
        return userNameCN;
    }

    public void setUserNameCN(String userNameCN) {
        this.userNameCN = userNameCN;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getAddUser() {
        return addUser;
    }

    public void setAddUser(String addUser) {
        this.addUser = addUser;
    }



    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public Double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public Float getmHorizontalAccuracyMeters() {
        return mHorizontalAccuracyMeters;
    }

    public void setmHorizontalAccuracyMeters(Float mHorizontalAccuracyMeters) {
        this.mHorizontalAccuracyMeters = mHorizontalAccuracyMeters;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
