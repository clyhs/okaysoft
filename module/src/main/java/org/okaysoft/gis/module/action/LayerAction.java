package org.okaysoft.gis.module.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Namespace;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.LayerInfo.Type;
import org.geoserver.catalog.ProjectionPolicy;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WMSLayerInfo;
import org.geoserver.catalog.WMSStoreInfo;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.okaysoft.core.action.DefaultAction;
import org.okaysoft.core.utils.Struts2Utils;
import org.okaysoft.gis.core.action.ExtJSGeoAction;
import org.okaysoft.gis.module.service.Icon;
import org.okaysoft.gis.module.service.LayerService;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.postgresql.util.PSQLException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

@Scope("prototype")
@Controller
@Namespace("/geo")
public class LayerAction extends ExtJSGeoAction<LayerInfo> {

	@Resource(name = "layerService")
	private LayerService layerService;

	private String storeId;

	private List<org.okaysoft.gis.module.model.Resource> lists = new ArrayList<org.okaysoft.gis.module.model.Resource>();

	public String query() {

		log.info("query");

		Iterator<LayerInfo> layerInfos = catalog.list(LayerInfo.class,
				Filter.INCLUDE, getOffset(), getLimit(), null);

		Map json = new HashMap();
		List<Map> result = new ArrayList<Map>();
		while (layerInfos.hasNext()) {
			LayerInfo layerInfo = layerInfos.next();

			Map<String, String> map = new HashMap<String, String>();
			// map.put("id",layerInfo.getId());
			map.put("name", layerInfo.getName());
			map.put("type", getIcon(layerInfo));
			map.put("title", layerInfo.getResource().getTitle());
			map.put("id", layerInfo.getResource().getId());
			map.put("prefix", layerInfo.prefixedName());
			map.put("srs", layerInfo.getResource().getSRS());
			map.put("logotype", layerInfo.getResource().getStore().getType());

			result.add(map);
		}
		json.put("root", result);
		json.put("totalProperty",
				catalog.count(LayerInfo.class, Filter.INCLUDE));
		Struts2Utils.renderJson(json);

		return null;
	}

	public String create() {

		StoreInfo store = catalog.getStore(
				"DataStoreInfoImpl-54f05f44:143859e3da2:-7ffc",
				StoreInfo.class);

		List<Name> names = null;

		log.info("create start");

		if (store instanceof DataStoreInfo) {
			log.info("create ing");
			DataStoreInfo dstore = (DataStoreInfo) store;
			try {
				names = dstore.getDataStore(null).getNames();

				org.okaysoft.gis.module.model.Resource resource = new org.okaysoft.gis.module.model.Resource(
						names.get(1));

				CatalogBuilder cb = new CatalogBuilder(catalog);

				LayerInfo layerInfo = buildLayerInfo(resource, store);
				ResourceInfo resourceInfo = layerInfo.getResource();
				layerInfo.setName(names.get(1).getLocalPart());
				layerInfo.setEnabled(true);
				layerInfo.setAdvertised(true);
				layerInfo.setTitle(names.get(1).getLocalPart());
				layerInfo.setQueryable(true);

				resourceInfo.setSRS("EPSG:4326");
				resourceInfo.setStore(store);
				resourceInfo.setEnabled(true);
				CoordinateReferenceSystem declaredCRS;

				declaredCRS = CRS.decode("EPSG:4326");

				// resourceInfo.setNativeCRS(declaredCRS);

				ReferencedEnvelope nativeBounds = cb
						.getNativeBounds(resourceInfo);
				resourceInfo.setNativeBoundingBox(nativeBounds);
				ReferencedEnvelope latlonBounds = cb.getLatLonBounds(
						nativeBounds, declaredCRS);
				resourceInfo.setLatLonBoundingBox(latlonBounds);

				resourceInfo
						.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);

				layerInfo.getMetadata().put("GWC.metaTilingX", 4);
				layerInfo.getMetadata().put("GWC.autoCacheStyles", true);

				layerInfo.getMetadata().put("GWC.metaTilingY", 4);

				layerInfo.getMetadata().put("GWC.gutter", 0);

				layerInfo.getMetadata().put("GWC.enabled", true);
				layerInfo.getMetadata().put("GWC.gridSets",
						"EPSG:4326,EPSG:900913");
				layerInfo.getMetadata().put("GWC.cacheFormats",
						"image/png,image/jpeg");

				layerInfo.setResource(resourceInfo);
				catalog.add(resourceInfo);
				catalog.add(layerInfo);

			} catch (Exception e) {
				// TODO Auto-generated catch block

			}

		}

		return null;
	}

	public String find() {

		if (!" ".equals(storeId) && storeId!=null) {
			StoreInfo info = catalog.getStore(storeId, StoreInfo.class);
			List<Name> names = null;
			Map json = new HashMap();
			List<Map> result = new ArrayList<Map>();
			Map<String, org.okaysoft.gis.module.model.Resource> resources = new HashMap<String, org.okaysoft.gis.module.model.Resource>();
			if (info instanceof DataStoreInfo) {
				DataStoreInfo dstore = (DataStoreInfo) info;
				try {
					names = dstore.getDataStore(null).getNames();
					int toIndex = start + limit;
					if (toIndex > names.size()) {
						toIndex = names.size();
					}
					Iterator<Name> iters = names.subList(start, toIndex)
							.iterator();
					while (iters.hasNext()) {

						Name n = iters.next();
						Map<String, String> map = new HashMap<String, String>();
						map.put("localPart", n.getLocalPart());
						FeatureTypeInfo fti = catalog.getFeatureTypeByDataStore(dstore, n.getLocalPart());
						if(fti==null){
							map.put("published", "false");
						}else{
							map.put("published", "true");
						}
						result.add(map);
					}
					json.put("root", result);
					json.put("totalProperty", dstore.getDataStore(null)
							.getNames().size());
					Struts2Utils.renderJson(json);

				} catch (IOException e) {

				}
			}
		}else{
			log.warn("没有选择数据源");
		}
		return null;
	}

	
	LayerInfo buildLayerInfo(org.okaysoft.gis.module.model.Resource resource,
			StoreInfo store) {

		try {
			CatalogBuilder builder = new CatalogBuilder(catalog);
			builder.setStore(store);
			if (store instanceof CoverageStoreInfo) {
				CoverageInfo ci = builder.buildCoverage(resource.getName()
						.getLocalPart());
				return builder.buildLayer(ci);
			} else if (store instanceof DataStoreInfo) {
				FeatureTypeInfo fti = builder.buildFeatureType(resource
						.getName());
				fti.setSRS("EPSG:4326");
				return builder.buildLayer(fti);
			} else if (store instanceof WMSStoreInfo) {
				WMSLayerInfo wli = builder.buildWMSLayer(resource
						.getLocalName());
				return builder.buildLayer(wli);
			}
		} catch (Exception e) {

		}
		return null;
	}

	private String getIcon(LayerInfo info) {
		if (info.getType() == Type.RASTER) {
			return Icon.RASTER_ICON;
		} else if (info.getType() == Type.VECTOR) {
			try {
				FeatureTypeInfo fti = (FeatureTypeInfo) info.getResource();
				GeometryDescriptor gd = fti.getFeatureType()
						.getGeometryDescriptor();
				return getVetoryIcon(gd);
			} catch (Exception e) {
				return Icon.GEOMETRY_ICON;
			}
		} else if (info.getType() == Type.WMS) {
			return Icon.MAP_ICON;
		} else {
			return Icon.UNKNOWN_ICON;
		}
	}

	private String getVetoryIcon(GeometryDescriptor gd) {
		if (gd == null) {
			return Icon.GEOMETRY_ICON;
		}
		Class geom = gd.getType().getBinding();
		return getVectorIcon(geom);
	}

	private String getVectorIcon(Class geom) {
		if (Point.class.isAssignableFrom(geom)
				|| MultiPoint.class.isAssignableFrom(geom)) {
			return Icon.POINT_ICON;
		} else if (LineString.class.isAssignableFrom(geom)
				|| MultiLineString.class.isAssignableFrom(geom)) {
			return Icon.LINE_ICON;
		} else if (Polygon.class.isAssignableFrom(geom)
				|| MultiPolygon.class.isAssignableFrom(geom)) {
			return Icon.POLYGON_ICON;
		} else {
			return Icon.GEOMETRY_ICON;
		}
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

}
