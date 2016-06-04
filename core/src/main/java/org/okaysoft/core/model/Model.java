package org.okaysoft.core.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelAttrRef;
import org.okaysoft.core.annotation.RenderIgnore;
import org.okaysoft.core.annotation.SimpleDic;
import org.okaysoft.core.annotation.TreeDic;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.utils.ReflectionUtils;




@MappedSuperclass
@EntityListeners(value=ModelListener.class)
public abstract class Model implements Serializable {
	/**
	 * 
	 */
	@RenderIgnore
	@Transient
	private static final long serialVersionUID = 1L;

	@RenderIgnore
	@Transient
	protected final OkayLogger log = new OkayLogger(getClass()); 

	@ModelAttr("编号")
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;
	
	@ModelAttr("更新次数")
	@Version
	protected Integer version;
	
	@ModelAttr("创建时间")
	@Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
	protected Date createTime;
	
	@ModelAttr("上一次更新时间")
	@Column(insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
	protected Date updateTime;
	
	@ManyToOne
    @ModelAttr("数据所有者名称")
    @ModelAttrRef("username")
    protected User ownerUser;
	
	public Model(){
		ModelMetaData.addMetaData(this);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlTransient
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        if(this.ownerUser==null){
            this.ownerUser = ownerUser;
        }else{
            log.info("忽略设置OwnerUser");
        }
    }


	@Override
	public int hashCode() {
		/*
		if (id == null) {
            id = -1;
        }
        return new Integer(id + 1000).hashCode();*/
		return HashCodeBuilder.reflectionHashCode(this, "id" );
	}

	@Override
	public boolean equals(Object obj) {
		/*
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;*/
		return EqualsBuilder.reflectionEquals(this,obj,"id");
	}
	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getMetaData()+this.getId();
	}
	
	public List<ModelFieldData> getAllModelAttr(){
        List<ModelFieldData> list=new ArrayList<ModelFieldData>();
        //获取所有字段，包括继承的
        List<Field> fields = ReflectionUtils.getDeclaredFields(this);
        for (Field field : fields) {
            ModelFieldData data=getFieldData(field);
            if(data!=null){
                list.add(data);
            }
        }
        return list;
    }
	
	
	public List<ModelFieldData> getModelAttr(){
		List<ModelFieldData> lists = new ArrayList<ModelFieldData>();
		
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field : fields){
			ModelFieldData data = getFieldData(field);
			if(data !=null){
				lists.add(data);
			}
		}
		
		return lists;
	}
	
	
	private ModelFieldData getFieldData(Field field){
		if(field.isAnnotationPresent(ModelAttr.class)){
			String english = field.getName();
			return getModelFieldData(english,field);
		}
		return null;
	}
	
	private ModelFieldData getModelFieldData(String english,Field field){
		
		String chinese = field.getAnnotation(ModelAttr.class).value();
		ModelFieldData data = new ModelFieldData();
		data.setChinese(chinese);
		data.setEnglish(english);
		data.setSimpleDic("");
		data.setTreeDic("");
		
		if(field.isAnnotationPresent(SimpleDic.class)){
			String simpleDic = field.getAnnotation(SimpleDic.class).value();
			data.setSimpleDic(simpleDic);
		}
		if(field.isAnnotationPresent(TreeDic.class)){
			String treeDic = field.getAnnotation(TreeDic.class).value();
			data.setTreeDic(treeDic);
		}
		String valueClass = field.getType().getSimpleName();
		if("Timestamp".equals(valueClass) || "Date".equals(valueClass)){
			data.setType("Date");
		}else{
			data.setType(valueClass);
		}
		
		return data;
	}
	

	public abstract String getMetaData();
	
	

}
