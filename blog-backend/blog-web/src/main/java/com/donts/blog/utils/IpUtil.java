package com.donts.blog.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.donts.exception.ShouldBeCheckedException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import static com.donts.consts.CommonConstant.UNKNOWN;

@Component
@Slf4j
public class IpUtil {
    @Value("${ip2region.db-path}")
    private String dbPath;

    private Searcher memorySearcher;

    public String getIpSource(String ipAddress) {
        if (ipAddress == null || !isIpAddress(ipAddress)) {
            log.error("Error: Invalid ip address");
            return "";
        }
        try {
            return memorySearcher.search(ipAddress);
        } catch (Exception e) {
            log.error("getCityInfo exception:", e);
        }
        return "";
    }

    public String getIpProvince(String ipSource) {
        if (StringUtils.isBlank(ipSource)) {
            return UNKNOWN;
        }
        String[] strings = ipSource.split("\\|");
        if (strings.length > 1 && strings[1].endsWith("省")) {
            return StringUtils.substringBefore(strings[1], "省");
        }
        return strings[0];
    }

    public UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader("User-Agent"));
    }

    public String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Real-IP");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("x-forwarded-for");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (NetUtil.LOCAL_IP.equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                //取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getIpAddress exception:", e);
                }
                assert inet != null;
                ipAddress = inet.getHostAddress();
            }
        }
        return StringUtils.substringBefore(ipAddress, ",");
    }

    private Boolean isIpAddress(String ipAddress) {
        String[] p = ipAddress.split("\\.");
        if (p.length != 4) {
            return false;
        } else {
            int var3 = p.length;

            for (String pp : p) {
                if (pp.length() > 3) {
                    return false;
                }

                int val = Integer.parseInt(pp);
                if (val > 255) {
                    return false;
                }
            }
            return true;
        }
    }

    @PostConstruct
    private void initIp2regionResource() throws ShouldBeCheckedException {

        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[] cBuff;
        try {
            if (dbPath.startsWith("classpath:")) {
                String path = dbPath.substring("classpath:".length());
                URL resourceUrl = getClass().getClassLoader().getResource(path);
                if (resourceUrl == null) {
                    throw new ShouldBeCheckedException("resource not found: " + path);
                }
                dbPath = resourceUrl.getPath();
            }
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            log.error("failed to load ip2region db file", e);
            //中断启动
            throw new ShouldBeCheckedException("failed to load ip2region db file");
        }

        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        try {
            memorySearcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            log.error("failed to create searcher", e);
            //中断启动
            throw new ShouldBeCheckedException("failed to create searcher");
        }

    }

    @PreDestroy
    private void destroy() throws IOException {
        // 4、关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
        memorySearcher.close();
    }

}
