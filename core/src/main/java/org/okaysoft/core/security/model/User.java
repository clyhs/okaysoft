package org.okaysoft.core.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


import org.okaysoft.core.annotation.IgnoreLog;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelAttrRef;
import org.okaysoft.core.annotation.ModelCollRef;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.service.ServiceFacade;
import org.okaysoft.core.utils.SpringContextUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;









@Entity
@Component
@Scope("prototype")
@Table(name="t_user",uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"username"})})
@XmlRootElement
@XmlType(name = "User")
public class User extends Model implements UserDetails{
	
	@ModelAttr("用户名")
	protected String username;
	@ModelAttr("姓名")
	protected String realName;
	@ModelAttr("密码")
	protected String password;
	@ModelAttr("备注")
	protected String des;
	
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_role", joinColumns = {
    @JoinColumn(name = "userID")}, inverseJoinColumns = {
    @JoinColumn(name = "roleID")})
    @OrderBy("id")
    @ModelAttr("用户拥有的角色列表")
    @ModelCollRef("roleName")
    protected List<Role> roles = new ArrayList<>();
    
    
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_userGroup", joinColumns = {
    @JoinColumn(name = "userID")}, inverseJoinColumns = {
    @JoinColumn(name = "userGroupID")})
    @OrderBy("id")
    @ModelAttr("用户拥有的用户组列表")
    @ModelCollRef("userGroupName")
    protected List<UserGroup> userGroups = new ArrayList<UserGroup>();
    
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_position", joinColumns = {
    @JoinColumn(name = "userID")}, inverseJoinColumns = {
    @JoinColumn(name = "positionID")})
    @OrderBy("id")
    @ModelAttr("用户拥有的岗位列表")
    @ModelCollRef("positionName")
    protected List<Position> positions = new ArrayList<Position>();
    
    @ManyToOne
    @ModelAttr("组织架构")
    @ModelAttrRef("orgName")
    protected Org org;
	
	
	@ModelAttr("账号过期")
    protected boolean accountexpired = false;
    @ModelAttr("账户锁定")
    protected boolean accountlocked = false;
    @ModelAttr("信用过期")
    protected boolean credentialsexpired = false;
    @ModelAttr("账户可用")
    protected boolean enabled = true;
    
    
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
    
    public String getPositionStrs(){
        if(this.positions==null || this.positions.isEmpty()) {
            return "";
        }
        StringBuilder result=new StringBuilder();
        for(Position position : this.positions){
            result.append("position-").append(position.getId()).append(",");
        }
        result=result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    
    public String getUserGroupStrs(){
        if(this.userGroups==null || this.userGroups.isEmpty()) {
            return "";
        }
        StringBuilder result=new StringBuilder();
        for(UserGroup userGroup : this.userGroups){
            result.append("userGroup-").append(userGroup.getId()).append(",");
        }
        result=result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    
    
    public String getAuthoritiesStr(){
        StringBuilder result=new StringBuilder();
        for(GrantedAuthority auth : getAuthorities()){
            result.append(auth.getAuthority()).append(",");
        }
        return result.toString();
    }
    
    
    /**
     * 用户是否为超级管理员
     * @return
     */
    public boolean isSuperManager(){
        if(this.roles != null && !this.roles.isEmpty()) {
            for(Role role : this.roles){
                if(role.isSuperManager()) {
                    return true;
                }
            }
        }
        if(this.userGroups != null && !this.userGroups.isEmpty()){
            for(UserGroup userGroup : this.userGroups){
                for(Role role : userGroup.getRoles()){
                    if(role.isSuperManager()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 
     * get all command
     */
    private List<Command> getAllCommand(){
    	ServiceFacade serviceFacade = SpringContextUtils.getBean("serviceFacade");
        List<Command> allCommand = serviceFacade.query(Command.class).getModels();
        return allCommand;
    }
    
    public List<Command> getCommand() {
        List<Command> result = new ArrayList<>();

        if(this.roles != null && !this.roles.isEmpty()) {
            //如果用户为超级管理员
            for (Role role : this.roles) {
                if (role.isSuperManager()) {
                    return getAllCommand();
                }
            }
            //如果用户不是超级管理员则进行一下处理
            for (Role role : this.roles) {
                result.addAll(role.getCommands());
            }
        }
        if(this.userGroups != null && !this.userGroups.isEmpty()){
            for(UserGroup userGroup : this.userGroups){
                //如果用户为超级管理员
                for(Role role : userGroup.getRoles()){
                    if(role.isSuperManager()) {
                        return getAllCommand();
                    }
                }
                //如果用户不是超级管理员则进行一下处理
                for(Role role : userGroup.getRoles()){
                    result.addAll(role.getCommands());
                }
            }
        }
        if(this.positions != null && !this.positions.isEmpty()) {
            for (Position position : this.positions) {
                result.addAll(position.getCommands());
            }
        }
        return result;
    }
    
    /**
     * get all module
     * @return
     */
    private List<Module> getAllModule(){
    	ServiceFacade serviceFacade = SpringContextUtils.getBean("serviceFacade");
        List<Module> allModule = serviceFacade.query(Module.class).getModels();
        return allModule;
    }
    
    private void assemblyModule(List<Module> modules,Module module){
        if(module!=null){
            Module parentModule=module.getParentModule();
            if(parentModule!=null){
                modules.add(parentModule);
                assemblyModule(modules,parentModule);
            }
        }
    }
    private List<Module> assemblyModule(List<Command> commands){
        List<Module> modules=new ArrayList<>();
        if(commands==null) {
            return modules;
        }
        
        for(Command command : commands){
            if(command!=null){
                Module module=command.getModule();
                if(module!=null){
                    modules.add(module);
                    assemblyModule(modules,module);
                }
            }
        }
        return modules;
    }
    
    public List<Module> getModule() {
        List<Module> result = new  ArrayList<>();

        if(this.roles != null && !this.roles.isEmpty()) {
            //如果用户为超级管理员
            for (Role role : this.roles) {
                if (role.isSuperManager()) {
                    return getAllModule();
                }
            }
            //如果用户不是超级管理员则进行一下处理
            for (Role role : this.roles) {
                result.addAll(assemblyModule(role.getCommands()));
            }
        }
        if(this.userGroups != null && !this.userGroups.isEmpty()){
            for(UserGroup userGroup : this.userGroups){
                //如果用户为超级管理员
                for(Role role : userGroup.getRoles()){
                    if(role.isSuperManager()) {
                        return getAllModule();
                    }
                }
                //如果用户不是超级管理员则进行一下处理
                for(Role role : userGroup.getRoles()){
                    result.addAll(assemblyModule(role.getCommands()));
                }
            }
        }
        if(this.positions != null && !this.positions.isEmpty()) {
            for (Position position : this.positions) {
                result.addAll(assemblyModule(position.getCommands()));
            }
        }

        return result;
    }
    
    

    @XmlAttribute
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@XmlAttribute
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlAttribute
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	

	

	public void setAccountexpired(boolean accountexpired) {
		this.accountexpired = accountexpired;
	}

	

	public void setAccountlocked(boolean accountlocked) {
		this.accountlocked = accountlocked;
	}

	

	public void setCredentialsexpired(boolean credentialsexpired) {
		this.credentialsexpired = credentialsexpired;
	}

	@XmlAttribute
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
	
	@XmlTransient
	public List<Role> getRoles() {
		return Collections.unmodifiableList(this.roles);
	}

	public void clearRole(){
		roles.clear();
	}
	
	
	public void addRole(Role role){
		this.roles.add(role);
	}
	
	public void removeRole(Role role){
		this.roles.remove(role);
	}
	

	@XmlTransient
	public List<UserGroup> getUserGroups() {
		return Collections.unmodifiableList(this.userGroups);
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
	public void addUserGroup(UserGroup ug){
		userGroups.add(ug);
	}

	public void removeUserGroup(UserGroup ug){
		userGroups.remove(ug);
	}
	
	public void clearUserGroup(){
		userGroups.clear();
	}
	
	@XmlTransient
	public List<Position> getPositions() {
		return Collections.unmodifiableList(this.positions);
	}
	
	public void addPosition(Position p){
		positions.add(p);
	}
	
	public void removePosition(Position p){
		positions.remove(p);
	}
	
	public void clearPosition(){
		positions.clear();
	}

	

	
	@XmlTransient
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "用户信息";
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> grantedAuthArray=new HashSet<>();

        log.debug("user privilege:");
        if(this.roles != null && !this.roles.isEmpty()) {
            log.debug("     roles:");
            for (Role role : this.roles) {
                for (String priv : role.getAuthorities()) {
                    log.debug(priv);
                    grantedAuthArray.add(new GrantedAuthorityImpl(priv.toUpperCase()));
                }
            }
        }
        if(this.userGroups != null && !this.userGroups.isEmpty()){
            log.debug("     userGroups:");
            for(UserGroup userGroup : this.userGroups){
                for(Role role : userGroup.getRoles()){
                    for (String priv : role.getAuthorities()) {
                        log.debug(priv);
                        grantedAuthArray.add(new GrantedAuthorityImpl(priv.toUpperCase()));
                    }
                }
            }
        }        
        if(this.positions != null && !this.positions.isEmpty()) {
            log.debug("     positions:");
            for (Position position : this.positions) {
                for (String priv : position.getAuthorities()) {
                    log.debug(priv);
                    grantedAuthArray.add(new GrantedAuthorityImpl(priv.toUpperCase()));
                }
            }
        }
        if(grantedAuthArray.isEmpty()){
            return null;
        }
        grantedAuthArray.add(new GrantedAuthorityImpl("ROLE_MANAGER"));
        log.debug("ROLE_MANAGER");
        return grantedAuthArray;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return !accountexpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !accountlocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return !credentialsexpired;
	}

	
	
}
