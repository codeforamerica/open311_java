package org.codeforamerica.open311.facade.exceptions;

import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper;

/**
 * Thrown if it was any problem performing an operation through the
 * {@link APIWrapper}.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class APIWrapperException extends Exception {

	private static final long serialVersionUID = 1L;
	private Error error;
	private List<GeoReportV2Error> geoReportErrors;

	public APIWrapperException(String message, Error error,
			List<GeoReportV2Error> geoReportErrors) {
		super(message);
		this.error = error;
		this.geoReportErrors = geoReportErrors;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Error getError() {
		return error;
	}

	public List<GeoReportV2Error> getGeoReportErrors() {
		return geoReportErrors;
	}

	/**
	 * Different types of errors.
	 */
	public static enum Error {
		DATA_PARSING("Problem while parsing text data."), NETWORK_MANAGER(
				"Network problem."), URL_BUILDER(
				"Problem while building a valid URL."), GEO_REPORT_V2(
				"GeoReport v2 problem.");
		private String description;

		private Error(String description) {
			this.description = description;
		}

		public String toString() {
			return this.description;
		}
	}

}
