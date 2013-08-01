package org.codeforamerica.open311.internals.parsing;

import java.util.List;

import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;

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
	public static final String ATTRIBUTES_TAG = "attributes";
	public static final String VARIABLE_TAG = "variable";
	public static final String CODE_TAG = "code";
	public static final String DATATYPE_TAG = "datatype";
	public static final String REQUIRED_TAG = "required";
	public static final String DATATYPE_DESCRIPTION_TAG = "datatype_description";
	public static final String ORDER_TAG = "order";
	public static final String VALUE_TAG = "value";
	public static final String VALUES_TAG = "values";
	public static final String KEY_TAG = "key";
	public static final String NAME_TAG = "name";
	public static final String SERVICE_REQUEST_TAG = "request";
	public static final String SERVICE_REQUESTS_TAG = "service_requests";
	public static final String TOKEN_TAG = "token";
	public static final String SERVICE_REQUEST_ID_TAG = "service_request_id";
	public static final String STATUS_TAG = "status";
	public static final String STATUS_NOTES_TAG = "status_notes";
	public static final String AGENCY_RESPONSIBLE_TAG = "agency_responsible";
	public static final String SERVICE_NOTICE_TAG = "service_notice";
	public static final String REQUESTED_DATETIME_TAG = "requested_datetime";
	public static final String UPDATED_DATETIME_TAG = "updated_datetime";
	public static final String EXPECTED_DATETIME_TAG = "expected_datetime";
	public static final String ADDRESS_TAG = "address";
	public static final String ADDRESS_ID_TAG = "address_id";
	public static final String ZIPCODE_TAG = "zipcode";
	public static final String LATITUDE_TAG = "lat";
	public static final String LONGITUDE_TAG = "long";
	public static final String MEDIA_URL_TAG = "media_url";
	public static final String ACCOUNT_ID_TAG = "account_id";
	public static final String ERROR_TAG = "error";
	public static final String DISCOVERY_TAG = "discovery";
	public static final String CHANGESET_TAG = "changeset";
	public static final String CONTACT_TAG = "contact";
	public static final String KEY_SERVICE_TAG = "key_service";
	public static final String SPECIFICATION_TAG = "specification";
	public static final String URL_TAG = "url";
	public static final String FORMAT_TAG = "format";
	public static final String ENDPOINT_TAG = "endpoint";
	public static final String START_DATE_TAG = "start_date";
	public static final String END_DATE_TAG = "end_date";
	public static final String EMAIL_TAG = "email";
	public static final String DEVICE_ID_TAG = "device_id";
	public static final String FIRST_NAME_TAG = "first_name";
	public static final String LAST_NAME_TAG = "last_name";
	public static final String PHONE_TAG = "phone";

	/**
	 * Parses the response to the GET service list operation.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return A list of {@link Service} objects.
	 */
	public List<Service> parseServiceList(String rawData)
			throws DataParsingException;

	/**
	 * Parses a service definition.
	 * 
	 * @param rawData
	 *            Text data.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 * 
	 * @return A service definition object.
	 */
	public ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException;

	/**
	 * Parses the response to the GET service request id from a token.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return the given token and the service request id.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public ServiceRequestIdResponse parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException;

	/**
	 * Parses a list of service requests.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return A list of ServiceRequest objects.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException;

	/**
	 * Parses the response of a POST Service Request operation.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return an object containing the response information.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public POSTServiceRequestResponse parsePostServiceRequestResponse(
			String rawData) throws DataParsingException;

	/**
	 * Parses an error and returns an object with its information.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return Error information.
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public List<GeoReportV2Error> parseGeoReportV2Errors(String rawData)
			throws DataParsingException;

	/**
	 * Parses a service discovery and returns an object with its information.
	 * 
	 * @param rawData
	 *            Text data.
	 * @return Service discovery information (endpoints and their formats).
	 * @throws DataParsingException
	 *             If there was any problem parsing the data.
	 */
	public ServiceDiscoveryInfo parseServiceDiscovery(String rawData)
			throws DataParsingException;
}
