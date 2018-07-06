package com.mucfc.ftc.tenant.server.common;

import com.caimatech.riskcontrol.config.EnvironmentConfig;
import com.caimatech.riskcontrol.config.EnvironmentEnum;
import com.mucfc.ftc.aps.base.AppConfig;
import com.mucfc.ftc.aps.base.ServiceExecutor;
import com.mucfc.ftc.aps.base.ServiceRequest;
import com.mucfc.ftc.aps.base.ServiceResponse;
import com.mucfc.ftc.aps.sdk.exception.InvalidAppConfigException;
import com.mucfc.ftc.aps.sdk.exception.ServiceRequestException;
import com.mucfc.ftc.aps.sdk.security.SecureBizContent;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 金融云平台调用者类
 *
 * Created by huqingmiao on 2017-8-10.
 */
public class FtcCaller {
    private static final Logger log = LoggerFactory.getLogger(FtcCaller.class);

    // 平台服务地址
    private static final String PLATFORM_ADDRESS = "https://open.muftc.com/do/api/call";

    // 平台公钥
    private static String PLATFORM_PUB_KEY = "classpath:keys/platform_public_key.pem";

    // App私钥(环球金融)
    private static String APP_PRI_KEY = "classpath:keys/caimatech_private_key.pem";


    public static String tenantId = "10137";
    
    public static String appId = "10000012";
    
    // http代理
//    private static final String PROXY_ADDRESS = "119.90.63.3";
//    private static final int PROXY_PORT = 8123;
//    private static final String PROXY_TYPE = "http";

//    private static final String PROXY_ADDR = "110.73.3.149";
//    private static final int PROXY_PORT = 8123;
//    private static final String PROXY_TYPE = "http";

    static {
        if(EnvironmentConfig.env == EnvironmentEnum.DEBUG){
            PLATFORM_PUB_KEY = "classpath:keys/test_platform_public_key.pem";
            APP_PRI_KEY = "classpath:keys/test_private_key.pem";
            tenantId = "10010";
            appId = "10002";
        }
        try {
            configure();
        } catch (Exception e) {
            log.error("", e);
        }
    }


    public static void configure() throws InvalidAppConfigException {
        AppConfig appConfig = AppConfig.getInstance();

        //必要的话，可设置网络代理
//        appConfig.setProxyAddress(PROXY_ADDRESS);
//        appConfig.setProxyPort(PROXY_PORT);
//        appConfig.setProxyType(PROXY_TYPE);

        appConfig.setPlatformAddress(PLATFORM_ADDRESS);
        appConfig.setPlatformPublicKeyPath(PLATFORM_PUB_KEY);
        appConfig.setAppPrivateKeyPath(APP_PRI_KEY);
    }


    public static ServiceResponse callService(String appId, String serviceId, Map<String, String> bizContentMap, String callbackUrl)
            throws ServiceRequestException {
        ServiceRequest request = new ServiceRequest();
        request.setAppId(appId);

        // 服务接口ID
        request.setReqServiceId(serviceId);

        // 业务参数
        if (bizContentMap != null && !bizContentMap.isEmpty()) {
            request.setBizContent(bizContentMap);
        }

        // 加密算法
        request.setEncryptType(SecureBizContent.ENCRYPT_TYPE_RSA);
        request.setBase64(true);
        request.setEncryptResponseType(SecureBizContent.ENCRYPT_TYPE_RSA);
        request.setBase64Response(true);


        // 如果需要平台回调，则指定回递地址
        if(StringUtils.isNotEmpty(callbackUrl)){
            request.setCallbackUrl(callbackUrl);
        }

        ServiceResponse response = ServiceExecutor.execute(request);
        return response;
    }
}
