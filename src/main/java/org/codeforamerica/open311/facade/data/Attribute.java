package org.codeforamerica.open311.facade.data;

import java.util.Map;

/**
 * Specifies the required operations of an attribute useful for the <a
 * href="http://wiki.open311.org/GeoReport_v2#POST_Service_Request">POST Service
 * Request</a> operation.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public interface Attribute {
	/**
	 * Generates a list of pairs (String, String) ready to be passed as
	 * paremeters to a POST request.
	 * 
	 * @return A pair (attribute[CODE],value) or a list of pairs
	 *         (attribute[CODE][],value1), (attribute[CODE][], value2)...
	 */
	public Map<String, String> generatePOSTRequestParameter();
}
