package org.okaysoft.core.security.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Component
@Table(name="t_position")
@XmlRootElement
@XmlType(name = "Position")
public class Position extends Model {
	

    @ModelAttr("岗位名称")
    protected String positionName;

    @ManyToOne
    @ModelAttr("上级岗位")
    @ModelAttrRef("positionName")
    protected Position parent;

    @RenderIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("id DESC")
    @ModelAttr("下级岗位")
    @ModelCollRef("positionName")
    protected List<Position> child = new ArrayList<Position>();
    
    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "positions", fetch = FetchType.EAGER)
    protected List<User> users=new ArrayList<User>();

    /**
     * 职位拥有的命令
     */
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "t_position_command", joinColumns = {
    @JoinColumn(name = "positionID")}, inverseJoinColumns = {
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
     * 获取授予岗位的权利
     * @return
     */
    public List<String> getAuthorities() {
        List<String> result = new ArrayList<>();
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
	public String getPositionName() {
		return positionName;
	}



	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}



	@XmlTransient
	public Position getParent() {
		return parent;
	}



	public void setParent(Position parent) {
		this.parent = parent;
	}



	@XmlElementWrapper(name = "subPositions")
    @XmlElement(name = "position")
	public List<Position> getChild() {
		return child;
	}



	public void setChild(List<Position> child) {
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



	public void setCommands(List<Command> commands) {
		this.commands = commands;
	}



	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "岗位";
	}

}
