package org.okaysoft.core.spring;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.okaysoft.core.log.OkayLogger;
import org.okaysoft.core.monitor.model.RuningTime;
import org.okaysoft.core.monitor.service.MemoryMonitorThread;
import org.okaysoft.core.service.LogService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;


public class SystemListener {
	
	private static final OkayLogger log = new OkayLogger(SystemListener.class);
	
    private static boolean running=false;
    
    private static String basePath;
    private static String contextPath;
    private static RuningTime runingTime=null;
    private static final  boolean memoryMonitor;
    private static final  boolean runingMonitor;
    private static MemoryMonitorThread memoryMonitorThread;
    private static Set searchLocations;
    private static File baseDirectory;
    
    public SystemListener(){
    	searchLocations = new TreeSet();
    	setSearchLocations(Collections.EMPTY_SET);
    }
    
    static{
    	memoryMonitor = true;
    	runingMonitor = true;
    	if(memoryMonitor){
            log.info("启用内存监视日志");
            log.info("Enable memory monitor log", Locale.ENGLISH);
        }else{
            log.info("禁用内存监视日志");
            log.info("Disable memory monitor log", Locale.ENGLISH);
        }
        if(runingMonitor){
            log.info("启用系统运行日志");
            log.info("Enable system log", Locale.ENGLISH);
        }else{
            log.info("禁用系统运行日志");
            log.info("Disable system log", Locale.ENGLISH);
        }
    }
    public static boolean isRunning() {
        return running;
    }
    
    
    public static void contextInitialized(ServletContextEvent sce) {
    	contextPath   = sce.getServletContext().getContextPath();
    	ServletContext sc=sce.getServletContext();
        basePath=sc.getRealPath("/");
        if(!basePath.endsWith(File.separator)){
            basePath=basePath+File.separator;
        }
        
        if(runingMonitor){
            log.info("记录服务器启动日志");
            log.info("Recording the server boot logging", Locale.ENGLISH);
            runingTime=new RuningTime();
            try {
                runingTime.setServerIP(InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException e) {
                log.error("记录服务器启动日志出错", e);
                log.error("Failed to record the server boot logging", e, Locale.ENGLISH);
            }
            runingTime.setAppName(contextPath);
            runingTime.setOsName(System.getProperty("os.name"));
            runingTime.setOsVersion(System.getProperty("os.version"));
            runingTime.setOsArch(System.getProperty("os.arch"));
            runingTime.setJvmName(System.getProperty("java.vm.name"));
            runingTime.setJvmVersion(System.getProperty("java.vm.version"));
            runingTime.setJvmVendor(System.getProperty("java.vm.vendor"));
            runingTime.setStartupTime(new Date());
        }
        if(memoryMonitor){
            log.info("启动内存监视线程");
            log.info("Enable memory monitor thread", Locale.ENGLISH);
            int circle=2;
            memoryMonitorThread=new MemoryMonitorThread(circle);
            memoryMonitorThread.start();
        }
        running=true;
    }
    
    public static void contextDestroyed(ServletContextEvent sce) {
    	UserLoginListener.forceAllUserOffline();
    	if(runingMonitor){
            log.info("记录服务器关闭日志");
            log.info("Recording the server shutdown logging", Locale.ENGLISH);             
            runingTime.setShutdownTime(new Date());
            runingTime.setRuningTime(runingTime.getShutdownTime().getTime()-runingTime.getStartupTime().getTime());
            LogService.addLog(runingTime);
        }
        if(memoryMonitor){
            log.info("停止内存监视线程");
            log.info("Stop memory monitor thread", Locale.ENGLISH);
            memoryMonitorThread.running=false;
            memoryMonitorThread.interrupt();
        }
        
        if(LogService.getLogQueue()!=null){
        	LogService.getLogQueue().saveLog();
        }
    }
    
    public static String getContextPath() {
        return contextPath;
    }

	public static void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		
		log.info("--------------开始设置数据目录");
		
		if (baseDirectory == null) {
            //lookup the data directory
            if (applicationContext instanceof WebApplicationContext) {
                String data = lookupGeoServerDataDirectory(
                        ((WebApplicationContext)applicationContext).getServletContext());
                if (data != null) {
                    setBaseDirectory(new File(data)); 
                }
            }
        }
        
        if (baseDirectory != null) {
            addSearchLocation(new File(baseDirectory, "data"));
        }

        if (applicationContext instanceof WebApplicationContext) {
            ServletContext servletContext = ((WebApplicationContext)applicationContext).getServletContext();
            if (servletContext != null) {
                String path = servletContext.getRealPath("WEB-INF");
                if (path != null) {
                    addSearchLocation(new File(path));
                }
                path = servletContext.getRealPath("/");
                if (path != null) {
                    addSearchLocation(new File(path));
                }
            }
        }
        log.info("-----------------结束设置数据目录");
	}
    
    public static void setSearchLocations(Set searchLocs) {
        searchLocations = new HashSet(searchLocs);
        if (baseDirectory != null) {
            searchLocations.add(baseDirectory);
        }
    }
    public static void addSearchLocation(File searchLocation) {
        searchLocations.add(searchLocation);
    }
	
	public static File getBaseDirectory() {
        return baseDirectory;
    }

    public static void setBaseDirectory(File baseDir) {
        baseDirectory = baseDir;
        searchLocations.add(baseDir);
    }
    
    public static String lookupGeoServerDataDirectory(ServletContext servContext) {
        
        final String[] typeStrs = { "Java environment variable ",
                "Servlet context parameter ", "System environment variable " };

        final String[] varStrs = { "GEOSERVER_DATA_DIR", "GEOSERVER_DATA_ROOT" };

        String dataDirStr = null;
        String msgPrefix = null;
        int iVar = 0;
        // Loop over variable names
        for (int i = 0; i < varStrs.length && dataDirStr == null; i++) {
            
            // Loop over variable access methods
            for (int j = 0; j < typeStrs.length && dataDirStr == null; j++) {
                String value = null;
                String varStr = new String(varStrs[i]);
                String typeStr = typeStrs[j];

                // Lookup section
                switch (j) {
                case 0:
                    value = System.getProperty(varStr);
                    break;
                case 1:
                    value = servContext.getInitParameter(varStr);
                    break;
                case 2:
                    value = System.getenv(varStr);
                    break;
                }

                if (value == null || value.equalsIgnoreCase("")) {
                    log.info(" 没有设置  " + typeStr + varStr + " ");
                    continue;
                }

                
                // Verify section
                File fh = new File(value);

                // Being a bit pessimistic here
                msgPrefix = "设置了 " + typeStr + varStr + " 为 :" + value;

                if (!fh.exists()) {
                	log.info(msgPrefix + " , 路径不存在");
                    continue;
                }
                if (!fh.isDirectory()) {
                	log.info(msgPrefix + " , 不是目录");
                    continue;
                }
                if (!fh.canWrite()) {
                	log.info(msgPrefix + " , 不可写");
                    continue;
                }

                // Sweet, we can work with this
                dataDirStr = value;
                iVar = i;
            }
        }
        
        // fall back to embedded data dir
        if(dataDirStr == null){
            dataDirStr = servContext.getRealPath("/data");
            log.info("得新设置数据目录："+dataDirStr);
        }
        
        
        return dataDirStr;
    }

}
