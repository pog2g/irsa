package com.irsa.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Component
public class ConsoleFilter implements Filter {
    Logger log = LoggerFactory.getLogger(ConsoleFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long s = System.currentTimeMillis();
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        // 当前访问路径             
        String currentUrl = req.getRequestURI();
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Entry<String, String[]> e : parameterMap.entrySet()) {
            String[] valueArr = e.getValue();
            if (valueArr == null || valueArr.length == 0) {
                params.put(e.getKey(), "");
                continue;
            }
            if (valueArr.length == 1) {
                params.put(e.getKey(), valueArr[0]);
                continue;
            }
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < valueArr.length; i++) {
                String key = valueArr[i];
                if (i != valueArr.length - 1) {
                    sb.append(key).append(",");
                    continue;
                }
                sb.append(key);
            }
            params.put(e.getKey(), sb.toString());
        }
        log.info("[过滤器] [控制台] [{}] [{}] [{}] 参数：{}", new Object[]{req.getRemoteAddr(), req.getContentType() == null ? "" : req.getContentType(), currentUrl, params, parameterMap});

        if (currentUrl.indexOf("/res/") != -1 || currentUrl.indexOf("/self/") != -1 ||
                currentUrl.indexOf("login") != -1 || currentUrl.indexOf("logon") != -1 || currentUrl.indexOf("404") != -1) {
            chain.doFilter(request, response);
        } else {
            if (req.getSession().getAttribute("account") == null || "".equals(req.getSession().getAttribute("account").toString())) {
                StringBuffer domain = new StringBuffer().append(req.getScheme()).append("://").append(request.getServerName());
                if (request.getServerPort() != 80) {
                    domain.append(":").append(request.getServerPort()).append(req.getContextPath());
                }
                res.sendRedirect(domain.toString() + "/console/login.html");
            } else {
                chain.doFilter(request, response);
            }
        }
        long e = System.currentTimeMillis();
        log.info("[过滤器] [控制台] [{}] [{}] [{}] 耗时：{}(毫秒)...", new Object[]{req.getRemoteAddr(), req.getContentType() == null ? "" : req.getContentType(), currentUrl, e - s});
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }
}
