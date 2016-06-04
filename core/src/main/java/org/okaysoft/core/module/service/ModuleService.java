package org.okaysoft.core.module.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.annotation.Resource;

import org.okaysoft.core.action.ExtJSSimpleAction;
import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.security.service.SpringSecurityService;
import org.okaysoft.core.security.service.UserHolder;
import org.okaysoft.core.service.PropertyHolder;
import org.okaysoft.core.service.ServiceFacade;
import org.okaysoft.core.utils.PrivilegeUtils;
import org.springframework.stereotype.Service;




@Service
public class ModuleService {
	
	protected static final OkayLogger log = new OkayLogger(ModuleService.class);
	
	@Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;
	
	public ModuleService(){
		//log.info("module service");
	}
	
	public Module getRootModule(){
		
		PropertyEditor propertyEditor = new PropertyEditor("english", Operator.eq, "root");
		PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(propertyEditor);
        List<Module> roots = serviceFacade.query(Module.class, null, propertyCriteria).getModels();
        if(roots!=null && roots.size()==1){
            return roots.get(0);
        }
        log.error("有多个根模块!");
    
		
		return null;
	}
	
	public Module getModule(String english){
		PropertyEditor propertyEditor = new PropertyEditor("english", Operator.eq, english);

        PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(propertyEditor);

        List<Module> page = serviceFacade.query(Module.class, null, propertyCriteria).getModels();
        if (page.isEmpty()) {
            return null;
        }
        return page.get(0);
	}
	
	public Module getModule(int id) {
        PropertyEditor propertyEditor = new PropertyEditor("id", Operator.eq, Integer.toString(id));

        PropertyCriteria propertyCriteria = new PropertyCriteria();
        propertyCriteria.addPropertyEditor(propertyEditor);

        List<Module> page = serviceFacade.query(Module.class, null, propertyCriteria).getModels();
        if (page.isEmpty()) {
            log.error("没有找到ID等于" + id + "的模块");
            return null;
        }
        return page.get(0);
    }
	
	
    public String toJsonForPrivilege(Module module) {
        ModuleFilter filter = new ModuleFilter() {

            @Override
            public void filter(List<Module> subModules) {
            }

            @Override
            public boolean script() {
                return false;
            }

            @Override
            public boolean recursion() {
                return true;
            }

            @Override
            public boolean command() {
                return true;
            }
        };
        return toJson(module, filter);
    }


    public String toJsonForEdit(Module module) {
        ModuleFilter filter = new ModuleFilter() {

            @Override
            public void filter(List<Module> subModules) {
            }

            @Override
            public boolean script() {
                return false;
            }

            @Override
            public boolean recursion() {
                return false;
            }

            @Override
            public boolean command() {
                return true;
            }
        };
        return toJson(module, filter);
    }
    
    
    
    public String toRootJsonForEdit(){
        Module m=getRootModule();
        if(m==null){
            log.error("获取根功能菜单失败！");
            return "";
        }
        StringBuilder json=new StringBuilder();
        json.append("[{'text':'").append(m.getChinese()).append("','id':'module-").append(m.getId()).append("','iconCls':'").append(m.getEnglish()).append("','leaf':false}]");
        
        return json.toString();
    }


    public String toJsonForUser(Module module, final boolean recursion) {
        ModuleFilter filter = new ModuleFilter() {

            @Override
            public void filter(List<Module> subModules) {
                if (SpringSecurityService.isSecurity()) {
                    securityControl(subModules);
                }
                displayControl(subModules);
            }

            @Override
            public boolean script() {
                return true;
            }

            @Override
            public boolean recursion() {
                return recursion;
            }

            @Override
            public boolean command() {
                return false;
            }
        };
        return toJson(module, filter);
    }

	public String toJson(Module module, ModuleFilter filter) {
		StringBuilder json = new StringBuilder();
        List<Module> subModules = module.getSubModules();

        if (filter != null) {
            //是否对模块及命令进行移除交给模块控制器来做
            filter.filter(subModules);
        }
               
        if (subModules.size() > 0) {
        	
            json.append("[");
            for (Module m : subModules) {
                json.append("{'text':'").append(m.getChinese()).append("','id':'module-").append(m.getId()).append("','iconCls':'").append(m.getEnglish()).append("'");
                if (filter.recursion()) {
                    json.append(",children:").append(toJson(m, filter));
                }
                if (m.getSubModules().size() > 0) {
                    json.append(",'leaf':false");
                } else {
                    if (filter.command()) {
                        json.append(",'leaf':false");
                    } else {
                        json.append(",'leaf':true");
                    }
                    if (filter.script()) {
                        json.append(",listeners:{'click':function(node,event){view.getCenterPanel().openTab(node,event,eval('").append(m.getUrl()).append("'))}}");
                    }
                }
                json.append("},");
            }
            json = json.deleteCharAt(json.length() - 1);
            json.append("]");
        }else {
        
            if (filter.command()) {
                List<Command> commands = module.getCommands();
                if (commands.size() > 0) {
                    json.append("[");
                    for (Command c : commands) {
                        json.append("{'text':'").append(c.getChinese()).append("','id':'command-").append(c.getId()).append("','iconCls':'").append(c.getEnglish()).append("','leaf':true").append("},");
                    }
                    json = json.deleteCharAt(json.length() - 1);
                    json.append("]");
                }
            }
        }
        //log.info(json.toString());
        
		return json.toString();
	}
	
	
	public String toJsonForTest(Module module,final boolean recursion) {
		ModuleFilter filter = new ModuleFilter() {

            @Override
            public void filter(List<Module> subModules) {
            }

            @Override
            public boolean script() {
                return true;
            }

            @Override
            public boolean recursion() {
                return recursion;
            }

            @Override
            public boolean command() {
                return false;
            }
        };
        return toJson(module, filter);
	}
	
	
	/**
     * 去除没有分配给用户的模块
     * @param modules
     * @param commands
     */
    public void securityControl(List<Module> modules) {
    	
    	
        User user = UserHolder.getCurrentLoginUser();
        //重新装载用户，消除由于不同会话间延迟加载的问题
    
        
        user = serviceFacade.find(User.class, user.getId());
        List<Module> userModules = user.getModule();
        
        Iterator<Module> moduleIterator = modules.iterator();
        while (moduleIterator.hasNext()) {
            Module m = moduleIterator.next();
            //把没有分配给用户的模块去掉
            boolean contains = false;
            for (Module userModule : userModules) {
                if (m.getId() == userModule.getId()) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                moduleIterator.remove();
            }
        }
    }
    
    /**
     * 去除隐藏的模块
     * @param modules
     * @param commands
     */
    public void displayControl(List<Module> modules) {
    	
        Iterator<Module> moduleIterator = modules.iterator();
        while (moduleIterator.hasNext()) {
            Module m = moduleIterator.next();
            //去掉隐藏的模块
            if (!m.isDisplay()) {
                moduleIterator.remove();
                continue;
            }
        }
    }
	
	public static Map<String, String> getCommandPathToRole(Command command) {
        Map<String, String> result = new HashMap<>();
        for (String path : getCommandPath(command)) {
            String role = path.toString().substring(3).replace("/", "_").replace("!", "_").toUpperCase();
            result.put(path, role);
        }

        return result;
    }
	
	public static List<String> getCommandPath(Command command) {
        List<String> result = new ArrayList<>();
        String dependency = PropertyHolder.getProperty("command." + command.getEnglish());
        String[] commands = null;
        if (dependency != null && !"".equals(dependency.trim())) {
            commands = dependency.split(",");
        } else {
            commands = new String[]{command.getEnglish()};
        }
        Module module = command.getModule();
        //上下文不把大写字母转换为小写字母+_
        //String modulePath = PrivilegeUtils.process(getModulePath(module.getParentModule()));
        String modulePath = getModulePath(module.getParentModule());
        String moduleName = PrivilegeUtils.process(module.getEnglish());
        for (String cmd : commands) {
            StringBuilder path = new StringBuilder();
            path.append("/**/").append(modulePath).append(moduleName).append("!").append(cmd);
            result.add(path.toString());
        }
        return result;
    }
	
	public static String getModulePath(Module module) {
        StringBuilder str = new StringBuilder();
        Stack<Module> stack = new Stack<Module>();
        getModules(module, stack);
        int len = stack.size();
        for (int i = 0; i < len; i++) {
            str.append(stack.pop().getEnglish()).append("/");
        }
        return str.toString();
    }

	
	
	private static void getModules(Module module, Stack<Module> stack) {
        //将当前模块加入堆栈
        stack.push(module);
        Module parent = module.getParentModule();
        //当还有父模块并且父模块不为根模块的时候，把父模块也加入堆栈
        if (parent != null && !"root".equals(parent.getEnglish())) {
            getModules(parent, stack);
        }
    }
	
	
	/**
     * 比较module.xml中定义的命令和ExtJSSimpleAction中默认提供的命令
     * 返回在在module.xml中指定而在ExtJSSimpleAction中没有默认提供的命令
     * @param module
     * @return 
     */
    public static List<Command> getSpecialCommand(Module module) {
    	List<Command> special = new ArrayList<Command>();
        List<String> commons = new ArrayList<String>();
        for (Method method : ExtJSSimpleAction.class.getMethods()) {
            commons.add(method.getName());
            log.info("common method: " + method.getName());
        }
        for (Command command : module.getCommands()) {
            String dependency = PropertyHolder.getProperty("command." + command.getEnglish());
            if (dependency != null && !"".equals(dependency.trim())) {
                for (String c : dependency.split(",")) {
                    if (!commons.contains(c)) {
                        log.info("被依赖的方法不存在，通常情况下，被依赖的方法应该是通用方法，应该存在");
                        log.info("不存在通用方法: " + c);
                    }
                }
            } else {
                if (!commons.contains(command.getEnglish())) {
                    special.add(command);
                    log.info("special method:"+command.getEnglish());
                }
            }
        }
        return special;
    }
	
	
	 /**
     * 将根模块转换为所有的模块的集合
     * @return 
     */
    public static List<Module> getAllModule(Module rootModule){
        List<Module> list=new ArrayList<>();
        list.add(rootModule);
        return getAllModule(list);
    }
    public static List<Module> getAllModule(List<Module> rootModules){
        List<Module> modules=new ArrayList<>();
        for(Module m : rootModules){
            moduleWalk(modules,m);
        }
        return modules;
    }
    private static void moduleWalk(List<Module> modules, Module module){
        modules.add(module);
        for(Module m : module.getSubModules()){
            moduleWalk(modules,m);
        }
    }
    
    /**
     * 根据英文名称获取模型，从XML文件中获取模块信息，不从数据库中获取，英文此方法主要供代码辅助生成使用
     * @param moduleEnglishName
     * @return 
     */
    public static Module getModuleFromXml(String moduleEnglishName){   
        for(Module module : getAllModule(ModuleParser.getRootModules())){
            if(moduleEnglishName.equals(module.getEnglish())){
                return module;
            }
        }
        return null;
    }
    
    /**
     * 获取叶子模块，每一个叶子模块都对应一个Action
     * @return 
     */
    public static List<Module> getLeafModule(Module rootModule){
        List<Module> leaf = new ArrayList<>(); 
        for(Module module : getAllModule(rootModule)){
            if(module.getCommands().isEmpty()){
                log.info(module.getChinese()+" 模块不是叶子模块");
            }else{
                leaf.add(module);
            }
        }
        return leaf;
    }

}
