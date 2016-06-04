package org.okaysoft.core.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.okaysoft.core.criteria.Criteria;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Order;
import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.PageCriteria;
import org.okaysoft.core.criteria.Property;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.criteria.Sequence;
import org.okaysoft.core.dao.DataPrivilegeControl;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.utils.ReflectionUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

@MappedSuperclass
public abstract class ActionSupport extends DataPrivilegeControl {

	protected final OkayLogger log = new OkayLogger(getClass());

	protected static final String LIST = "list";
	protected static final String FORM = "form";
	protected static final String INPUT = "input";
	protected static final String ERROR = "error";
	protected static final String SUCCESS = "success";
	protected static final String DETAIL = "detail";
	private Feedback feedback;
	private PageCriteria pageCriteria = new PageCriteria(1, 17);
	private String propertyCriteria;
	private String orderCriteria;
	
	private PropertyCriteria pc = new PropertyCriteria(Criteria.or);
	private OrderCriteria oc = new OrderCriteria();
	// 客户端传过来的构造好的查询字符串
	protected String queryString;
	private String modelName;
	// 三种获取批量ID的形式
	private Integer[] id;
	private String ids;
	private boolean allPage = false;

	protected static final OrderCriteria defaultOrderCriteria = new OrderCriteria();

	static {
		defaultOrderCriteria.addOrder(new Order("id", Sequence.DESC));
	}

	public Locale getLocale() {
		ActionContext ctx = ActionContext.getContext();
		if (ctx != null) {
			return ctx.getLocale();
		} else {
			log.debug("Action context not initialized");
			return null;
		}
	}

	public String execute() {
		return null;
	}

	protected String getDefaultModelName(Class clazz) {
		String modelClassName = ReflectionUtils.getSuperClassGenricType(clazz)
				.getSimpleName();
		return Character.toLowerCase(modelClassName.charAt(0))
				+ modelClassName.substring(1);
	}

	public Integer[] getIds() {
		if (ids != null && ids.contains("-") && ids.contains(",")) {
            Set<Integer> result = new HashSet<>();
            String[] idInfo = ids.split(",");
            for (String info : idInfo) {
                if (info.contains("-")) {
                    String[] inner = info.split("-");
                    int start = Integer.parseInt(inner[0]);
                    int end = Integer.parseInt(inner[1]);
                    if (start > end) {
                        int temp = start;
                        start = end;
                        end = temp;
                    }
                    for (int i = start; i < end + 1; i++) {
                        result.add(i);
                    }
                } else {
                    result.add(Integer.parseInt(info));
                }
            }
            return result.toArray(new Integer[result.size()]);
        }
        if (ids != null && ids.contains("-")) {
            String[] idInfo = ids.split("-");
            int start = Integer.parseInt(idInfo[0]);
            int end = Integer.parseInt(idInfo[1]);
            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
            }
            Set<Integer> result = new HashSet<>();
            for (int i = start; i < end + 1; i++) {
                result.add(i);
            }
            return result.toArray(new Integer[result.size()]);
        }
        if (ids != null && ids.contains(",")) {
            String[] idInfo = ids.split(",");
            Set<Integer> result = new HashSet<>();
            for (int i = 0; i < idInfo.length; i++) {
                result.add(Integer.parseInt(idInfo[i]));
            }
            return result.toArray(new Integer[result.size()]);
        }
        if (ids != null) {
            Integer[] result = new Integer[1];

            result[0] = Integer.parseInt(ids);

            return result;
        }

        return id;
	}

	public PropertyCriteria buildPropertyCriteria() {
		return null;
	}

	public OrderCriteria buildOrderCriteria() {
		return null;
	}

	public static String dealWithResult(String finalLocation,
			ActionInvocation invocation) {
		String namespace = invocation.getProxy().getNamespace();
		String action = invocation.getProxy().getActionName();
		String method = invocation.getProxy().getMethod();

		if (finalLocation.contains("_namespace_")) {
			finalLocation = finalLocation.replace("_namespace_", namespace);
		}
		if (finalLocation.contains("_action_")) {
			finalLocation = finalLocation.replace("_action_", action);
		}

		return finalLocation;
	}

	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	private Enumeration<?> getRequestParameterNames() {
		return getRequest().getParameterNames();
	}

	private String getRequestParameterValue(String par) {
		return getRequest().getParameter(par);
	}

	public boolean hasRequest() {
		if (getRequest() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 在更新一个特定的部分的Model之前对Model添加需要修改的属性
	 * 
	 * @return
	 */
	protected void assemblyModelForPartUpdate(List<Property> properties) {

	}

	protected <T extends Model> List<Property> getPartProperties(T model) {
		List<Property> properties = new ArrayList<>();

        Enumeration<?> pars = getRequestParameterNames();
        while (pars.hasMoreElements()) {
            String par = (String) pars.nextElement();
            if (par.startsWith("model.") && !par.equals("model.id")) {
                String prop = par.replace("model.", "");
                if(prop.contains(".")){
                     if(prop.contains(".id")){
                         //处理两个对象之间的引用，如：model.org.id=1
                        String[] attr=prop.replace(".",",").split(",");
                        if(attr.length==2){
                            Object obj=ReflectionUtils.getFieldValue(model, attr[0]);
                            properties.add(new Property(prop, ReflectionUtils.getFieldValue(obj, attr[1])));
                        }
                     }
                }
                else{
                    properties.add(new Property(prop, ReflectionUtils.getFieldValue(model, prop)));
                }
            }
        }
        assemblyModelForPartUpdate(properties);

        return properties;
	}

	protected void buildModel(Model model) {

	}

	protected User refreshUser(User user) {
		return user;
	}

	public Feedback getFeedback() {
		return feedback;
	}

	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}

	public boolean isAllPage() {
		
		return allPage;
	}

	public void setAllPage(boolean allPage) {
		this.allPage = allPage;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public void setPropertyCriteria(String propertyCriteria) {
		this.propertyCriteria = propertyCriteria;
	}

	public void setOrderCriteria(String orderCriteria) {
		this.orderCriteria = orderCriteria;
	}

	public PageCriteria getPageCriteria() {
		if(this.isAllPage()){
            return null;
        }
		return pageCriteria;
	}

	public void setPageCriteria(PageCriteria pageCriteria) {
		this.pageCriteria = pageCriteria;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getQueryString() {
		StringBuilder result = new StringBuilder();
		
		//log.info("before query :"+queryString);
		if (queryString != null && !queryString.trim().equals("")) {
			String[] props=queryString.trim().split(" ");
			for(int i=0;i<props.length-1;i++){
				
			}
		}

		return result.toString();
	}
	
	

	protected String getCustomQueryString(String queryString) {
		return queryString;
	}

	public void setId(Integer[] id) {
		this.id = id;
	}

	public PropertyCriteria getPc() {	
		if (queryString != null && !queryString.trim().equals("")) {
			String[] props=queryString.trim().split(" ");		
			log.info("length:"+props.length);		
			for(int i=0;i<props.length;i++){				
				String prop=props[i];
				String propName = prop.split(":")[0];
				String propValue = prop.split(":")[1];	
				log.info("propName:"+propName+"propValue:"+propValue);
				pc.addPropertyEditor(new PropertyEditor(propName, Operator.eq, "String",propValue));
			}
		}	
		return pc;
	}

	public void setPc(PropertyCriteria pc) {
		this.pc = pc;
	}

	public OrderCriteria getOc() {
		return oc;
	}

	public void setOc(OrderCriteria oc) {
		this.oc = oc;
	}
	
	

}
