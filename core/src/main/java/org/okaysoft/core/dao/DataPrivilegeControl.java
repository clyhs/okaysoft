package org.okaysoft.core.dao;

import javax.persistence.Entity;

import org.okaysoft.core.model.Model;



public class DataPrivilegeControl {
	
	private static String[] excludes = null;
	
	static{
		
	}
	
	protected boolean needPrivilege(String modelClass){
		for(String exclude:excludes){
			if(exclude.equals(modelClass))
			{
				return false;
			}
		}
		return true;
	}
	
	protected <T extends Model> boolean needPrivilege(Class<T> modelClass){
		String entity = getEntityName(modelClass);
		return needPrivilege(entity);
	}
	
	protected String getEntityName(Class<? extends Model> clazz){
		String entityname = clazz.getSimpleName();
		Entity entity = clazz.getAnnotation(Entity.class);
		if (entity != null && entity.name() != null && !"".equals(entity.name())) {
            entityname = entity.name();
        }
        return entityname;
	}

}
