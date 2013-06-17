package org.codeforamerica.open311.internals.parsing;

import java.util.List;

import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
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
	public static final String SERVICE_TAG = "service";
	public static final String SERVICE_CODE_TAG = "service_code";
	public static final String SERVICE_NAME_TAG = "service_name";
	public static final String DESCRIPTION_TAG = "description";
	public static final String METADATA_TAG = "metadata";
	public static final String TYPE_TAG = "type";
	public static final String KEYWORDS_TAG = "keywords";
	public static final String KEYWORDS_SEPARATOR = ",";
	public static final String SERVICE_GROUP_TAG = "group";
	public static final String SERVICE_DEFINITION_TAG = "service_definition";
	public static final String ATTRIBUTE_TAG = "attribute";
	public static final String VARIABLE_TAG = "variable";
	public static final String CODE_TAG = "code";
	public static final String DATATYPE_TAG = "datatype";
	public static final String REQUIRED_TAG = "required";
	public static final String DATATYPE_DESCRIPTION_TAG = "datatype_description";
	public static final String ORDER_TAG = "order";
	public static final String VALUE_TAG = "value";
	public static final String KEY_TAG = "key";
	public static final String NAME_TAG = "name";
	public static final String SERVICE_REQUEST_TAG = "request";
	public static final String TOKEN_TAG = "token";
	public static final String SERVICE_REQUEST_ID_TAG = "service_request_id";

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

	/**
	 * Parses a list of service requests.
	 * 
	 * @param rawData
	 *            XML text data.
	 * @return A list of ServiceRequest objects.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException;
}
