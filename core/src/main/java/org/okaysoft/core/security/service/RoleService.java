package org.okaysoft.core.security.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.okaysoft.core.criteria.Criteria;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.module.model.TreeNode;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.service.ServiceFacade;
import org.springframework.stereotype.Service;




@Service
public class RoleService {
	
	protected static final OkayLogger log = new OkayLogger(RoleService.class);

	@Resource(name="serviceFacade")
    private ServiceFacade serviceFacade;
	
	public static List<String> getChildNames(Role role){
        List<String> names=new ArrayList<>();
        List<Role> child=role.getChild();
        for(Role item : child){
            names.add(item.getRoleName());
            names.addAll(getChildNames(item));
        }
        return names;
    }
	
	public static List<Integer> getChildIds(Role role){
        List<Integer> ids=new ArrayList<>();
        List<Role> child=role.getChild();
        for(Role item : child){
            ids.add(item.getId());
            ids.addAll(getChildIds(item));
        }
        return ids;
    }
	
	public static boolean isParentOf(Role parent,Role child){
        Role role=child.getParent();
        while(role!=null){
            if(role.getId()==parent.getId()){
                return true;
            }
            role=role.getParent();
        }
        return false;
    }
	
	public String toRootJson(boolean recursion){
        Role rootRole=getRootRole();
        if(rootRole==null){
            log.error("获取根角色失败！");
            return "";
        }
        
        
        
        StringBuilder json=new StringBuilder();
        json.append("[");

        json.append("{'text':'")
            .append(rootRole.getRoleName())
            .append("','id':'role-")
            .append(rootRole.getId());
            
            if(!checkIsChildern(rootRole.getId())){
                json.append("','leaf':true,'cls':'file'");
            }else{
                json.append("','leaf':false,'cls':'folder'");
                
                //if (recursion) {
                    //for(Role item : roles){
                        //json.append(",children:").append(toJson(item.getId(), recursion));
                    //}
                //}
            }
        json.append("}");
        json.append("]");
        
        return json.toString();
    }
	
	
	/* 这个方法 不能取到同步更新的值*/
	public String toJson(int roleId, boolean recursion){
        Role role=serviceFacade.find(Role.class, roleId);
        if(role==null){
            log.error("获取ID为 "+roleId+" 的角色失败！");
            return "";
        }
        List<Role> child=role.getChild();
        if(child.isEmpty()){
            return "";
        }
        StringBuilder json=new StringBuilder();
        json.append("[");

        
        for(Role item : child){
            json.append("{'text':'")
                .append(item.getRoleName())
                .append("','id':'role-")
                .append(item.getId());
                if(item.getChild().isEmpty()){
                    json.append("','leaf':true,'cls':'file'");
                }else{
                    json.append("','leaf':false,'cls':'folder'");
                    if (recursion) {
                        json.append(",children:").append(toJson(item.getId(), recursion));
                    }
                }
           json .append("},");
        }
        //删除最后一个,号，添加一个]号
        json=json.deleteCharAt(json.length()-1);
        json.append("]");

        return json.toString();
    }
	
	public String toJsonByParentId(int id, boolean recursion){
		
		PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.or);
        propertyCriteria.addPropertyEditor(new PropertyEditor("parent.id", Operator.eq,id));
        Page<Role> page = serviceFacade.query(Role.class, null, propertyCriteria);
        
        List<Role> roles = page.getModels();
        
        StringBuilder json=new StringBuilder();
        json.append("[");

        
        for(Role item : roles){
            json.append("{'text':'")
                .append(item.getRoleName())
                .append("','id':'role-")
                .append(item.getId());
                
                if(!checkIsChildern(item.getId())){
                    json.append("','leaf':true,'cls':'file'");
                }else{
                    json.append("','leaf':false,'cls':'folder'");
                    if (recursion) {
                        json.append(",children:").append(toJsonByParentId(item.getId(), recursion));
                    }
                }
           json .append("},");
        }
        //删除最后一个,号，添加一个]号
        json=json.deleteCharAt(json.length()-1);
        json.append("]");
        
		return json.toString();
	}
	

	
	public boolean checkIsChildern(int id){
		PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.or);
        propertyCriteria.addPropertyEditor(new PropertyEditor("parent.id", Operator.eq,id));
        Page<Role> page = serviceFacade.query(Role.class, null, propertyCriteria); 
        List<Role> roles = page.getModels();
        if(roles.size()>0){
        	return true;
        }
        else{
        	return false;
        }
	}
	
	public Role getRootRole(){
        PropertyCriteria propertyCriteria = new PropertyCriteria(Criteria.or);
        propertyCriteria.addPropertyEditor(new PropertyEditor("roleName", Operator.eq, "String","角色"));
        Page<Role> page = serviceFacade.query(Role.class, null, propertyCriteria);
        if (page.getTotalRecords() == 1) {
            return page.getModels().get(0);
        }
        return null;
    }
	
	
}
