package org.okaysoft.core.log;

import java.io.Serializable;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class OkayLogger implements Logger, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1756174306390711265L;
	private static Locale configLanguage = null;
    private Logger log = null;
    
    public OkayLogger(Class cls){
    	log = LoggerFactory.getLogger(cls);
    }

 
	public static void setConfigLanguage(Locale configLanguage) {
		OkayLogger.configLanguage = configLanguage;
	}

	private boolean shouldOutput(Locale specifyLanguage){
    	if(configLanguage == null)
    	{
    		return true;
    	}
    	return specifyLanguage.getLanguage().equals(configLanguage.getLanguage());
    }

	public String getName() {
		// TODO Auto-generated method stub
		return log.getName();
	}

	public boolean isTraceEnabled() {
		// TODO Auto-generated method stub
		return log.isTraceEnabled();
	}

	public void trace(String msg) {
		// TODO Auto-generated method stub
		trace(msg,Locale.CHINA);
	}
	
	public void trace(String msg,Locale locale){
		if(shouldOutput(locale)){
			log.trace(msg);
		}
	}

	public void trace(String format, Object obj) {
		// TODO Auto-generated method stub
		log.trace(format, obj);
	}

	public void trace(String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.trace(format, arg1, arg2);
	}

	public void trace(String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.trace(format, argArray);
	}
	
	public void trace(String msg, Throwable t, Locale locale) {
        if(shouldOutput(locale)){
            log.trace(msg, t);
        }
    }

	public void trace(String msg, Throwable t) {
		// TODO Auto-generated method stub
		trace(msg,t,Locale.CHINA);
	}

	public boolean isTraceEnabled(Marker marker) {
		// TODO Auto-generated method stub
		return log.isTraceEnabled(marker);
	}

	public void trace(Marker marker, String msg) {
		// TODO Auto-generated method stub
		trace(marker,msg,Locale.CHINA);
	}

	public void trace(Marker marker, String msg, Locale locale) {
        if(shouldOutput(locale)){
            log.trace(marker, msg);
        }
    }
	
	public void trace(Marker marker, String format, Object arg) {
		// TODO Auto-generated method stub 
		log.trace(marker, format, arg);
	}

	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.trace(marker, format, arg1, arg2);
	}

	public void trace(Marker marker, String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.trace(marker, format, argArray);
	}

	public void trace(Marker marker, String msg, Throwable t) {
		// TODO Auto-generated method stub
		trace(marker,msg,t,Locale.CHINA);
	}
	
	public void trace(Marker marker, String msg, Throwable t, Locale locale) {
        if(shouldOutput(locale)){
            log.trace(marker, msg, t);
        }
    }

	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return log.isDebugEnabled();
	}

	public void debug(String msg) {
		// TODO Auto-generated method stub
		debug(msg,Locale.CHINA);
	}
	
	public void debug(String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.debug(msg);
		}

	}

	public void debug(String format, Object arg) {
		// TODO Auto-generated method stub
		log.debug(format, arg);
	}

	public void debug(String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.debug(format, arg1, arg2);
	}

	public void debug(String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.debug(format, argArray);
	}

	public void debug(String msg, Throwable t) {
		// TODO Auto-generated method stub
		debug(msg,t,Locale.CHINA);
	}
	
	public void debug(String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.debug(msg, t);
		}
	}

	public boolean isDebugEnabled(Marker marker) {
		// TODO Auto-generated method stub
		return log.isDebugEnabled(marker);
	}

	public void debug(Marker marker, String msg) {
		// TODO Auto-generated method stub
		debug(marker,msg,Locale.CHINA);
	}
	
	public void debug(Marker marker, String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.debug(marker, msg);
		}
	}

	public void debug(Marker marker, String format, Object arg) {
		// TODO Auto-generated method stub
		log.debug(marker, format, arg);
	}

	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.debug(marker, format, arg1, arg2);
	}

	public void debug(Marker marker, String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.debug(marker, format, argArray);
	}

	public void debug(Marker marker, String msg, Throwable t) {
		// TODO Auto-generated method stub
		debug(marker,msg,t,Locale.CHINA);
	}
	
	public void debug(Marker marker, String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.debug(marker, msg, t);
		}
	}

	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return log.isInfoEnabled();
	}

	public void info(String msg) {
		// TODO Auto-generated method stub
		info(msg,Locale.CHINA);
	}
	
	public void info(String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.info(msg);
		}
	}

	public void info(String format, Object arg) {
		// TODO Auto-generated method stub
		log.info(format, arg);
	}

	public void info(String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.info(format, arg1, arg2);
	}

	public void info(String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.info(format, argArray);
	}

	public void info(String msg, Throwable t) {
		// TODO Auto-generated method stub
		info(msg,t,Locale.CHINA);
	}
	
	public void info(String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.info(msg, t);
		}
	}

	public boolean isInfoEnabled(Marker marker) {
		// TODO Auto-generated method stub
		return log.isDebugEnabled(marker);
	}

	public void info(Marker marker, String msg) {
		// TODO Auto-generated method stub
		info(marker,msg,Locale.CHINA);
	}
	
	public void info(Marker marker, String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.info(marker, msg);
		}
	}

	public void info(Marker marker, String format, Object arg) {
		// TODO Auto-generated method stub
		log.info(marker, format, arg);
	}

	public void info(Marker marker, String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.info(marker, format, arg1, arg2);
	}

	public void info(Marker marker, String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.info(marker, format, argArray);
	}

	public void info(Marker marker, String msg, Throwable t) {
		// TODO Auto-generated method stub
		info(marker,msg,t,Locale.CHINA);
	}
	
	public void info(Marker marker, String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.info(marker, msg, t);
		}
	}

	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return log.isWarnEnabled();
	}

	public void warn(String msg) {
		// TODO Auto-generated method stub
		warn(msg,Locale.CHINA);
	}
	
	public void warn(String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.warn(msg);
		}
	}

	public void warn(String format, Object arg) {
		// TODO Auto-generated method stub
		log.warn(format, arg);
	}

	public void warn(String format, Object[] argArray) {
		// TODO Auto-generated method stub

		log.warn(format, argArray);
	}

	public void warn(String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.warn(format, arg1, arg2);
	}

	public void warn(String msg, Throwable t) {
		// TODO Auto-generated method stub
		warn(msg,t,Locale.CHINA);
	}
	
	public void warn(String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.warn(msg, t);
		}
	}

	public boolean isWarnEnabled(Marker marker) {
		// TODO Auto-generated method stub
		return log.isWarnEnabled(marker);
	}

	public void warn(Marker marker, String msg) {
		// TODO Auto-generated method stub
		warn(marker,msg,Locale.CHINA);
	}
	
	public void warn(Marker marker, String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.warn(marker, msg);
		}
	}

	public void warn(Marker marker, String format, Object arg) {
		// TODO Auto-generated method stub
		log.warn(marker, format, arg);
	}

	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.warn(marker, format, arg1, arg2);
	}

	public void warn(Marker marker, String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.warn(marker, format, argArray);
	}

	public void warn(Marker marker, String msg, Throwable t) {
		// TODO Auto-generated method stub
		warn(marker,msg,t,Locale.CHINA);
	}
	
	public void warn(Marker marker, String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.warn(marker, msg,t);
		}
	}

	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return log.isErrorEnabled();
	}

	public void error(String msg) {
		// TODO Auto-generated method stub
		error(msg,Locale.CHINA);
	}

	public void error(String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.error(msg);
		}
	}
	
	public void error(String format, Object arg) {
		// TODO Auto-generated method stub
		log.error(format, arg);
	}

	public void error(String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.error(format, arg1, arg2);
	}

	public void error(String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.error( format, argArray);
	}

	public void error(String msg, Throwable t) {
		// TODO Auto-generated method stub
		error(msg,t,Locale.CHINA);
	}
	
	public void error(String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.error(msg,t);
		}
	}

	public boolean isErrorEnabled(Marker marker) {
		// TODO Auto-generated method stub
		return log.isErrorEnabled(marker);
	}

	public void error(Marker marker, String msg) {
		// TODO Auto-generated method stub
		error(marker,msg,Locale.CHINA);
	}
	
	public void error(Marker marker, String msg,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.error(marker,msg);
		}
	}

	public void error(Marker marker, String format, Object arg) {
		// TODO Auto-generated method stub
		log.error(marker, format, arg);
	}

	public void error(Marker marker, String format, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		log.error(marker, format, arg1, arg2);
	}

	public void error(Marker marker, String format, Object[] argArray) {
		// TODO Auto-generated method stub
		log.error(marker, format, argArray);
	}

	public void error(Marker marker, String msg, Throwable t) {
		// TODO Auto-generated method stub
		error(marker,msg,t,Locale.CHINA);
	}
	
	public void error(Marker marker, String msg, Throwable t,Locale locale) {
		// TODO Auto-generated method stub
		if(shouldOutput(locale)){
			log.error(marker,msg,t);
		}
	}

}
