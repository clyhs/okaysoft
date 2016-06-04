package org.okaysoft.gis.module.action;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.factory.CommonFactoryFinder;
import org.okaysoft.core.utils.Struts2Utils;
import org.okaysoft.gis.core.action.ExtJSGeoAction;
import org.okaysoft.gis.module.model.ParamInfo;
import org.opengis.coverage.grid.Format;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.vfny.geoserver.util.DataStoreUtils;

public class StoreAction extends ExtJSGeoAction<StoreInfo> {
	
	private String type;
	
	private String storeName;
	
	private String wsName;
	private String name;
	private String desc;
	private boolean enabled;
	private String url;
	private int port;
	private String dataSrc;
	private String username;
	private String password;
	private String schema;
	private boolean epk;
	private int maxConn;
	private int minConn;
	private int connTime;
	private int fetchSize;
	private boolean vConn;
	private boolean looseBbox;
	private boolean estimated;
	private boolean pstate;
	private int mops;
	private boolean encodefunctions;
	
	

	@Override
	public String query() {
		// TODO Auto-generated method stub
		
		FilterFactory factory = CommonFactoryFinder.getFilterFactory();
		SortBy sortOrder = null;
		
		sortOrder = factory.sort("name", SortOrder.ASCENDING);
		
		Iterator<StoreInfo> infos = catalog.list(StoreInfo.class, Filter.INCLUDE, start, limit, sortOrder);
		Map json = new HashMap();
		List<Map> result = new ArrayList<Map>();
		while(infos.hasNext()){
			StoreInfo info = infos.next();
			Map<String, String> map = new HashMap<String, String>();

			map.put("data_type", getDataType(info));
			map.put("type", info.getType());
			map.put("name", info.getName());
			map.put("wsname", info.getWorkspace().getName());
			map.put("enabled", info.isEnabled()+"");

			result.add(map);
		}
		json.put("root", result);
		json.put("totalProperty",
				catalog.count(WorkspaceInfo.class, Filter.INCLUDE));
		Struts2Utils.renderJson(json);

		return null;
	}
	
	private String getDataType(StoreInfo info){
		if(info instanceof DataStoreInfo){
			return "vector";
		}else{
			return "raster";
		}
		
	}
	
	public String menu(){
		
		Iterator<DataAccessFactory> availableDataStores = DataStoreUtils.getAvailableDataStoreFactories().iterator();
		Format[] availableFormats = GridFormatFinder.getFormatArray();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		if(type.equals("1")){
			while(availableDataStores.hasNext()){
				
				DataAccessFactory daf = availableDataStores.next();
				
				sb.append("{")
				  .append("\"text\":")
				  .append("\""+daf.getDisplayName()+"\",")
				  .append("\"iconCls\": \"iconData\"")
				  .append("},");
				
			}
		}else if(type.equals("2")){
			for (Format format : availableFormats) {
				sb.append("{")
				  .append("\"text\":")
				  .append("\""+format.getName()+"\",")
				  .append("\"iconCls\": \"iconData\"")
				  .append("},");
			}
		}
		sb=sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		Struts2Utils.renderJson(sb.toString());
		return null;
	}
	
	public String create(){
		
		storeName = "PostGIS";
		WorkspaceInfo wsInfo = catalog.getWorkspaceByName(wsName);
		NamespaceInfo nsInfo = catalog.getNamespaceByPrefix(wsInfo.getName());
		
		log.info(nsInfo.getName()+enabled+" "+vConn);
		
		DataStoreInfo info = catalog.getFactory().createDataStore();
		info.setWorkspace(wsInfo);
		info.setEnabled(enabled);
		info.setType(storeName);
		info.setName(name);
        info.setDescription(desc);
		ResourcePool resourcePool = catalog.getResourcePool();
		Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
        connectionParameters.put("dbtype", "postgis");
        connectionParameters.put("host", url);
        connectionParameters.put("port", port);
        connectionParameters.put("database", dataSrc);
        connectionParameters.put("schema", schema);
        connectionParameters.put("user", username);
        connectionParameters.put("passwd", password);
        connectionParameters.put("namespace", nsInfo.getURI());
        connectionParameters.put("Expose primary keys",epk);
        connectionParameters.put("max connections",maxConn);
        connectionParameters.put("min connections", minConn);
        connectionParameters.put("fetch size", fetchSize);
        connectionParameters.put("Connection timeout", connTime);
        connectionParameters.put("validate connections", vConn);
        connectionParameters.put("Loose bbox", looseBbox);
        connectionParameters.put("Estimated extends", estimated);
        connectionParameters.put("preparedStatements", pstate);
        connectionParameters.put("Max open prepared statements", mops);
        connectionParameters.put("encode functions", encodefunctions);
        
        DataAccessFactory dsFactory;
        try{
            dsFactory = catalog.getResourcePool().getDataStoreFactory(info);
            final Param[] dsParams = dsFactory.getParametersInfo();
           
            for (Param p : dsParams) {
                ParamInfo paramInfo = new ParamInfo(p);
                if(!"".equals(connectionParameters.get(paramInfo.getName())) && connectionParameters.get(paramInfo.getName())!=null){
                	info.getConnectionParameters().put(paramInfo.getName(), connectionParameters.get(paramInfo.getName()));
                    log.info(paramInfo.getName()+"---:"+connectionParameters.get(paramInfo.getName()));
                
                }
               
            }
            catalog.add(info);
        }catch(Exception e){
        	map=new HashMap();
            map.put("success", false);
            map.put("message", e.getMessage());
        }
		
		
		
		map=new HashMap();
        map.put("success", true);
        map.put("message", "修改成功");
        Struts2Utils.renderJson(map);
		
		return null;
	}

	public String create2(){
		storeName = "PostGIS";
		final WorkspaceInfo defaultWs = catalog.getDefaultWorkspace();
		final NamespaceInfo defaultNs = catalog.getDefaultNamespace();
		DataStoreInfo info = catalog.getFactory().createDataStore();
		info.setWorkspace(defaultWs);
		info.setEnabled(true);
		info.setType(storeName);
		
		
		
        
        String dataSrcName = "ssh";
        String description = "dong guang data";
        boolean enabled = true;
        
        final ResourcePool resourcePool = catalog.getResourcePool();
        
        
        DataAccessFactory dsFactory;
        try {
        
            info.setName(dataSrcName);
            info.setEnabled(enabled);
            info.setDescription(description);
            
            Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
            connectionParameters.put("dbtype", "postgis");
            connectionParameters.put("host", "127.0.0.1");
            connectionParameters.put("port", "5432");
            connectionParameters.put("database", "ssh");
            connectionParameters.put("schema", "public");
            connectionParameters.put("user", "postgres");
            connectionParameters.put("passwd", "postgres");
            connectionParameters.put("namespace", "http://cly");
            connectionParameters.put("Expose primary keys",false);
            connectionParameters.put("max connections",10);
            connectionParameters.put("min connections", 1);
            connectionParameters.put("fetch size", 1000);
            connectionParameters.put("Connection timeout", 20);
            connectionParameters.put("validate connections", true);
            connectionParameters.put("Loose bbox", true);
            connectionParameters.put("Estimated extends", true);
            connectionParameters.put("preparedStatements", false);
            connectionParameters.put("Max open prepared statements", 50);
            connectionParameters.put("encode functions", false);
            
            
           // DataStoreInfo dataInfo = (DataStoreInfo)info;
            
            dsFactory = catalog.getResourcePool().getDataStoreFactory(info);
            
            
            
            
            final Param[] dsParams = dsFactory.getParametersInfo();
            
            for (Param p : dsParams) {
                ParamInfo paramInfo = new ParamInfo(p);

                if(!"".equals(connectionParameters.get(paramInfo.getName())) && connectionParameters.get(paramInfo.getName())!=null){
                	info.getConnectionParameters().put(paramInfo.getName(), connectionParameters.get(paramInfo.getName()));
                    //log.info( info.getConnectionParameters().get(paramInfo.getName()).toString() );
                    
                    log.info(paramInfo.getName()+"---:"+connectionParameters.get(paramInfo.getName()));
                
                }
               
            }
            
            DataStoreInfo savedStore = catalog.getFactory().createDataStore();
            clone(info, savedStore);
           
            catalog.add(savedStore);
            
            
            
            return null;
        } catch (IOException e) {
        	log.info("",e);
        }
        
        
        
        
        //store params
        
		
		
		return null;
	}
	
	
	public String select(){
		
		List<StoreInfo> storeList = catalog.getStores(StoreInfo.class);
		
        StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		
		for(StoreInfo s:storeList){
			
			sb.append("{")
			  .append("\"id\":\""+s.getId()+"\",")
			  .append("\"text\":")
			  .append("\""+s.getWorkspace().getName()+":"+s.getName()+"\",")
			  .append("\"leaf\":true")
			  .append("},");
		}
		
		sb=sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		Struts2Utils.renderJson(sb.toString());
		
		
		return null;
	}
	
	protected void clone(final DataStoreInfo source, DataStoreInfo target) {
        target.setDescription(source.getDescription());
        target.setEnabled(source.isEnabled());
        target.setName(source.getName());
        target.setWorkspace(source.getWorkspace());
        target.setType(source.getType());

        target.getConnectionParameters().clear();
        target.getConnectionParameters().putAll(source.getConnectionParameters());
    }
	
	
	public String delete(){
		
		StoreInfo info = catalog.getStore("DataStoreInfoImpl--3bbe04e4:1431d654c53:-8000", StoreInfo.class);
		//info.
		
		catalog.remove(info);
		return null;
	}
	
	protected void applyDataStoreParamsDefaults(StoreInfo info) {
        // grab the factory
        final DataStoreInfo dsInfo = (DataStoreInfo) info;
        DataAccessFactory dsFactory;
        try {
            dsFactory = catalog.getResourcePool().getDataStoreFactory(dsInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Param[] dsParams = dsFactory.getParametersInfo();
        Map connParams = info.getConnectionParameters();
        for (Param p : dsParams) {
            ParamInfo paramInfo = new ParamInfo(p);

            // set default value if not already set to some default
            if (!connParams.containsKey(p.key) || connParams.get(p.key) == null) {
                applyParamDefault(paramInfo, info);
            }
        }
    }
	
	protected void applyParamDefault(ParamInfo paramInfo, StoreInfo info) {
        Serializable defValue = paramInfo.getValue();
        if ("namespace".equals(paramInfo.getName())) {
            defValue = catalog.getDefaultNamespace().getURI();
        } else if (URL.class == paramInfo.getBinding() && null == defValue) {
            defValue = "file:data/example.extension";
        } else {
            defValue = paramInfo.getValue();
        }
        log.info("name:"+paramInfo.getName()+" value:"+defValue);
        info.getConnectionParameters().put(paramInfo.getName(), defValue);
    }
	
	public void setType(String type) {
		this.type = type;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getWsName() {
		return wsName;
	}

	public void setWsName(String wsName) {
		this.wsName = wsName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDataSrc() {
		return dataSrc;
	}

	public void setDataSrc(String dataSrc) {
		this.dataSrc = dataSrc;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public boolean isEpk() {
		return epk;
	}

	public void setEpk(boolean epk) {
		this.epk = epk;
	}

	public int getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public int getMinConn() {
		return minConn;
	}

	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	public int getConnTime() {
		return connTime;
	}

	public void setConnTime(int connTime) {
		this.connTime = connTime;
	}

	public int getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	public boolean isvConn() {
		return vConn;
	}

	public void setvConn(boolean vConn) {
		this.vConn = vConn;
	}

	public boolean isLooseBbox() {
		return looseBbox;
	}

	public void setLooseBbox(boolean looseBbox) {
		looseBbox = looseBbox;
	}

	public boolean isEstimated() {
		return estimated;
	}

	public void setEstimated(boolean estimated) {
		this.estimated = estimated;
	}

	public boolean isPstate() {
		return pstate;
	}

	public void setPstate(boolean pstate) {
		this.pstate = pstate;
	}

	

	public int getMops() {
		return mops;
	}

	public void setMops(int mops) {
		this.mops = mops;
	}

	public boolean isEncodefunctions() {
		return encodefunctions;
	}

	public void setEncodefunctions(boolean encodefunctions) {
		this.encodefunctions = encodefunctions;
	}

	public String getType() {
		return type;
	}

	public String getStoreName() {
		return storeName;
	}

	
	
}
