package org.codeforamerica.open311.facade.data;

import java.io.Serializable;

/**
 * Represents a GeoReport service.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class Service implements Serializable {
	private static final long serialVersionUID = 1124990264010718071L;
	private String serviceCode;
	private String serviceName;
	private String description;

	public Service(String serviceCode, String serviceName, String description,
			boolean metadata, Type type, String[] keywords, String group) {
		super();
		this.serviceCode = serviceCode;
		this.serviceName = serviceName;
		this.description = description;
		this.metadata = metadata;
		this.type = type;
		this.keywords = keywords;
		this.group = group;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasMetadata() {
		return metadata;
	}

	public Type getType() {
		return type;
	}

	public String[] getKeywords() {
		return keywords;
	}

	public String getGroup() {
		return group;
	}

	private boolean metadata;
	private Type type;
	private String[] keywords;
	private String group;

	/**
	 * Different types of Service.
	 * 
	 */
	public static enum Type {
		REALTIME, BATCH, BLACKBOX;
		/**
		 * Returns an instance of this class from a given string.
		 * 
		 * @param type
		 * @return <code>null</code> if the string is not one of the contained
		 *         types.
		 */
		public static Type getFromString(String type) {
			type = type.toLowerCase();
			if (type.equals("realtime")) {
				return REALTIME;
			}
			if (type.equals("batch")) {
				return BATCH;
			}
			if (type.equals("blackbox")) {
				return BLACKBOX;
			}
			return null;
		}
	}

	public String toString() {
		return "[" + this.serviceCode + "] " + this.serviceName + " ("
				+ this.description + ")";
	}

}
