package org.codeforamerica.open311.internals.parsing;

import java.util.List;

import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;

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
	public List<Service> parseServiceList(String rawData)
			throws DataParsingException;

	/**
	 * Parses a list of services (XML/UTF-8 format).
	 * 
	 * @param rawData
	 *            XML text data.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 * 
	 * @returns List of service objects.
	 */
	public ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException;

	/**
	 * Parses the response to the GET service request id from a token.
	 * 
	 * @param rawData
	 *            XML text data.
	 * @return token and service request id.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public List<ServiceRequestIdResponse> parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException;
}
