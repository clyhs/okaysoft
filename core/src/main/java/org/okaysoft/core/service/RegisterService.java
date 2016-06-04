package org.okaysoft.core.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.okaysoft.core.criteria.Operator;
import org.okaysoft.core.criteria.Page;
import org.okaysoft.core.criteria.PropertyCriteria;
import org.okaysoft.core.criteria.PropertyEditor;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.model.Model;
import org.okaysoft.core.model.ModelMetaData;
import org.okaysoft.core.module.model.Module;
import org.okaysoft.core.utils.FileUtils;
import org.okaysoft.core.utils.ReflectionUtils;
import org.okaysoft.core.utils.XMLFactory;
import org.okaysoft.core.utils.XmlUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;



public abstract class RegisterService <T extends Model> implements ApplicationListener {
	
	protected final OkayLogger log = new OkayLogger(getClass());
	
	@Resource(name="serviceFacade")
    protected ServiceFacade serviceFacade;
    @Resource(name="entityManagerFactory")
    protected EntityManagerFactory entityManagerFactory;
    
    protected Class<T> modelClass;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// TODO Auto-generated method stub
		if(event instanceof ContextRefreshedEvent){
            this.modelClass = ReflectionUtils.getSuperClassGenricType(getClass());
            log.info("spring容器初始化完成, 开始检查 "+ModelMetaData.getMetaData(this.modelClass.getSimpleName()) +" 是否需要初始化数据");
            if(shouldRegister()){
                log.info("需要初始化 "+ModelMetaData.getMetaData(this.modelClass.getSimpleName()));
                openEntityManager();
                registe();
                closeEntityManager();
                registeSuccess();
            }else{
                log.info("不需要初始化 "+ModelMetaData.getMetaData(this.modelClass.getSimpleName()));
            }
        }
	}
	
	private void openEntityManager(){        
        EntityManager em = entityManagerFactory.createEntityManager();
        TransactionSynchronizationManager.bindResource(entityManagerFactory, new EntityManagerHolder(em));
        log.info("打开实体管理器");
    }
    private void closeEntityManager(){
        EntityManagerHolder emHolder = (EntityManagerHolder)TransactionSynchronizationManager.unbindResource(entityManagerFactory);
        log.info("关闭实体管理器");
        EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
    }
    
    protected void registeSuccess(){
        
    }
    protected List<T> getRegisteData(){
        return null;
    }
    protected abstract void registe();

    protected boolean shouldRegister() {
        Page<T> pageData=serviceFacade.query(modelClass);
        if(pageData.getTotalRecords()==0) {
            return true;
        }
        return false;
    }
    
    protected String process(Class<T> model,String xmlFile){
    	
    	log.info("注册【"+model.getSimpleName().toLowerCase()+" "+xmlFile+"】文件");
    	
    	String dtdFile = xmlFile.replace(".xml", ".dtd");
    	
    	String xmlPath = "";
		String dtdPath = "";
		
		String copyDtdFile = "target/"+model.getSimpleName().toLowerCase()+".dtd";
		
		try {
			Enumeration<URL> ps_xml = Thread.currentThread().getContextClassLoader().getResources(xmlFile);
			Enumeration<URL> ps_dtd = Thread.currentThread().getContextClassLoader().getResources(dtdFile);
			if(ps_xml.hasMoreElements() && ps_dtd.hasMoreElements()) {
				InputStream in = null;
				URL url_xml=ps_xml.nextElement();
				URL url_dtd=ps_dtd.nextElement();
				
				xmlPath = url_xml.getPath();
				dtdPath = url_dtd.getPath();
				log.info(model.getSimpleName().toLowerCase()+".xml的路径为："+xmlPath);
				log.info(model.getSimpleName().toLowerCase()+".dtd的路径为："+dtdPath);
				in = url_xml.openStream();
				byte[] data=FileUtils.readAll(in);
				byte[] dtdData = FileUtils.readAll(url_dtd.openStream());
				FileUtils.createAndWriteFile(copyDtdFile, dtdData);		
	            String xml=new String(data,"utf-8");
	            log.info(new String(data,"utf-8"));
	            xml=xml.replace(model.getSimpleName().toLowerCase()+".dtd", FileUtils.getAbsolutePath(copyDtdFile));
	            ByteArrayInputStream bin=new ByteArrayInputStream(xml.getBytes("utf-8"));
	            verifyFile(bin,model.getSimpleName().toLowerCase()+".dtd");   
	            return xml;
	            
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(model.getSimpleName().toLowerCase()+"模块注册出错",e);
		}
		return null;
    }
    
    private  void verifyFile(InputStream in,String dtdFile){    
        boolean pass=XmlUtils.validateXML(in);
        if(!pass){
            log.info("验证没有通过，请参考"+dtdFile+"文件");
            return ;
        }
        log.info("验证通过");
    }

}
