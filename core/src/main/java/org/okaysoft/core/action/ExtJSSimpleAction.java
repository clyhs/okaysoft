package org.okaysoft.core.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang.StringUtils;
import org.okaysoft.core.action.converter.DateTypeConverter;
import org.okaysoft.core.annotation.ModelAttr;
import org.okaysoft.core.annotation.ModelAttrRef;
import org.okaysoft.core.annotation.ModelCollRef;
import org.okaysoft.core.annotation.RenderIgnore;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.Property;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.security.model.Role;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.service.ExcelService;
import org.okaysoft.core.service.ServiceFacade;
import org.okaysoft.core.utils.ReflectionUtils;
import org.okaysoft.core.utils.SpringContextUtils;
import org.okaysoft.core.utils.Struts2Utils;

public abstract class ExtJSSimpleAction<T extends Model> extends
		ExtJSActionSupport implements Action {

	private boolean search = false;
	@Resource(name = "serviceFacade")
	protected ServiceFacade service;
	protected T model = null;
	protected Class<T> modelClass;
	protected Page<T> pageData = new Page<T>();
	protected Map map = null;
	@Resource(name = "excelService")
	protected ExcelService excelService;

	@Resource(name = "springContextUtils")
	protected SpringContextUtils springContextUtils;

	@PostConstruct
	private void initModel() {
		try {
			if (this.model == null) {
				String modelName = getDefaultModelName();

				// System.out.println(modelName);

				if ("model".equals(modelName)) {
					this.model = (T) getRequest().getAttribute("model");
				} else {
					// System.out.println(modelName+"get bean before");
					this.model = (T) springContextUtils.getBean(modelName);

					// System.out.println(modelName+"get bean after");
				}
				modelClass = (Class<T>) model.getClass();
			}
		} catch (Exception e) {
			log.error("initModel  fail");
		}
	}

	private String getDefaultModelName() {
		return getDefaultModelName(this.getClass());
	}

	protected void checkModel(T model) throws Exception {

	}

	protected void assemblyModelForCreate(T model) {

	}

	protected void afterSuccessCreateModel(T model) {

	}

	/**
	 * 模型【创建失败】后的回调方法
	 * 
	 * @return
	 */
	protected void afterFailCreateModel(T model) {

	}

	@Override
	public String create() {
		// TODO Auto-generated method stub
		//log.info("create usergroup");
		try {
			try {
				checkModel(model);
			} catch (Exception e) {
				map = new HashMap();
				map.put("success", false);
				map.put("message", e.getMessage() + ",不能添加");
				Struts2Utils.renderJson(map);
				return null;
			}
			assemblyModelForCreate(model);
			objectReference(model);
			service.create(model);
			afterSuccessCreateModel(model);
		} catch (Exception e) {
			log.error("创建模型失败", e);
			afterFailCreateModel(model);

			map = new HashMap();
			map.put("success", false);
			map.put("message", "创建失败 " + e.getMessage());
			Struts2Utils.renderJson(map);
			return null;
		}
		map = new HashMap();
		map.put("success", true);
		map.put("message", "创建成功");
		Struts2Utils.renderJson(map);
		return null;
	}

	public String export() {
		if (search) {
			// 导出全部搜索结果
			pageData = service.query(modelClass, getPageCriteria(), getPc());
			List<T> models = processSearchResult(pageData.getModels());
			pageData.setModels(models);
			// 导出当前页的搜索结果
			// this.setPage(service.search(getQueryString(), getPageCriteria(),
			// modelClass));
		} else {
			// 导出全部数据
			this.setPageData(service.query(modelClass, null,
					buildPropertyCriteria(), buildOrderCriteria()));
			// 导出当前页的数据
			// this.setPage(service.query(modelClass, getPageCriteria(),
			// buildPropertyCriteria(), buildOrderCriteria()));
		}
		List<List<String>> result = new ArrayList<>();
		renderForExport(result);
		String path = excelService.write(result, exportFileName());
		Struts2Utils.renderText(path);
		return null;
	}

	private List<T> processSearchResult(List<T> models) {
		List<T> result = new ArrayList<>();
		for (T obj : models) {
			T t = service.find(modelClass, obj.getId());
			if (t != null) {
				result.add(t);
			}
		}
		return result;
	}

	@Override
	public String createForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String find() {
		// TODO Auto-generated method stub
		this.setModel(service.find(modelClass, model.getId()));
		if (model == null) {
			Struts2Utils.renderText("false");
			return null;
		}
		afterfind(model);
		Map temp = new HashMap();
		renderJsonForfind(temp);
		findAfterRender(temp, model);
		Struts2Utils.renderJson(temp);
		return null;
	}

	protected void afterfind(T model) {

	}

	@Override
	public String updateWhole() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void assemblyModelForUpdate(T model) {

    }
    /**
     * 模型【部分】【更新成功】后的回调方法
     * @return
     */
    protected void afterSuccessPartUpdateModel(T model) {

    }
    
    /**
     * 模型【部分】【更新失败】后的回调方法
     * @return
     */
    protected void afterFailPartUpdateModel(T model) {

    }
    
    protected void old(T model) {

    }
    protected void now(T model) {

    }

	@Override
	public String updatePart() {
		// TODO Auto-generated method stub

		try{
            Integer version=model.getVersion();
            //此时的model里面存的值是从浏览器传输过来的
            List<Property> properties=getPartProperties(model);
            //此时的model里面存的值是从数据库里面加载的
            model=service.find(modelClass,model.getId());
            
            //数据版本控制，防止多个用户同时修改一条数据，造成更新丢失问题
            if(version==null){
                log.info("前台界面没有传递版本信息");
                throw new RuntimeException("您的数据没有版本信息");
            }else{
                log.info("前台界面传递了版本信息,version="+version);
            }
            if(version!=model.getVersion()){
                log.info("当前数据的版本为 "+model.getVersion()+",您的版本为 "+version);
                throw new RuntimeException("您的数据已过期，请重新修改");
            }
            
            old(model);
            for(Property property : properties){
                //把从浏览器传来的值射入model
                if(property.getName().contains(".")){
                     //处理两个对象之间的引用，如：model.org.id=1
                     if(property.getName().contains(".id")){
                        String[] attr=property.getName().replace(".",",").split(",");
                        if(attr.length==2){
                            Field field=ReflectionUtils.getDeclaredField(model, attr[0]);
                            T change=service.find((Class<T>)field.getType(), (Integer)property.getValue());
                            ReflectionUtils.setFieldValue(model, attr[0], change);
                        }
                     }
                }
                else{
                    ReflectionUtils.setFieldValue(model, property.getName(), property.getValue());
                }
            }
            now(model);
            //在更新前调用模板方法对模型进行处理
            assemblyModelForUpdate(model);
            service.update(model);
            afterSuccessPartUpdateModel(model);
        }catch(Exception e){
            log.error("更新模型失败",e);
            afterFailPartUpdateModel(model);
            map=new HashMap();
            map.put("success", false);
            map.put("message", "修改失败 "+e.getMessage());
            Struts2Utils.renderJson(map);
            return null;
        }
        map=new HashMap();
        map.put("success", true);
        map.put("message", "修改成功");
        Struts2Utils.renderJson(map);
        return null;
	}

	@Override
	public String updateForm() {
		// TODO Auto-generated method stub
		return null;
	}

	public void prepareForDelete(Integer[] ids) {

	}

	@Override
	public String delete() {
		// TODO Auto-generated method stub
		try {
			prepareForDelete(getIds());
			List<Integer> deletedIds = service.delete(modelClass, getIds());
			afterDelete(deletedIds);
		} catch (Exception e) {
			log.info("删除数据出错", e);
			Struts2Utils.renderText(e.getMessage());
			return null;
		}
		Struts2Utils.renderText("删除成功");
		return null;
	}

	public void afterDelete(List<Integer> deletedIds) {

	}

	protected void beforeQuery() {

	}

    protected void afterRender(Map map,T obj){
        
    }
    protected String generateReportData(List<T> models){
        return null;
    }
	@Override
	public String query() {
		// TODO Auto-generated method stub
		beforeQuery();
		if (search) {

			//log.info("search ing");

			search();
			return null;
		}
		//getPageCriteria().getPage();
		//log.info("start:"+getStart()+"limit:"+getLimit());
		this.setPageData(service.query(modelClass, getPageCriteria(),
				buildPropertyCriteria(), buildOrderCriteria()));
		Map json = new HashMap();
		json.put("totalProperty", pageData.getTotalRecords());
		List<Map> result = new ArrayList<Map>();
		renderJsonForSearch(result);
		json.put("root", result);
		Struts2Utils.renderJson(json);

		return null;
	}

	protected void renderJsonForfind(Map map) {
		render(map, model);
	}

	protected void findAfterRender(Map map, T obj) {

	}

	protected void renderJsonForSearch(List result) {
		renderJsonForQuery(result);
	}

	/**
	 * 渲染需要在页面【表格】中显示的字段
	 * 
	 * @return
	 */
	protected void renderJsonForQuery(List result) {
		for (T obj : pageData.getModels()) {

			

			Map temp = new HashMap();
			render(temp, obj);
			// afterRender(temp,obj);
			result.add(temp);
		}
	}

	protected void render(Map map, T obj) {
		// 获取所有字段，包括继承的
		List<Field> fields = ReflectionUtils.getDeclaredFields(model);
		for (Field field : fields) {
			if (field.isAnnotationPresent(RenderIgnore.class)) {
				continue;
			}
			addFieldValue(obj, field, map);
		}
	}
	
	 public String chart(){
	        if(StringUtils.isNotBlank(getQueryString())){
	            //搜索出所有数据   
	            beforeSearch();
	            pageData=service.query(modelClass, null, getPc());
	            List<T> models=processSearchResult(pageData.getModels());
	            pageData.setModels(models);
	        }else{
	            beforeQuery();
	            this.setPageData(service.query(modelClass));
	        }
	        //生成报表XML数据
	        String data=generateReportData(pageData.getModels());
	        if(StringUtils.isBlank(data)){
	            log.info("生成的报表数据为空");
	            return null;
	        }
	        Struts2Utils.renderXml(data);
	        return null;
	    }

	protected String exportFileName() {
		return model.getMetaData() + ".xls";
	}

	protected void renderForExport(List<List<String>> result) {
		List<String> data = new ArrayList<>();
		// 获取所有字段，包括继承的
		List<Field> fields = ReflectionUtils.getDeclaredFields(model);
		for (Field field : fields) {
			if (field.isAnnotationPresent(ModelAttr.class)) {
				ModelAttr attr = field.getAnnotation(ModelAttr.class);
				String fieldAttr = attr.value();
				data.add(fieldAttr);
			}
		}
		result.add(data);
		for (T obj : pageData.getModels()) {
			data = new ArrayList<>();
			renderDataForExport(data, obj);
			result.add(data);
		}
	}

	private void renderDataForExport(List<String> data, T obj) {
		// 获取所有字段，包括继承的
		List<Field> fields = ReflectionUtils.getDeclaredFields(obj);

		for (Field field : fields) {
			if (field.isAnnotationPresent(ModelAttr.class)) {
				// 导出的时候，如果是复杂类型，则忽略*_id属性
				Map<String, String> temp = new HashMap<>();
				addFieldValue(obj, field, temp);
				// 复杂类型对应两个值
				temp.remove(field.getName() + "_id");
				data.addAll(temp.values());
			}
		}
	}

	private void addFieldValue(T obj, Field field, Map<String, String> data) {
		String fieldName = field.getName();
		try {
			if (field.isAnnotationPresent(Lob.class)) {
				log.debug("字段[" + fieldName + "]为大对象，忽略生成JSON字段");
				return;
			}
			Object value = ReflectionUtils.getFieldValue(obj, field);
			if (value == null) {
				data.put(fieldName, "");
				return;
			}
			// 处理集合类型
			if (field.isAnnotationPresent(ModelCollRef.class)) {
				ModelCollRef ref = field.getAnnotation(ModelCollRef.class);
				String fieldRef = ref.value();
				Collection col = (Collection) value;
				String colStr = "";
				if (col != null) {
					log.debug("处理集合,字段为：" + field.getName() + ",大小为："
							+ col.size());
					if (col.size() > 0) {
						StringBuilder str = new StringBuilder();
						for (Object m : col) {
							str.append(
									ReflectionUtils.getFieldValue(m, fieldRef)
											.toString()).append(",");
						}
						str = str.deleteCharAt(str.length() - 1);
						colStr = str.toString();
					}
				} else {
					log.debug("处理集合失败，" + value + " 不能转换为集合");
				}
				data.put(fieldName, colStr);
				return;
			}
			// 处理复杂对象类型
			if (field.isAnnotationPresent(ModelAttrRef.class)) {
				log.debug("处理对象,字段为：" + field.getName());
				ModelAttrRef ref = field.getAnnotation(ModelAttrRef.class);
				String fieldRef = ref.value();
				// 加入复杂对象的ID
				Object id = ReflectionUtils.getFieldValue(value, "id");
				data.put(fieldName + "_id", id.toString());
				// 因为是复杂对象，所以变换字段名称
				fieldName = fieldName + "_" + fieldRef;
				// 获取fieldRef的值
				value = ReflectionUtils.getFieldValue(value, fieldRef);
			}
			if (value.getClass() == null) {
				data.put(fieldName, "");
				return;
			}
			String valueClass = value.getClass().getSimpleName();

			if ("PersistentBag".equals(valueClass)) {
				value = "";
			}
			if ("Timestamp".equals(valueClass) || "Date".equals(valueClass)) {
				value = DateTypeConverter.toDefaultDateTime((Date) value);
			}
			// 处理下拉菜单
			if ("DicItem".equals(valueClass)) {
				value = ReflectionUtils.getFieldValue(value, "name");
			}
			data.put(fieldName, value.toString());
		} catch (Exception e) {
			log.error("获取字段值失败", e);
		}
	}

	protected void beforeSearch() {

	}

	@Override
	public String search() {
		// TODO Auto-generated method stub
		beforeSearch();
		// pageData=service.search(modelClass, getPageCriteria(),
		// getQueryString());
		// List<T> models=processSearchResult(page.getModels());
		// page.setModels(models);

		pageData = service.query(modelClass, getPageCriteria(), getPc());

		Map json = new HashMap();
		json.put("totalProperty", pageData.getTotalRecords());
		List<Map> result = new ArrayList<>();
		renderJsonForSearch(result);
		json.put("root", result);
		Struts2Utils.renderJson(json);
		return null;
	}

	protected void objectReference(T model) {
		Field[] fields = model.getClass().getDeclaredFields();// 获得对象方法集合
		for (Field field : fields) {// 遍历该数组
			if (field.isAnnotationPresent(ManyToOne.class)
					|| field.isAnnotationPresent(OneToOne.class)) {
				log.debug(model.getMetaData() + " 有ManyToOne 或 OneToOne映射，字段为"
						+ field.getName());
				Model value = (Model) ReflectionUtils.getFieldValue(model,
						field);
				if (value == null) {
					log.debug(model.getMetaData() + " 的字段" + field.getName()
							+ "没有值，忽略处理");
					continue;
				}
				int id = value.getId();
				log.debug("id: " + id);
				value = service.find(value.getClass(), id);
				ReflectionUtils.setFieldValue(model, field, value);
			}
		}
	}

	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}

	public Page<T> getPageData() {
		return pageData;
	}

	public void setPageData(Page<T> pageData) {
		this.pageData = pageData;
	}

	public T getModel() {
		return model;
	}

	public void setModel(T model) {
		this.model = model;
	}

}
