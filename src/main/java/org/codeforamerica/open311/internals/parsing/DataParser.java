package org.codeforamerica.open311.internals.parsing;

import java.util.List;

import org.codeforamerica.open311.facade.data.Service;

/**
 * Specifies required operations to the parsers.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public interface DataParser {
	public static final String TEXT_FORMAT = "utf-8";

	public List<Service> parseServiceList(String rawData);
}
