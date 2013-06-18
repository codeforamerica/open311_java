package org.codeforamerica.open311.facade.exceptions;

/**
 * Wraps a GeoReport v2 API error. <a
 * href="http://wiki.open311.org/GeoReport_v2#Errors">More info</>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class GeoReportV2Error {
	private String code;
	private String description;
	private static final String NAME = "GeoReportError";

	public GeoReportV2Error(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return NAME + " #" + code + ": " + description;
	}

}
