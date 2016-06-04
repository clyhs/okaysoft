package org.okaysoft.core.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.PageCriteria;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.Property;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.model.Model;
import org.springframework.stereotype.Repository;


@Repository
public class DaoFacade extends DaoSupport {

	public void clear(){
		em.clear();
	}
	
	public <T extends Model> void create(T model){
		em.persist(model);
	}
	
	public <T extends Model> void update(T model){
	    em.merge(model);	
	}
	
	public <T extends Model> void update(Class<T> modelClass,Integer modelId,List<Property> properties){
		
	}
	
	public <T extends Model> T find(Class<T> modelClass,Integer modelId){
		T model = em.find(modelClass, modelId);
		
//		Session session = (Session) em.getDelegate();
//	    Transaction ts = session.beginTransaction();
//	    
//	    String entityName = getEntityName(modelClass);
//	    
//	    entityName = entityName.substring(0, 1).toUpperCase()+entityName.substring(1).toLowerCase();
//	    log.info(entityName);
//	    Query query = session.createQuery(" from "+entityName+" o where o.id=?").setParameter(0, modelId);
//	    
//	    List<T> lists = query.list();
//	    
//	    session.flush();
//	    ts.commit();
//	    session.close();
//	    if(lists.size()>0){
//	    	model = lists.get(0);
//	    }
//		//T model = em.
		return model;
	}
	
	public <T extends Model> void delete(Class<T> modelClass,Integer modelId){
		T model = find(modelClass, modelId);
		if(model!=null){
			em.remove(model);
		}
	}
	
	
	
	public <T extends Model> Page<T> query(Class<T> modelClass ){
		return query(modelClass,null);
	}
	
	public <T extends Model> Page<T> query(Class<T> modelClass,PageCriteria pageCriteria  ){
		return query(modelClass,pageCriteria,null);
	}
	
	public <T extends Model> Page<T> query(Class<T> modelClass,PageCriteria pageCriteria,PropertyCriteria propertyCriteria ){
		return query(modelClass,pageCriteria,propertyCriteria,defaultOrderCriteria);
	}
	
	public <T extends Model> Page<T> query(Class<T> modelClass,PageCriteria pageCriteria,PropertyCriteria propertyCriteria ,OrderCriteria orderCriteria){
		return queryData(modelClass,pageCriteria,propertyCriteria,orderCriteria);
	}
}
