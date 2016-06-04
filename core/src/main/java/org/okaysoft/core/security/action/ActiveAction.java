package org.okaysoft.core.security.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.DefaultAction;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@SuppressWarnings("serial") 
@Scope("prototype")
@Controller
@Namespace("/security")
public class ActiveAction extends DefaultAction {
	
	public String active(){
		return null;
	}

}
