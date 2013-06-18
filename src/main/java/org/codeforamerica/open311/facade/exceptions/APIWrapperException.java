package org.codeforamerica.open311.facade.exceptions;

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
	private GeoReportError geoReportError;

	public APIWrapperException(Error error, GeoReportError geoReportError) {
		super();
		this.error = error;
		this.geoReportError = geoReportError;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Error getError() {
		return error;
	}

	public GeoReportError getGeoReportError() {
		return geoReportError;
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
