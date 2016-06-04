package org.okaysoft.core.monitor.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.okaysoft.core.action.converter.DateTypeConverter;
import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.monitor.model.RuningTime;
import org.okaysoft.core.utils.ConvertUtils;


public class RuningTimeChartDataService {
	
    protected static final OkayLogger log = new OkayLogger(RuningTimeChartDataService.class);
    
    public static LinkedHashMap<String,Long> getRuningSequence(List<RuningTime> models){
        LinkedHashMap<String,Long> data=new LinkedHashMap<>();
        if(models.size()<1){
            return data;
        }
        Collections.sort(models, new Comparator(){

            @Override
            public int compare(Object o1, Object o2) {
                RuningTime p1=(RuningTime)o1;
                RuningTime p2=(RuningTime)o2;
                return (int) (p1.getStartupTime().getTime()-p2.getStartupTime().getTime());
            }
        
        });
        for(int i=0;i<models.size();){
            RuningTime item = models.get(i);
            String key=DateTypeConverter.toDefaultDateTime(item.getStartupTime())+", 运行"+item.getRuningTimeStr();
            data.put(key, item.getRuningTime());
            i++;
            if(i<models.size()){
                RuningTime item2 = models.get(i);
                long stop=item2.getStartupTime().getTime()-item.getShutdownTime().getTime();
                key=DateTypeConverter.toDefaultDateTime(item.getShutdownTime())+", 停机"+ConvertUtils.getTimeDes(stop);
                data.put(key, -stop);
            }
        }
        
        return data;
    }
    
    public static LinkedHashMap<String,Long> getRuningRateData(List<RuningTime> models){
        LinkedHashMap<String,Long> data=new LinkedHashMap<>();
        if(models.size()<1){
            return data;
        }
        Collections.sort(models, new Comparator(){

            @Override
            public int compare(Object o1, Object o2) {
                RuningTime p1=(RuningTime)o1;
                RuningTime p2=(RuningTime)o2;
                return (int) (p1.getStartupTime().getTime()-p2.getStartupTime().getTime());
            }
        
        });
        RuningTime first=models.get(0);
        RuningTime latest=models.get(models.size()-1);
        log.debug("系统首次启动时间："+DateTypeConverter.toDefaultDateTime(first.getStartupTime()));
        log.debug("系统最后关闭时间："+DateTypeConverter.toDefaultDateTime(latest.getShutdownTime()));
        long totalTime=latest.getShutdownTime().getTime()-first.getStartupTime().getTime();
        log.debug("系统总时间："+latest.getShutdownTime().getTime()+"-"+first.getStartupTime().getTime()+"="+totalTime);
        long runingTime=0;
        for(RuningTime item : models){
            log.debug("      增加系统运行时间："+item.getRuningTime());
            runingTime+=item.getRuningTime();
        }
        log.debug("系统运行时间："+runingTime);
        long stopTime=totalTime-runingTime;
        log.debug("系统停机时间："+stopTime);
        data.put("运行时间", runingTime);
        data.put("停机时间", -stopTime);
        
        return data;
    }

}
