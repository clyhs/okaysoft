package org.okaysoft.core.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.okaysoft.core.annotation.IgnoreLog;
import org.okaysoft.core.annotation.IgnoreUser;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.log.model.OperateLog;
import org.okaysoft.core.log.model.OperateLogType;
import org.okaysoft.core.security.model.User;
import org.okaysoft.core.security.service.UserHolder;
import org.okaysoft.core.service.LogService;
import org.okaysoft.core.spring.SystemListener;





public class ModelListener {
	
	protected static final OkayLogger log = new OkayLogger(ModelListener.class);
	
	private static final boolean create;
    private static final boolean delete;
    private static final boolean update;
    static{
    	create = false;
    	delete = true;
    	update = true;
    	if(create){
            log.info("启用添加数据日志(Enable add data log)");
        }else{
            log.info("禁用添加数据日志(Disable add data log)");
        }
        if(delete){
            log.info("启用删除数据日志(Enable delete data log)");
        }else{
            log.info("禁用删除数据日志(Disable delete data log)");
        }
        if(update){
            log.info("启用更新数据日志(Enable update data log)");
        }else{
            log.info("禁用更新数据日志(Disable update data log)");
        }
    	
    }
    
    @PrePersist
    public void prePersist(Model model){
    	User user=UserHolder.getCurrentLoginUser();
        if(user!=null && model.getOwnerUser()==null && !model.getClass().isAnnotationPresent(IgnoreUser.class)){
            //设置数据的拥有者
            model.setOwnerUser(user);
        }
        //设置创建时间
        model.setCreateTime(new Date());
    }
    
    @PostPersist
    public void postPersist(Model model){
    	log.info("create");
    	if(create){
            saveLog(model,OperateLogType.ADD);
        }
    }
    private void saveLog(Model model, String type){
        if(!model.getClass().isAnnotationPresent(IgnoreLog.class)){
            User user=UserHolder.getCurrentLoginUser();
            String ip=UserHolder.getCurrentUserLoginIp();
            OperateLog operateLog=new OperateLog();
            operateLog.setOwnerUser(user);
            operateLog.setLoginIP(ip);
            try {
                operateLog.setServerIP(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
            
            operateLog.setAppName(SystemListener.getContextPath());
            operateLog.setOperatingTime(new Date());
            operateLog.setOperatingType(type);
            operateLog.setOperatingModel(model.getMetaData());
            operateLog.setOperatingID(model.getId());
            LogService.addLog(operateLog);
        }
    }
    
    @PreRemove
    public void perRemove(Model model){
    	
    }
    
    @PostRemove
    public void postRemove(Model model){
    	log.info("delete");
    	if(delete){
            saveLog(model,OperateLogType.DELETE);
        }
    }
    
    @PreUpdate
    public void preUpdate(Model model){
    	//设置更新时间
        model.setUpdateTime(new Date());
    }
    
    @PostUpdate
    public void postUpdate(Model model){
    	log.info("update");
    	if(update){
            saveLog(model,OperateLogType.UPDATE);
        }
    }

}
