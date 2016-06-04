package org.okaysoft.gis.module.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Namespace;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.okaysoft.core.utils.Struts2Utils;
import org.okaysoft.gis.core.action.ExtJSGeoAction;
import org.okaysoft.gis.module.service.Icon;
import org.opengis.filter.Filter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Scope("prototype")
@Controller
@Namespace("/geo")
public class WorkspaceAction extends ExtJSGeoAction<WorkspaceInfo> {

	@Override
	public String query() {
		// TODO Auto-generated method stub

		Iterator<WorkspaceInfo> workspaces = catalog.list(WorkspaceInfo.class,
				Filter.INCLUDE, getOffset(), getLimit(), null);

		Map json = new HashMap();
		List<Map> result = new ArrayList<Map>();
		while (workspaces.hasNext()) {
			WorkspaceInfo info = workspaces.next();
			Map<String, String> map = new HashMap<String, String>();

			map.put("name", info.getName());
			map.put("default", isDefaultWorkspace(info));

			result.add(map);
		}

		json.put("root", result);
		json.put("totalProperty",
				catalog.count(WorkspaceInfo.class, Filter.INCLUDE));
		Struts2Utils.renderJson(json);

		return null;
	}

	public String create() {

		try {
			WorkspaceInfo info = new WorkspaceInfoImpl();
			NamespaceInfo ns = catalog.getFactory().createNamespace();
			info.setName("cly");
			ns.setPrefix(info.getName());
			ns.setURI("http://" + info.getName());
			catalog.add(info);
			catalog.add(ns);
			catalog.setDefaultWorkspace(info);
			
			Struts2Utils.renderJson(map);
		} catch (IllegalArgumentException e) {
			map = new HashMap();
			map.put("success", false);
			map.put("message", "创建失败 " + e.getMessage());
			Struts2Utils.renderJson(map);
			return null;
		}
		map = new HashMap();
		map.put("success", true);
		map.put("message", "创建成功");
		return null;
	}
	
	public String store(){
		Iterator<WorkspaceInfo> workspaces = catalog.list(WorkspaceInfo.class,
				Filter.INCLUDE, getOffset(), getLimit(), null);
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		while (workspaces.hasNext()) {
			WorkspaceInfo info = workspaces.next();
			sb.append("{")
			  .append("\"id\":\""+info.getId()+"\",")
			  .append("\"text\":")
			  .append("\""+info.getName()+"\",")
			  .append("\"leaf\":true")
			  .append("},");
		}
		
		sb=sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		Struts2Utils.renderJson(sb.toString());

		return null;
	}

	private String isDefaultWorkspace(WorkspaceInfo ws) {
		WorkspaceInfo defaultWorkspace = catalog.getDefaultWorkspace();

		String IconUrl = "";

		if (defaultWorkspace != null && defaultWorkspace.equals(ws)) {
			IconUrl = Icon.ENABLED_ICON;
		}

		return IconUrl;
	}

}
