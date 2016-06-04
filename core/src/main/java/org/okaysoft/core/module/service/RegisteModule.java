package org.okaysoft.core.module.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.okaysoft.core.criteria.Order;
import org.okaysoft.core.criteria.OrderCriteria;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.Sequence;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.service.PropertyHolder;
import org.okaysoft.core.service.RegisterService;
import org.okaysoft.core.utils.FileUtils;
import org.springframework.stereotype.Service;










@Service
public class RegisteModule extends RegisterService<Module> {
	
	private List<Module> data;
	@Resource(name = "moduleService")
    private  ModuleService moduleService;
	private boolean registed=false;
	
	private Module rootModule;

	@Override
	protected void registe() {
		// TODO Auto-generated method stub
		data=new ArrayList<Module>();
		
        List<Module> modules=ModuleParser.getRootModules();
        for(Module module : modules){
            registeModule(module);
        }
	}
	
	
	
	@Override
	protected void registeSuccess() {
		// TODO Auto-generated method stub
		if(shouldRegister()){
			log.info("模块、命令数据注册完毕，重新初始化权限信息");
		}else{
			log.info("模块、命令数据没有变化");
		}
		log.info("重新生成/platform/css/module.css");
        reGenerateModuleCss();
        log.info("重新生成/platform/css/operation.css");
        reGenerateCommandCss();
	}



	@Override
	protected boolean shouldRegister() {
		// TODO Auto-generated method stub
		return true;
	}



	private void registeModule(Module module){
		//moduleService.getRootModule();
		Page<Module> pageData=serviceFacade.query(Module.class);
        if(pageData.getTotalRecords()==0){
            log.info("第一次注册第一个模块: "+module.getChinese());            
            serviceFacade.create(module);
            registed=true;
            //保存根模块
            rootModule=module;
            data.add(module);
        }else{
        	log.info("以前已经注册过模块");
            log.info("查找出根模块");
            Module root=null;
            Module existRoot=moduleService.getRootModule();
            if(existRoot!=null){
                root=existRoot;
                log.info("找到以前导入的根模块: "+root.getChinese());
            }else{
                log.info("没有找到以前导入的根模块");
                if(rootModule!=null){
                    log.info("使用本次导入的根模块");
                    root=rootModule;
                }
            }
            
            if(root!=null){
            	log.info("将第一次以后的模块的根模块设置为第一次注册的根模块");
                for(Module subModule : module.getSubModules()){
                    if(hasRegisteModule(subModule)){
                        log.info("模块 "+subModule.getChinese()+" 在此前已经被注册过，此次忽略，检查其子模块");                        
                        registeSubModule(subModule);
                        continue;
                    }
                    //确保后续注册的模块的顺序在最后
                    subModule.setOrderNum(subModule.getOrderNum()+(int)pageData.getTotalRecords());
                    subModule.setParentModule(root);
                    log.info("注册后续模块: "+subModule.getChinese());
                    serviceFacade.create(subModule);
                    registed=true;
                    data.add(subModule);
                }
            }else{
                log.info("没有找到根模块，注册失败！");
            }
        }
	}
	
	private void registeSubModule(Module module){        
        log.info("模块 "+module.getChinese()+" 在此前已经被注册过，检查其命令");
        chechCommand(module);
        for(Module sub : module.getSubModules()){
            if(hasRegisteModule(sub)){
                log.info("模块 "+sub.getChinese()+" 在此前已经被注册过，此次忽略，检查其子模块");
                registeSubModule(sub);
            }else{
                log.info("注册后续模块: "+sub.getChinese());
                //重新从数据库中查询出模块，参数中的模块是从XML中解析出来的
                sub.setParentModule(moduleService.getModule(module.getEnglish()));
                serviceFacade.create(sub);
                registed=true;
                data.add(sub);
            }
        }
    }
	
	
	private void chechCommand(Module module){
        Set<String> existCommands=new HashSet<String>();
        Module existsModule=moduleService.getModule(module.getEnglish());
        if(existsModule!=null){
            for(Command command : existsModule.getCommands()){
                existCommands.add(command.getEnglish());
            }
            for(Command command : module.getCommands()){
                if(!existCommands.contains(command.getEnglish())){
                    command.setModule(existsModule);
                    serviceFacade.create(command);
                    log.info("注册新增命令: "+command.getChinese()+" ,模块："+existsModule.getChinese());
                }
            }
        }
    }
	
	private boolean hasRegisteModule(Module module){
        Module existsModule=moduleService.getModule(module.getEnglish());
        if(existsModule==null){
            return false;
        }
        return true;
    }

	@Override
	protected List<Module> getRegisteData() {
		// TODO Auto-generated method stub
		return data;
	}
	
	@PostConstruct
    private void reGenerateModuleCss(){
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.addOrder(new Order("id", Sequence.ASC));
        //查出所有模块
        List<Module> modules=serviceFacade.query(Module.class,null,null,orderCriteria).getModels();
        StringBuilder css=new StringBuilder();
        css.append("/* 自动生成的文件，请不要修改 */");
        for(Module module : modules){
            if("root".equals(module.getEnglish().trim())){
                //忽略根模块
                continue;
            }
            String path = ModuleService.getModulePath(module);
            path=path.substring(0, path.length()-1);
            css.append(".")
                .append(module.getEnglish())
                .append("{")
                .append("background-image: url(../images/module/")
                .append(path)
                .append(".png) !important;")
                .append("}");
        }
        FileUtils.createAndWriteFile("/src/main/webapp/platform/css/module.css", css.toString());
        log.info("module css:"+css);
    }

	@PostConstruct
    private void reGenerateCommandCss() {
        OrderCriteria orderCriteria = new OrderCriteria();
        orderCriteria.addOrder(new Order("id", Sequence.ASC));
        //查出所有模块
        List<Command> list=serviceFacade.query(Command.class,null,null,orderCriteria).getModels();
        Set<String> commandCsses=new HashSet<String>();
        for(Command command : list){
            //在module.xml中配置的命令可能会对于前台的几个按钮
            String dependency = PropertyHolder.getProperty("command." + command.getEnglish());
            String[] commands = null;
            if (StringUtils.isNotBlank(dependency)) {
                commands = dependency.split(",");
            } else {
                commands = new String[]{command.getEnglish()};
            }
            commandCsses.addAll(Arrays.asList(commands));
        }
        StringBuilder css=new StringBuilder();
        css.append("/* 自动生成的文件，请不要修改 */");
        for(String commandCss : commandCsses){
            css.append(".")
                .append(commandCss)
                .append("{")
                .append("background-image: url(../images/operation/")
                .append(commandCss)
                .append(".png) !important;")
                .append("}");
        }
        FileUtils.createAndWriteFile("/src/main/webapp/platform/css/operation.css", css.toString());
        log.info("operation css:"+css);
    }
	
}
