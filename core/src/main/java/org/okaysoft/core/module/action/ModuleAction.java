package org.okaysoft.core.module.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.module.service.ModuleService;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Scope("prototype")
@Controller
@Namespace("/module")
public class ModuleAction extends ExtJSSimpleAction<Module> {
	@Resource(name="moduleService")
    private ModuleService moduleService;
    private String node;
    private boolean privilege=false;
    private boolean recursion=false;
    
    
	@Override
	public String query() {
		// TODO Auto-generated method stub
		if(node==null){
            return super.query();
        }
        Module module=null;
        if(node.contains("-")){
            String[] temp=node.split("-");
            int id=Integer.parseInt(temp[1]);
            module=moduleService.getModule(id);
        }else if(node.trim().startsWith("root")){
            module=moduleService.getRootModule();
        }
		
        if(module!=null){
            String json="";
            if(privilege){
            	//log.info("1");
                json=moduleService.toJsonForPrivilege(module);
            }else{
            	//log.info("2");
                json=moduleService.toJsonForUser(module,recursion);
            }
            //log.info("module action:======="+json);
            Struts2Utils.renderJson(json);
        }
		return null;
	}

	public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }

    public void setNode(String node) {
        this.node = node;
    }

	
	

	
    
    
    
}
