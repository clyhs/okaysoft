package org.okaysoft.core.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelCollRef;
import org.okaysoft.core.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Entity
@Scope("prototype")
@Component
@Table(name = "t_userGroup")
public class UserGroup extends Model {
	
	@Column(length=40)
    @ModelAttr("用户组名称")
    protected String userGroupName;
    @ModelAttr("备注")
    protected String des;
    
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "userGroups", fetch = FetchType.LAZY)
    protected List<User> users=new ArrayList<User>();
    
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "t_userGroup_role", joinColumns = {
    @JoinColumn(name = "userGroupID")}, inverseJoinColumns = {
    @JoinColumn(name = "roleID")})
    @OrderBy("id")
    @ModelAttr("用户组拥有的角色列表")
    @ModelCollRef("roleName")
    protected List<Role> roles = new ArrayList<Role>();
    
    public String getRoleStrs(){
        if(this.roles==null || this.roles.isEmpty()) {
            return "";
        }
        StringBuilder result=new StringBuilder();
        for(Role role : this.roles){
            result.append("role-").append(role.getId()).append(",");
        }
        result=result.deleteCharAt(result.length()-1);
        return result.toString();
    }

    
    public String getModuleCommandStr(){
        StringBuilder ids=new StringBuilder();
        for(Role role : roles){
            ids.append(role.getModuleCommandStr());
        }
        return ids.toString();
    }
    
    /**
     * 获取授予用户组的权利
     * @return
     */
    public List<String> getAuthorities() {
        List<String> result = new ArrayList<>();        
        for(Role role : roles){
            result.addAll(role.getAuthorities());
        }
        return result;
    }
    
	public String getUserGroupName() {
		return userGroupName;
	}


	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}


	public String getDes() {
		return des;
	}


	public void setDes(String des) {
		this.des = des;
	}

	

	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	public List<Role> getRoles() {
		return roles;
	}


	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	
	

	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "用户组信息";
	}

}
