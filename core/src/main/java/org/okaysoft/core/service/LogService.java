package org.okaysoft.core.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.model.Model;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	
	protected static final OkayLogger log = new OkayLogger(LogService.class);
	
	@Resource(name = "serviceFacade")
    private ServiceFacade serviceFacade;
    private static ConcurrentLinkedQueue <Model> logs =  new  ConcurrentLinkedQueue <>();
    private static int logQueueMax=Integer.parseInt(PropertyHolder.getProperty("logQueueMax"));
    public static synchronized void addLog(Model log){
        logs.add(log);
        if(logs.size()>logQueueMax){
            queue.saveLog();
        }
    }
    private static LogService queue=null;
    public static LogService getLogQueue(){
        return queue;
    }
    @PostConstruct
    public void execute(){
        queue=this;
    }
    public synchronized void saveLog(){
        int len=logs.size();
        int success=0;
        log.info("保存前队列中的日志数目为(Num. of log before saving in the queue)："+len);
        try{
            for(int i=0;i<len;i++){
                Model model = logs.remove();
                try{
                    serviceFacade.create(model);
                    success++;
                }catch(Exception e){
                    log.error("保存日志失败(Failed to save log):"+model.getMetaData(),e);
                }
            }
        } catch (Exception e) {
            log.error("保存日志抛出异常(Saving log exception)",e);
        }
        log.info("成功保存(Success to save) "+success+" 条日志(log)");
        log.info("保存后队列中的日志数目为(Num. of log after saving in the queue)："+logs.size());
    }

}
