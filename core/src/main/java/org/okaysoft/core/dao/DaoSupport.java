package org.okaysoft.core.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;


import org.okaysoft.core.criteria.Order;
import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.PageCriteria;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.criteria.Sequence;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.model.Model;





public abstract class DaoSupport extends DataPrivilegeControl {
	
	protected final OkayLogger log = new OkayLogger(getClass());
	
	protected static final OrderCriteria defaultOrderCriteria = new OrderCriteria();
	
	static {
        defaultOrderCriteria.addOrder(new Order("id", Sequence.DESC));
    }
	
	@PersistenceContext(type=PersistenceContextType.EXTENDED)
    protected EntityManager em;
	
	protected <T extends Model> Page<T> queryData(Class<T> modelClass,PageCriteria page,PropertyCriteria propertyCriteria ,OrderCriteria orderCriteria){
		
		StringBuilder jpql = new StringBuilder("select o from ");
		jpql.append(getEntityName(modelClass)).append(" o ").append(buildPropertyCriteria(propertyCriteria)).append(" ").append(buildOrderCriteria(orderCriteria));
		//log.info("jpql:"+jpql.toString());
		
		
		Query query = em.createQuery(jpql.toString());	
		bindingPropertyCriteria(query,propertyCriteria);	
		buildPage(page,query);	
		
		
		setQueryCache(query);
		
		List<T> result = query.getResultList();
		
		
		Page<T> pageData = new Page<T>();
		if(result!=null){
			pageData.setModels(result);
			pageData.setTotalRecords(getCount(modelClass,propertyCriteria));
		}
		return pageData;
	}
	
	private void bindingPropertyCriteria(Query query, PropertyCriteria propertyCriteria) {
		if (query != null && propertyCriteria != null) {
			List<PropertyEditor> propertyEditors=propertyCriteria.getPropertyEditors();
			
			int length = propertyEditors.size();
			for(int i=0;i<length;i++){
				PropertyEditor propertyEditor = propertyEditors.get(i);
                List<PropertyEditor> subPropertyEditor=propertyEditor.getSubPropertyEditor();
                if(subPropertyEditor==null || subPropertyEditor.isEmpty()){
                    query.setParameter(propertyEditor.getProperty().getNameParameter(), propertyEditor.getProperty().getValue());
                }else{
                	//绑定子集合查询
                    //binding(query,propertyEditor,1);
                }
			}
		}
	}
	
	
	
	
	/**
	 * 
	 * test
	 * @param modelClass
	 * @param page
	 * @param queryString
	 * @return
	 */
	public <T extends Model> Page<T> search(Class<T> modelClass , PageCriteria page , String queryString){
		
		List<T> result = new ArrayList<T>();
		
		//log.info("search query:----------"+queryString);
		
		Query query = em.createQuery(queryString);
		int firstindex = (page.getPage() -1 ) * page.getSize();
		int maxresult = page.getSize();
		result = query.setFirstResult(firstindex).setMaxResults(maxresult).getResultList();
		
		Page<T> pageData = new Page(); 
		pageData.setModels(result);
		pageData.setTotalRecords(result.size());
		return pageData;
	}
	
	/**
	 * 
	 * @param propertyCriteria
	 * @return
	 */
	private <T extends Model> String buildPropertyCriteria(PropertyCriteria propertyCriteria){
		
		StringBuilder wherejpql = new StringBuilder("");
		String result = "";
		if (propertyCriteria != null && propertyCriteria.getPropertyEditors().size() > 0) {
			
			if(propertyCriteria.getCollection()!=null && propertyCriteria.getObject()!=null){
				wherejpql.append(" join o.").append(propertyCriteria.getCollection()).append(" ").append(propertyCriteria.getObject());
			}
			wherejpql.append(" where ");
			
			
			List<PropertyEditor> propertyEditors = propertyCriteria.getPropertyEditors(); 
			
			int length = propertyEditors.size();
			for(int i=0;i<length;i++){
				
				PropertyEditor propertyEditor = propertyEditors.get(i);
				
				List<PropertyEditor> subPropertyEditors = propertyEditor.getSubPropertyEditor();
				
				if(subPropertyEditors==null || subPropertyEditors.isEmpty()){
					
					if(propertyCriteria.getCollection()!=null && propertyCriteria.getObject()!=null && propertyEditor.getProperty().getName().startsWith(propertyCriteria.getObject())){
                        wherejpql.append(" ");
                    }else{
                        wherejpql.append(" o.");
                    }
					wherejpql.append(propertyEditor.getProperty().getName())
					         .append(" ")
					         .append(propertyEditor.getPropertyOperator().getSymbol())
					         .append("")
					         .append(":")
					         .append(propertyEditor.getProperty().getNameParameter());
					if(i<length-1){
                        wherejpql.append(" ");
                        wherejpql.append(propertyCriteria.getCriteria().name());
                        wherejpql.append(" ");
                    }
				}else{
					//子集合查询
				}
				
			}
			result = wherejpql.toString();
			
		}
		
		
		
		return result;
	}
	
	/**
	 * 
	 * @param orderCriteria
	 * @return
	 */
	private String buildOrderCriteria(OrderCriteria orderCriteria){
		StringBuilder orderbyql = new StringBuilder("");
		if(orderCriteria!=null && orderCriteria.getOrders().size()>0){
			orderbyql.append(" order by ");
			for (Order order : orderCriteria.getOrders()) {
                orderbyql.append("o.").append(order.getPropertyName()).append(" ").append(order.getSequence().getValue()).append(",");
            }
            orderbyql.deleteCharAt(orderbyql.length() - 1);
		}
		return orderbyql.toString();
	}
	
	/**
	 * 
	 * @param page
	 * @param query
	 */
	private void buildPage(PageCriteria page,Query query){
		
		if(page!=null && query!=null){
			int firstindex = (page.getPage() -1 ) * page.getSize();
			//int firstindex = page.getPage();
			//log.info("first:"+firstindex);
			int maxresult = page.getSize();
			query.setFirstResult(firstindex).setMaxResults(maxresult);
		}
		
	}
	
	
	private void setQueryCache(Query query){
        if (query instanceof org.hibernate.ejb.QueryImpl) {
            ((org.hibernate.ejb.QueryImpl) query).getHibernateQuery().setCacheable(true);
        }
    }
	
	public long getCount(Class<? extends Model> modelClass){
		String entityName = getEntityName(modelClass);
		String hql = "select count(o.id) from "+entityName+" o";
		Query query = em.createQuery(hql);
		
		return (Long) query.getSingleResult();
	}
	
	private Long getCount(Class<? extends Model> clazz, PropertyCriteria propertyCriteria) {
		Query query = em.createQuery("select count(o.id) from " + getEntityName(clazz) + " o " + buildPropertyCriteria(propertyCriteria));
		bindingPropertyCriteria(query, propertyCriteria);        
        setQueryCache(query);
        return (Long) query.getSingleResult();
	}

}
