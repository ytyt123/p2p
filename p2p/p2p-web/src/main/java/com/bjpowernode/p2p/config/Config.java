package com.bjpowernode.p2p.config;



public class Config {
    /**
     * 实名认证的key
     */
    private String realNameAppkey;
    /**
     * 实名认证的Url
     */
    private  String realNameUrl;

    public String getRealNameAppkey() {
        return realNameAppkey;
    }

    public void setRealNameAppkey(String realNameAppkey) {
        this.realNameAppkey = realNameAppkey;
    }

    public String getRealNameUrl() {
        return realNameUrl;
    }

    public void setRealNameUrl(String realNameUrl) {
        this.realNameUrl = realNameUrl;
    }
}
