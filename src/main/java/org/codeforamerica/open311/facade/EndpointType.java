package org.codeforamerica.open311.facade;

/**
 * Represents the different types of endpoints.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public enum EndpointType {
	PRODUCTION, TEST, ACCEPTATION, UNKNOWN;

	/**
	 * Builds an instance from a string.
	 * 
	 * @param type
	 *            String representing a type.
	 * @return <code>null</code> if the given type isn't recognized.
	 */
	public static EndpointType getFromString(String type) {
		type = type.toLowerCase();
		if (type.equals("production")) {
			return PRODUCTION;
		}
		if (type.equals("test")) {
			return TEST;
		}
		if (type.equals("acceptation")) {
			return ACCEPTATION;
		}
		return null;

	}
}
