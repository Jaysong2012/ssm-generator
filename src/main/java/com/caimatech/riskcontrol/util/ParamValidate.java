package com.caimatech.riskcontrol.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;


/**
 * 参数校验工具类
 * @author Jaysong
 *
 */
public class ParamValidate {

    public static boolean mobileNoValidate(String mobileNo){
        if(StringUtils.isEmpty(mobileNo)){
            return false;
        }
        return mobileNo.matches("^[1][3,4,5,7,8]+\\d{9}");
    }
    
    public static boolean emailValidate(String email){
        if(StringUtils.isEmpty(email)){
            return false;
        }
        String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(email);
        return m.matches();
    }

    public static boolean ipValidate(String ip){
        if(StringUtils.isEmpty(ip)){
            return false;
        }
        String pattern = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(ip);
        return m.matches();
    }

    public static boolean phoneValidate(String phoneNo){
        if(StringUtils.isEmpty(phoneNo)){
            return false;
        }
        String pattern = "0?(13|14|15|17|18)[0-9]{9}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phoneNo);
        return m.matches();
    }

    public static boolean qqValidate(String qq){
        if(StringUtils.isEmpty(qq)){
            return false;
        }
        String pattern = "[1-9]([0-9]{5,11})";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(qq);
        return m.matches();
    }
    
    public static boolean idValidate(String idNo){
        if(StringUtils.isEmpty(idNo)){
            return false;
        }
        String pattern = "\\d{17}[\\d|x]|\\d{15}";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(idNo);
        return m.matches();
    }
    
    public static boolean linkValidate(String link){
        String pattern = "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(link);
        return m.matches();
    }
}
