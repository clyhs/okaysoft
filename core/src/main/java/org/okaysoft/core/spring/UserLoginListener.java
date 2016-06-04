package org.okaysoft.core.spring;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.monitor.model.UserLogin;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.security.service.UserHolder;
import org.okaysoft.core.service.LogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;





public class UserLoginListener implements HttpSessionAttributeListener,
		HttpSessionListener {
	
	protected static final OkayLogger log =new OkayLogger(UserLoginListener.class);
	
	private static Map<String,UserLogin> logs=new HashMap<String,UserLogin>();

    private static Map<String,HttpSession> sessions=new HashMap<>();
    private static final boolean loginMonitor;
    
    static{
    	loginMonitor = true;
    	log.info("启用用户登录注销日志");
    } 

	@Override
	public void sessionCreated(HttpSessionEvent hse) {
		// TODO Auto-generated method stub

		HttpSession session = hse.getSession();
        sessions.put(session.getId(), session);
        log.info("创建会话，ID："+session.getId()+" ,当前共有会话："+sessions.size());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent hse) {
		// TODO Auto-generated method stub

		HttpSession session = hse.getSession();
        sessions.remove(session.getId());
        log.info("销毁会话，ID："+session.getId()+" ,当前共有会话："+sessions.size());
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub

		if (se.getValue() instanceof SecurityContextImpl && loginMonitor) {
            User user=UserHolder.getCurrentLoginUser();
            if (null != user) {
                String sessioId=se.getSession().getId();
                log.info("用户 "+user.getUsername()+" 登录成功，会话ID："+sessioId);
                
                
                if(logs.get(user.getUsername())==null){
                    log.info("开始记录用户 "+user.getUsername()+" 的登录日志");
                    String ip=UserHolder.getCurrentUserLoginIp();
                    UserLogin userLogin=new UserLogin();
                    userLogin.setAppName(SystemListener.getContextPath());
                    userLogin.setLoginIP(ip);
                    //userLogin.setUserAgent(se.getSession().getAttribute("userAgent").toString());
                    userLogin.setLoginTime(new Date());
                    try {
                        userLogin.setServerIP(InetAddress.getLocalHost().getHostAddress());
                    } catch (UnknownHostException e) {
                        log.error("记录登录日志出错",e);
                    }
                    userLogin.setOwnerUser(user);
                    logs.put(user.getUsername(), userLogin);
                }else{
                    log.info("用户 "+user.getUsername()+" 的登录日志已经被记录过，用户在未注销前又再次登录，忽略此登录");
                }
            }
		}
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub

		if (se.getValue() instanceof SecurityContextImpl && loginMonitor) {
            SecurityContext context=(SecurityContext)se.getValue();
            Authentication authentication=context.getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof User) {
                    User user = (User) principal;
                    if (null != user) {
                        String sessioId=se.getSession().getId();
                        log.info("用户 "+user.getUsername()+" 注销成功，会话ID："+sessioId);
                        log.info("用户 "+user.getUsername()+" 注销成功，会话ID："+sessioId);

                        UserLogin userLogin=logs.get(user.getUsername());
                        if(userLogin!=null){
                            log.info("开始记录用户 "+user.getUsername()+" 的注销日志");
                            userLogin.setLogoutTime(new Date());
                            userLogin.setOnlineTime(userLogin.getLogoutTime().getTime()-userLogin.getLoginTime().getTime());
                            LogService.addLog(userLogin);
                            logs.remove(user.getUsername());
                        }else{
                            log.info("无法记录用户 "+user.getUsername()+" 的注销日志，因为用户的登录日志不存在");
                        }
                    }
                }
            }
		}
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub

	}
	public static void forceAllUserOffline(){
        if(!loginMonitor){
            return;
        }
        int len=logs.size();
        if(len<1){
            return;
        }
        
        log.info("有 "+len+" 个用户还没有注销，强制所有用户退出");
        for(String username : logs.keySet()){
            UserLogin userLogin=logs.get(username);
            log.info("开始记录用户 "+username+" 的注销日志");
            userLogin.setLogoutTime(new Date());
            userLogin.setOnlineTime(userLogin.getLogoutTime().getTime()-userLogin.getLoginTime().getTime());
            LogService.addLog(userLogin);
            logs.remove(username);
        }
    }

}
