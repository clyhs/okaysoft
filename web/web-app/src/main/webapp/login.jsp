<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter"%>
<%@page import="org.okaysoft.core.security.service.UserDetailsServiceImpl"%>
<%
	//String userAgent = request.getHeader("User-Agent");
	//request.getSession().setAttribute("userAgent", userAgent);
	String message = "";
	String state = request.getParameter("state");
	if (state != null) {
		response.addHeader("state", state);
	}
	if ("checkCodeError".equals(state)) {
		response.addHeader("checkCodeError", "true");
		message = "验证码错误";
		response.getWriter().write(message);
		response.getWriter().flush();
		response.getWriter().close();
		return;
	}
	Object obj = session.getAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY);

	String lastUsername = "";
	if (obj != null) {
		lastUsername = obj.toString();
		if (request.getParameter("login_error") != null) {
			String tip = UserDetailsServiceImpl.getMessage(lastUsername);
			if (tip != null) {
				message = tip;
				response.addHeader("login_error", "true");
				response.getWriter().write(message);
				response.getWriter().flush();
				response.getWriter().close();
				return;
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录页</title>

<script type="text/javascript" src="/okaysoft/js/jsloader.js"></script>
<script type="text/javascript" src="/okaysoft/login/loginPage.js"></script>
<script type="text/javascript">
	Ext.onReady(function() {
		Ext.QuickTips.init();
		Ext.BLANK_IMAGE_URL = '/okaysoft/extjs/images/default/s.gif';
		Ext.form.Field.prototype.msgTarget = 'side';
		if("<%=state%>" == "checkCodeError") {
			Ext.ux.Toast.msg('登录提示：', '验证码错误，请重新登录!');
		}
		var win = new LoginWindow();
		win.show();
		setTimeout(function() {
			Ext.get('loading-mask').fadeOut({
				remove : true
			});
		}, 10);

		fixPng();

	});
</script>
</head>
<body>
	<div id="loading-mask">
		<div id="loading">
			<div style="text-align: center; padding-top: 26%">
				<img alt="Loading..." src="/okaysoft/images/extanim32.gif" width="32" height="32" style="margin-right: 8px;" />
				Loading...
			</div>
		</div>
	</div>
</body>
</html>