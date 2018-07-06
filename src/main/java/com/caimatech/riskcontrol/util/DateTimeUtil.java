package com.caimatech.riskcontrol.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateTimeUtil {

    /**
     * 获取当前精确到毫秒时间
     * @return yyyyMMddHHmmssSSS
     */
    public static String getMillisecondDateTime(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
    
    /**
     * 获取当前时间
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    
    public static boolean isSameDate(String date1 ,String date2 ,SimpleDateFormat df1 ,SimpleDateFormat df2,SimpleDateFormat fdf){
        return getyyyyMMddFormat(date1,df1,fdf).equals(getyyyyMMddFormat(date2,df2,fdf));
    }
    
    public static String getyyyyMMddFormat(String time ,SimpleDateFormat df,SimpleDateFormat fdf){
        //SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        
        Date date;
        try {
            date = df.parse(time);
            return fdf.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 获取当前时间
     * @return yyyyMMddHHmmss
     */
    public static String getRequestDateTime(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
    
    /**
     * 判断是否是同一天
     * @param time
     * @return
     * @throws ParseException 
     */
    public static boolean isToDay(String time) throws ParseException{
        if(StringUtils.isEmpty(time))return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        Date date = df.parse(time);
        String dayStr = dayformat.format(date);
        return dayStr.equals(dayformat.format(new Date()));
    }
    
    public static boolean hasOverDue(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if(date!=null){
            return date.getTime() < getValidityTime();
        }else{
            return false;
        }
    }
    
    @SuppressWarnings("static-access")
    public static long getValidityTime(){
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(new Date());//把当前时间赋给日历
        calendar.add(calendar.MINUTE ,-30);
        return calendar.getTimeInMillis();
    }

    public static String getMinuteDateTime() {
        return new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
    }
    
    public static String getDayFormat(String time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        
        Date date;
        try {
            date = df.parse(time);
            return dayformat.format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getBeforeDayDateTime(int year ,int month ,int day,SimpleDateFormat sdf){
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        if(year != 0){
            calendar.add(Calendar.YEAR, year);
        }
        if(month != 0){
            calendar.add(Calendar.MONTH, month);//设置为month月
        }
        if(day != 0){
            calendar.add(Calendar.DAY_OF_MONTH, day);
        }
        dBefore = calendar.getTime();

        return sdf.format(dBefore);
    }
    
    /** 
     * 判断某一时间是否在一个区间内 
     * @param sourceTime 
     *            时间区间,半闭合,如[10:00-20:00) 
     * @param curTime 
     *            需要判断的时间 如10:00 
     * @return  
     * @throws ParseException 
     * @throws IllegalArgumentException 
     */  
    public static boolean isInTime(String startTime, String endTime ,String curTime,SimpleDateFormat df){

        try{
            long now = df.parse(curTime).getTime();  
            long start = df.parse(startTime).getTime();  
            long end = df.parse(endTime).getTime();  
            if (end < start) {  
                if (now >= end && now < start) {  
                    return false;  
                } else {  
                    return true;  
                }  
            }   
            else {  
                if (now >= start && now < end) {  
                    return true;  
                } else {  
                    return false;  
                }  
            }
        }catch(Exception e){
            return false;
        }
      
    }

    public static Date parse(long timeStamp, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return new Date(timeStamp);
    }

    public static Date parse(String timeStamp, String format){
        long time = 0L;
        try {
            time = Long.parseLong(timeStamp);
        }catch (Exception e){
            e.printStackTrace();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return new Date(time);
    }
}
