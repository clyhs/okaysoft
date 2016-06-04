package org.okaysoft.core.security.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.security.model.UserGroup;
import org.okaysoft.core.security.service.UserGroupService;
import org.okaysoft.core.utils.Struts2Utils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;



@Scope("prototype")
@Controller
@Namespace("/security")
public class UserGroupAction extends ExtJSSimpleAction<UserGroup> {
	
	@Resource(name="userGroupService")
    private UserGroupService userGroupService;
    private List<Role> roles = null;
    
    public String store(){     
        String json = userGroupService.toAllUserGroupJson();
        Struts2Utils.renderJson(json);

        return null;
    } 
    
    @Override
    public void prepareForDelete(Integer[] ids){
        //User loginUser=UserHolder.getCurrentLoginUser();
        for(int id :ids){
            UserGroup userGroup=service.find(UserGroup.class, id);
            boolean canDel=true;
            //获取拥有等待删除的角色的所有用户
            List<User> users=userGroup.getUsers();
            //for(User user : users){
                //if(loginUser.getId()==user.getId()){
                    //canDel=false;
                //}
            //}
            if(!canDel) {
                continue;
            }
            for(User user : users){
                user.removeUserGroup(userGroup);
                service.update(user);
            }
        }
    }
    
    @Override
    public void assemblyModelForCreate(UserGroup model) {
        model.setRoles(roles);
    }
    @Override
    public void assemblyModelForUpdate(UserGroup model){
        //默认roles==null
        //当在修改用户组的时候，如果客户端不修改roles，则roles==null
        if(roles!=null){
            model.setRoles(roles);
        }
    }
    
    @Override
    protected void findAfterRender(Map map,UserGroup model){
        map.put("roles", model.getRoleStrs());
    }
    
    public void setRoles(String roleStr) {
        String[] ids=roleStr.split(",");
        roles=new ArrayList<>();
        for(String id :ids){
            String[] attr=id.split("-");
            if(attr.length==2){
                if("role".equals(attr[0])){
                    Role role=service.find(Role.class, Integer.parseInt(attr[1]));
                    roles.add(role);
                }
            }
        }   
    } 

}
