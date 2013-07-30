package org.codeforamerica.open311.facade.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Single valued attribute useful for the <a
 * href="http://wiki.open311.org/GeoReport_v2#POST_Service_Request">POST Service
 * Request</a> operation.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class SingleValueAttribute implements Attribute {
	/**
	 * Code of the attribute.
	 */
	private String code;
	/**
	 * Value of the attribute.
	 */
	private String value;

	public SingleValueAttribute(String code, String value) {
		super();
		this.code = code;
		this.value = value;
	}

	/**
	 * @return A single pair (attribute[CODE], value) in a
	 *         <code>Map<String, String></code>.
	 */
	@Override
	public Map<String, String> generatePOSTRequestParameter() {
		Map<String, String> result = new HashMap<String, String>();
		result.put("attribute[" + code + "]", value);
		return result;
	}

}
