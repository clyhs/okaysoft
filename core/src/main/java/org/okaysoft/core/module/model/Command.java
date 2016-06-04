package org.okaysoft.core.module.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelAttrRef;
import org.okaysoft.core.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;




@Entity
@Scope("prototype")
@Component
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@XmlType(name = "Command")
public class Command extends Model {
	
	@ManyToOne
	@ModelAttr("所属模块")
	@ModelAttrRef("chinese")
	protected Module module;
	@ModelAttr("命令英文名称")
	protected String english;
	@ModelAttr("命令中文名称")
	protected String chinese;
	@ModelAttr("专属用户名")
	protected String username;
	@ModelAttr("链接地址")
	protected String url;
	@ModelAttr("排序号")
	protected int orderNum;
	@ModelAttr("是否显示")
	protected boolean display=true;

	
	
	@XmlTransient
	public Module getModule() {
		return module;
	}




	public void setModule(Module module) {
		this.module = module;
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
	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	@XmlAttribute
	public String getUrl() {
		String result="";
        if(url==null){
            result=this.getEnglish()+"Panel";
        }else{
            result=url;
        }
        return result;
	}




	public void setUrl(String url) {
		this.url = url;
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




	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "命令信息";
	}

}
