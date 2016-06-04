package org.okaysoft.core.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
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
import org.okaysoft.core.annotation.RenderIgnore;
import org.okaysoft.core.model.Model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Entity
@Scope("prototype")
@Component
@Table(name="t_org")
@XmlRootElement
@XmlType(name = "Org")
public class Org extends Model {
	
	@ModelAttr("组织架构名称")
    protected String orgName;

    @ModelAttr("负责人姓名")
    protected String chargeMan;

    @ModelAttr("联系电话")
    protected String phone;

    @ModelAttr("办公地址")
    protected String address;

    @ModelAttr("部门主要职能")
    @Lob
    protected String functions;
    
    @ManyToOne
    @ModelAttr("上级组织架构")
    @ModelAttrRef("orgName")
    protected Org parent;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @OrderBy("id DESC")
    @RenderIgnore
    protected List<Org> child=new ArrayList<Org>();
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "org")
    @OrderBy("id DESC")
    @RenderIgnore
    protected List<User> users=new ArrayList<User>();
    
    

    @XmlAttribute
	public String getOrgName() {
		return orgName;
	}



	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}



	@XmlAttribute
	public String getChargeMan() {
		return chargeMan;
	}



	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}



	@XmlAttribute
	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	@XmlAttribute
	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	@XmlAttribute
	public String getFunctions() {
		return functions;
	}



	public void setFunctions(String functions) {
		this.functions = functions;
	}



	@XmlTransient
	public Org getParent() {
		return parent;
	}



	public void setParent(Org parent) {
		this.parent = parent;
	}



	@XmlElementWrapper(name = "subOrgs")
    @XmlElement(name = "org")
	public List<Org> getChild() {
		return child;
	}



	public void setChild(List<Org> child) {
		this.child = child;
	}



	@XmlTransient
	public List<User> getUsers() {
		return users;
	}



	public void setUsers(List<User> users) {
		this.users = users;
	}



	@Override
	public String getMetaData() {
		// TODO Auto-generated method stub
		return "组织架构";
	}

}
