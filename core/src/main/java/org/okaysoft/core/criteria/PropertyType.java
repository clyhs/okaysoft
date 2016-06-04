package org.okaysoft.core.criteria;

import java.sql.Date;
import java.util.List;

public enum PropertyType {

	String(String.class), Integer(Integer.class), Double(Double.class), Date(Date.class), Boolean(Boolean.class),Long(Long.class),List(List.class);

	private Class<?> clazz;

	PropertyType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getValue() {
		return clazz;
	}
}
