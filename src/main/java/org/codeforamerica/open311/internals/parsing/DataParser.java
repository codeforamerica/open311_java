package org.codeforamerica.open311.internals.parsing;

import java.util.List;

import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;

/**
 * Specifies required operations to the parsers.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public interface DataParser {
	public static final String TEXT_FORMAT = "utf-8";

	/**
	 * Parses the response to the GET service list operation.
	 * 
	 * @param rawData
	 *            XML text data.
	 * @return A list of {@link Service} objects.
	 */
	public List<Service> parseServiceList(String rawData);

	/**
	 * 
	 * @param rawData
	 * @return
	 */
	public ServiceDefinition parseServiceDefinition(String rawData);
}
