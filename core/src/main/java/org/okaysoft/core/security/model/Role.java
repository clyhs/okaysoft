package org.okaysoft.core.security.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelAttrRef;
import org.okaysoft.core.annotation.ModelCollRef;
import org.okaysoft.core.annotation.RenderIgnore;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.module.service.ModuleService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;






@Entity
@Scope("prototype")
@Table(name="t_role")
@Component
@XmlRootElement
@XmlType(name = "Role")
public class Role extends Model {
	
	@Column(length=40)
    @ModelAttr("角色名")
	protected String roleName;
	
	@ModelAttr("备注")
    protected String des;
	
	@ManyToOne
    @ModelAttr("上级角色")
    @ModelAttrRef("roleName")
	protected Role parent;
	
	
	@RenderIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("id DESC")
    @ModelAttr("下级角色")
	@ModelCollRef("roleName")
	protected List<Role> child = new ArrayList<Role>();
	
	@ModelAttr("超级管理员")
    protected boolean superManager = false;
	
	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "roles", fetch = FetchType.LAZY)
    protected List<User> users=new ArrayList<User>();
	
	
	/**
     * 角色拥有的命令
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_command", joinColumns = {
    @JoinColumn(name = "roleID")}, inverseJoinColumns = {
    @JoinColumn(name = "commandID")})
    @OrderBy("id")
    protected List<Command> commands = new ArrayList<>();

    
	
    public String getModuleCommandStr(){
        if(this.commands==null || this.commands.isEmpty()){
            return "";
        }
        StringBuilder ids=new StringBuilder();
        
        Set<Integer> moduleIds=new HashSet<>();
        
        for(Command command : this.commands){
            ids.append("command-").append(command.getId()).append(",");
            Module module=command.getModule();
            moduleIds.add(module.getId());
            module=module.getParentModule();
            while(module!=null){
                moduleIds.add(module.getId());
                module=module.getParentModule();
            }
        }
        for(Integer moduleId : moduleIds){
            ids.append("module-").append(moduleId).append(",");
        }
        ids=ids.deleteCharAt(ids.length()-1);
        return ids.toString();
    }
	
    /**
     * 获取授予角色的权利
     * @return
     */
    public List<String> getAuthorities() {
        List<String> result = new ArrayList<>();
        if (superManager) {
            result.add("ROLE_SUPERMANAGER");
        }
        for (Command command : commands) {
            Map<String,String> map=ModuleService.getCommandPathToRole(command);
            for(String role : map.values()){
                StringBuilder str = new StringBuilder();
                str.append("ROLE_MANAGER").append(role);
                result.add(str.toString());
            }
        }
        return result;
    }

	@XmlAttribute
	public String getDes() {
		return des;
	}

	@XmlAttribute
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setDes(String des) {
		this.des = des;
	}
    @XmlTransient
	public Role getParent() {
		return parent;
	}

	public void setParent(Role parent) {
		this.parent = parent;
	}

	@XmlAttribute
	public boolean isSuperManager() {
		return superManager;
	}

	public void setSuperManager(boolean superManager) {
		this.superManager = superManager;
	}

	@XmlElementWrapper(name = "subRoles")
    @XmlElement(name = "role")
	public List<Role> getChild() {
		return child;
	}

	public void setChild(List<Role> child) {
		this.child = child;
	}
	
	

	@XmlTransient
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	@XmlTransient
	public List<Command> getCommands() {
		return commands;
	}
	
	public void addCommand(Command command){
		commands.add(command);
	}
	
	public void removeCommand(Command command){
		commands.remove(command);
	}
	
	public void clearCommand(){
		commands.clear();
	}

	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}

	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "角色信息";
	}
	
	
	

}
