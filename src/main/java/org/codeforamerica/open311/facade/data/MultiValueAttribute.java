package org.codeforamerica.open311.facade.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Multi valued attribute useful for the <a
 * href="http://wiki.open311.org/GeoReport_v2#POST_Service_Request">POST Service
 * Request</a> operation.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class MultiValueAttribute implements Attribute {
	/**
	 * Code of the attribute.
	 */
	private String code;
	/**
	 * Values of the attribute.
	 */
	private String[] values;

	public MultiValueAttribute(String code, String... values) {
		super();
		this.code = code;
		this.values = values;
	}

	/**
	 * @return A list of pairs (attribute[CODE][], value1), (attribute[CODE][],
	 *         value2), ... in a <code>Map<String, String></code>.
	 */
	@Override
	public Map<String, String> generatePOSTRequestParameter() {
		Map<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < values.length; i++) {
			result.put("attribute[" + code + "][" + i + "]", values[i]);
		}
		return result;
	}
}
