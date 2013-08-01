package org.codeforamerica.open311.facade.exceptions;

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
	private GeoReportV2Error geoReportError;

	public APIWrapperException(String message, Error error,
			GeoReportV2Error geoReportError) {
		super(message);
		this.error = error;
		this.geoReportError = geoReportError;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Error getError() {
		return error;
	}

	public GeoReportV2Error getGeoReportError() {
		return geoReportError;
	}

	public String toString() {
		String result = this.getMessage() + " (" + this.error;
		if (geoReportError != null) {
			result += " -- " + geoReportError.toString();
		}
		result += ")";
		return result;
	}

	/**
	 * Different types of errors.
	 */
	public static enum Error {
		DATA_PARSING("Problem while parsing text data."), NETWORK_MANAGER(
				"Network problem."), URL_BUILDER(
				"Problem while building a valid URL."), GEO_REPORT_V2(
				"GeoReport v2 problem."), NOT_SUITABLE_ENDPOINT_FOUND(
				"Problem finding a suitable endpoint."), NOT_CREATED_BY_A_WRAPPER(
				"The given object wasn't created by any wrapper");
		private String description;

		private Error(String description) {
			this.description = description;
		}

		public String toString() {
			return this.description;
		}
	}

}
