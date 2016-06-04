package org.okaysoft.core.module.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelAttrRef;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.module.service.ModuleService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;






@Entity
@Scope("prototype")
@Component
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@XmlRootElement
@XmlType(name = "Module")
public class Module extends Model {
	@ManyToOne
	@ModelAttr("父模块")
	@ModelAttrRef("chinese")
	protected Module parentModule;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parentModule")
    @OrderBy("orderNum ASC")
    //不缓存，如果缓存则在修改排序号之后数据不会失效
    //@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
	protected List<Module> subModules = new ArrayList<>();
	
	@ModelAttr("模块英文名称")
	protected String english;
	
	@ModelAttr("模块中文名称")
	protected String chinese;
	
	@ModelAttr("排序号")
	protected int orderNum;
	
	@ModelAttr("是否显示")
	protected boolean display=true;
	
	@ModelAttr("链接地址")
	protected String url;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "module" )
    @OrderBy("orderNum ASC")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
	protected List<Command> commands = new ArrayList<>();
	
	

	@XmlTransient
	public Module getParentModule() {
		return parentModule;
	}



	public void setParentModule(Module parentModule) {
		this.parentModule = parentModule;
	}



	@XmlElementWrapper(name = "subModules")
    @XmlElement(name = "module")
	public List<Module> getSubModules() {
		return subModules;
	}
	

	public void addSubModule(Module subModule) {
        this.subModules.add(subModule);
    }

    public void removeSubModule(Module subModule) {
        this.subModules.remove(subModule);
    }


    @XmlAttribute
	public String getEnglish() {
		return english;
	}



	public void setEnglish(String english) {
		this.english = english;
	}



	@XmlAttribute
	public String getChinese() {
		return chinese;
	}



	public void setChinese(String chinese) {
		this.chinese = chinese;
	}



	@XmlAttribute
	public int getOrderNum() {
		return orderNum;
	}



	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}



	@XmlAttribute
	public boolean isDisplay() {
		return display;
	}



	public void setDisplay(boolean display) {
		this.display = display;
	}



	@XmlAttribute
	public String getUrl() {
		String result="";
        if(url==null){
            //result=ModuleService.getModulePath(this.getParentModule())+this.getEnglish()+"Panel";
        	result=this.getEnglish()+"Panel";
        }else{
            result=url;
        }
        return result;
	}



	public void setUrl(String url) {
		this.url = url;
	}


	@XmlElementWrapper(name = "commands")
    @XmlElement(name = "command")
	public List<Command> getCommands() {
		return commands;
	}
	
	public void addCommand(Command command) {
        this.commands.add(command);
    }

    public void removeCommand(Command command) {
        this.commands.remove(command);
    }

	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "模块信息";
	}


	@Override
	public int hashCode() {
		/*
		int hash = 3;
        hash = 97 * hash + (this.english != null ? this.english.hashCode() : 0);
        return hash;*/
		
		return HashCodeBuilder.reflectionHashCode(this, "english" );
	}



	@Override
	public boolean equals(Object obj) {
		/*
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Module other = (Module) obj;
		if (chinese == null) {
			if (other.chinese != null)
				return false;
		} else if (!chinese.equals(other.chinese))
			return false;
		return true;*/
		return EqualsBuilder.reflectionEquals(this,obj,"chinese");
	}
	
	

}
