package com.bjpowernode.p2p.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * 通过添加天数返回日期
     * @param date
     * @param days
     * @return
     */
    public static Date getDateByAddDays(Date date, Integer days) {
        //创建一个日期处理类对象
        Calendar instance = Calendar.getInstance();
        //设置当前日期处理类对象的时间值
        instance.setTime(date);
        //当前日期处理类上添加天数
        instance.add(Calendar.DATE,days);
        return instance.getTime();
    }

    /**
     * 通过添加月份获取日期值
     * @param date
     * @param month
     * @return
     */
    public static Date getDateByAddMonth(Date date, Integer month) {
        //创建一个日期处理类对象
        Calendar instance = Calendar.getInstance();
        //设置当前日期处理类对象的时间值
        instance.setTime(date);
        //当前日期处理类上添加天数
        instance.add(Calendar.MONTH,month);
        return instance.getTime();
    }

    /**
     * 获取时间戳
     * @return
     */
    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
}
