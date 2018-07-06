package com.caimatech.riskcontrol.constants;

import com.caimatech.riskcontrol.config.EnvironmentConfig;
import com.caimatech.riskcontrol.config.EnvironmentEnum;

public class SystemConstants {

    public static String CAIMA_H5_SIGN_KEY = "68352e79616e6d616368696e612e636f6d";
    
    public static String CAIMA_APP_SIGN_KEY = "68352e79616e6d616368696e612e636f6d";
    
    public static final int MAX_UPLOAD_SIZE = 10*1024*1024;

    public static final int THRESHOLD_SIZE = 1024*1024;
    
    public static final String TEMP_ICON_UPLOAD_DIRECTORY ="/static/loanmarket/Uploads/temp"; //设置临时上传路径
    
    public static final String REAL_ICON_UPLOAD_DIRECTORY = "/static/loanmarket/icon";//设置上传路径
    
    public static String AES_KEY = "1234567812345678";
    
    static{
        if(EnvironmentConfig.env == EnvironmentEnum.PRO){
            CAIMA_H5_SIGN_KEY = "68352e6361696d61746563682e636f6d";
            AES_KEY = "6361696d61746563";
        }
    }
}
