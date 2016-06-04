package org.okaysoft.core.security.service;

import javax.servlet.http.HttpServletRequest;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.security.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class UserHolder {
    protected static final OkayLogger log = new OkayLogger(UserHolder.class);

    public static boolean hasLogin() {
        if (getCurrentLoginUser() == null) {
            return false;
        }
        return true;
    }

    public static String getCurrentUserLoginIp() {
        Authentication authentication = getAuthentication();

        if (authentication == null) {
            return "";
        }

        Object details = authentication.getDetails();
        if (!(details instanceof WebAuthenticationDetails)) {
            return "";
        }

        WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
        return webDetails.getRemoteAddress();
    }

    public static User getCurrentLoginUser() {
        Authentication authentication = getAuthentication();
        //log.info("auth");
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
            	User user = (User) principal;
            	//log.info("**********"+user.getUsername());
                return (User) principal;
            }
        }else{
        	//log.info("null");
        }
        return null;
    }

    private static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();

       
        
        if (context == null) {
        	
            return null;
        }else{
        
        }

        return context.getAuthentication();
    }

    public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request) {
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());

        if (request != null) {
            authentication.setDetails(new WebAuthenticationDetails(request));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}