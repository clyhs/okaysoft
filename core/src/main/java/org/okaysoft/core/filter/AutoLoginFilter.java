

package org.okaysoft.core.filter;


import java.io.IOException;
import java.util.Collection;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.security.service.SpringSecurityService;
import org.okaysoft.core.security.service.UserDetailsServiceImpl;
import org.okaysoft.core.security.service.UserHolder;
import org.okaysoft.core.service.PropertyHolder;
import org.okaysoft.core.utils.SpringContextUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class AutoLoginFilter implements Filter {
    protected static final OkayLogger log = new OkayLogger(AutoLoginFilter.class);
    
    private UserDetailsServiceImpl userDetailsServiceImpl;
    private boolean enabled = false;
    private String defaultUserName;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        if (enabled && !UserHolder.hasLogin()) {
            if (userDetailsServiceImpl == null) {
                userDetailsServiceImpl = SpringContextUtils.getBean("userDetailsServiceImpl");
            }
            if (userDetailsServiceImpl != null) {
                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(defaultUserName);

                UserHolder.saveUserDetailsToContext(userDetails, (HttpServletRequest) request);
                Collection<GrantedAuthority> auth=(Collection<GrantedAuthority>) userDetails.getAuthorities();
                for(GrantedAuthority au : auth){
                    log.info("\t"+au.getAuthority());
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig fc) throws ServletException {
        log.info("初始化自动登录过滤器(Initialize the automatic login filter)");
        enabled = !SpringSecurityService.isSecurity();
        defaultUserName = PropertyHolder.getProperty("auto.login.username");
        if(enabled){
            log.info("启用自动登录过滤器(Enable automatic login filter)");
        }else{            
            log.info("禁用自动登录过滤器(Disable automatic login filter)");
        }
    }

    @Override
    public void destroy() {
        log.info("销毁自动登录过滤器(Destroy the automatic login filter)");
    }
}