package com.caimatech.riskcontrol.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;


public class RequestFilter extends OncePerRequestFilter {

    protected static Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String uri = request.getRequestURI();
        String ip = getIpAddr(request);
        boolean isAjaxRequest = isAjaxRequest(request);
        
        String signData = request.getHeader("X-Sign-Data");
        String timeStamp = request.getHeader("X-Timestamp");
        String requestSource = request.getHeader("X-Request-Source");
        String userAgent = request.getHeader("User-Agent");
        
        
        logger.info("RequestFilter uri = {} isAjaxRequest  = {} ip = {} requestSource = {} signData = {} timeStamp = {} userAgent = {} "
                ,uri,isAjaxRequest,ip,requestSource,signData,timeStamp,userAgent);

        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setHeader("Access-Control-Allow-Origin","*");
        resp.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");
        resp.setHeader("Access-Control-Max-Age","3600");
        resp.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        resp.setHeader("","");response.setHeader("","");
        filterChain.doFilter(request, resp);
        
    }
    
    protected String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {
                        logger.error("UnknownHostException e = {}",e);
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    }

    protected static boolean isAjaxRequest(HttpServletRequest request)  {  
        String header = request.getHeader("X-Requested-With");   
        if (header != null && "XMLHttpRequest".equals(header))   
           return true;
        else   
            return false;
    }
    
}
