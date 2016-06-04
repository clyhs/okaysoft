package org.okaysoft.core.security.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.module.model.TreeNode;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.security.service.RoleService;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;




@Scope("prototype")
@Controller
@Namespace("/security")
public class RoleAction extends ExtJSSimpleAction<Role> {

	private String node;
    @Resource(name="roleService")
    private RoleService roleService;
    private List<Command> commands;
    private boolean recursion=false;
    
    public String store(){            
        if(recursion){
            int rootId = roleService.getRootRole().getId();
            
            log.info("rootId"+rootId);
            
            String json=roleService.toJson(rootId,recursion);
            Struts2Utils.renderJson(json);

            return null;
        }

        return query();
    }
    
    
    @Override
    public String query(){
        //如果node为null则采用普通查询方式
        if(node==null){
        	
            return super.query();
            
        }
        //如果指定了node则采用自定义的查询方式
        
        if(node.trim().startsWith("root")){
        	
            String json=roleService.toRootJson(recursion);
            
            Struts2Utils.renderJson(json);
        }else{
            String[] attr=node.trim().split("-");
            if(attr.length==2){
                int roleId=Integer.parseInt(attr[1]);
                //String json=roleService.toJson(roleId,recursion);
                String json = roleService.toJsonByParentId(roleId,recursion);
              Struts2Utils.renderJson(json);                    
            }   
        }
        return null;
    }
    
    @Override
    public void assemblyModelForCreate(Role model) {
        if(model.isSuperManager()){
            return;
        }
        model.setCommands(commands);
    }
    
    @Override
    public void assemblyModelForUpdate(Role model){
        if(model.isSuperManager()){
            model.clearCommand();
            return;
        }
        //默认commands==null
        //当在修改角色的时候，如果客户端不修改commands，则commands==null
        if(commands!=null){
            model.setCommands(commands);
        }
    }
    
    @Override
    protected void findAfterRender(Map map,Role model){
        map.put("privileges", model.getModuleCommandStr());
        map.put("superManager", model.isSuperManager());
    }
    
    public void setPrivileges(String privileges) {
        String[] ids=privileges.split(",");
        commands=new ArrayList<>();
        for(String id :ids){
            String[] attr=id.split("-");
            if(attr.length==2){
                if("command".equals(attr[0])){
                    Command command=service.find(Command.class, Integer.parseInt(attr[1]));
                    commands.add(command);
                }
            }
        }        
    }
    
    
    
	public void setNode(String node) {
		this.node = node;
	}
	public void setRecursion(boolean recursion) {
		this.recursion = recursion;
	}
    
    
}
