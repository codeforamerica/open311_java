package org.codeforamerica.open311.facade;

/**
 * Represent the different formats allowed by the library.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public enum Format {
	XML("xml", "text/xml"), JSON("json", "application/json");

	private String description;
	private String httpContentType;

	Format(String description, String httpContentType) {
		this.description = description;
		this.httpContentType = httpContentType;
	}

	public String toString() {
		return description;
	}

	public String getHttpContentType() {
		return httpContentType;
	}

	/**
	 * Builds an instance from the content type.
	 * 
	 * @param contentTypeString
	 *            A string representing a content type.
	 * @return <code>null</code> if the given content type is not allowed.
	 */
	public static Format getFromContentTypeString(String contentTypeString) {
		contentTypeString = contentTypeString.toLowerCase();
		if (contentTypeString.equals(JSON.httpContentType)) {
			return Format.JSON;
		}
		if (contentTypeString.equals(XML.httpContentType)) {
			return Format.XML;
		}
		return null;
	}
}
