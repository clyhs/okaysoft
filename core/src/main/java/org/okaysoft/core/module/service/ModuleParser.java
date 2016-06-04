package org.okaysoft.core.module.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.module.model.Command;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.utils.FileUtils;
import org.okaysoft.core.utils.XMLFactory;
import org.okaysoft.core.utils.XmlUtils;









public class ModuleParser {
	
	protected static final OkayLogger log  = new OkayLogger(ModuleParser.class);
	
	private static final String dtdFile = "target/module.dtd";
	private static final String disableModules = "";
	
	/**
     * 获取各个module.xml中的根模块
     * @return 
     */
	public static List<Module> getRootModules(){
		
		log.info("module.disable:"+disableModules);
		
		List<Module> modules = new ArrayList<Module>();
		
		try {
			Enumeration<URL> ps = Thread.currentThread().getContextClassLoader().getResources("init/module.xml");
			while(ps.hasMoreElements()) {
				InputStream in = null;
                try {
                    URL url=ps.nextElement();
                    log.info("找到模块描述文件："+url.getPath());
                    in = url.openStream();
                    Module root=getRootModule(in);
                    modules.add(root);
                }catch(Exception e)
                {
                    log.error("获取根模块出错",e);
                }
                finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("获取根模块出错",e);
                    }
                }
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取根模块出错",e);
		}
		
		return modules;
	}
	
	/**
     * 解析并获得根模块
     * @param in
     * @return 
     */
	public static Module getRootModule(InputStream in){
		
		prepareDtd();
		
		try{
			byte[] data=FileUtils.readAll(in);
            String xml=new String(data,"utf-8");
            log.info("将DTD文件替换为绝对路径");
            xml=xml.replace("module.dtd", FileUtils.getAbsolutePath(dtdFile));
            log.info("将模块文件读取到字节数组中，长度为："+data.length);
            ByteArrayInputStream bin=new ByteArrayInputStream(xml.getBytes("utf-8"));    
            log.info("注册module.xml文件");
            log.info("验证module.xml文件");
            
          //校验文件
            verifyFile(bin);
            // 解析模块
            Module module=parseModule(xml);
            return module;
            
		}catch(UnsupportedEncodingException e){
			log.error("获取根模块出错",e);
		}
		
		return null;
	}
	
	private static Module parseModule(String xml) {
        XMLFactory factory=new XMLFactory(Module.class,Command.class);
        Module module=factory.unmarshal(xml);
        //将XML表示的模块转变为JAVA对象表示的模块
        assembleMudule(module);
        return module;
    }
	
	private static void prepareDtd(){
		try {
			Enumeration<URL> ps = Thread.currentThread().getContextClassLoader().getResources("init/module.dtd");
			if(ps.hasMoreElements()) {
				InputStream in = null;
                try {
                    URL url=ps.nextElement();
                    log.info("找到模块DTD文件："+url.getPath());
                    in = url.openStream();
                    byte[] data=FileUtils.readAll(in);
                    log.info("将DTD复制到："+dtdFile);
                    FileUtils.createAndWriteFile(dtdFile, data);
                }catch(Exception e)
                {
                    log.error("获取根模块出错",e);
                }
                finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error("获取根模块出错",e);
                    }
                }
			}else{
				log.info("没有找到模块DTD文件");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取根模块出错",e);
		}
	}
	
	/**
     * 采用递归的方式建立模块树的引用关系
     * @param module  模块树的根
     */
    private static void assembleMudule(Module module){
    	int order=1;
        for(Command c : module.getCommands()){
            c.setModule(module);
            if(c.getOrderNum()==0){
                c.setOrderNum(order++);
            }
        }
        order=1;
        List<Module> toDelete=new ArrayList<>();
        for(Module m : module.getSubModules()){            
            //根据参数module.hide来设置模块
        	
            if(disableModules.contains(m.getEnglish())){
                log.info("禁用: "+m.getChinese());
                toDelete.add(m);                
                continue;
            }else{
                log.info("启用: "+m.getChinese());
            }
            m.setParentModule(module);
            if(m.getOrderNum()==0){
                m.setOrderNum(order++);
            }
            assembleMudule(m);
        }
        for(Module m : toDelete){
            module.removeSubModule(m);
        }
    }
    
    private static void verifyFile(InputStream in){    
        boolean pass=XmlUtils.validateXML(in);
        if(!pass){
            log.info("验证没有通过，请参考module.dtd文件");
            return ;
        }
        log.info("验证通过");
    }

}
