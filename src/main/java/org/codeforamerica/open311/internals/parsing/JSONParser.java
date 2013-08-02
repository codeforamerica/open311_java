package org.codeforamerica.open311.internals.parsing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.AttributeInfo;
import org.codeforamerica.open311.facade.data.AttributeInfo.Datatype;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Implementation of a {@link DataParser} which takes JSON data as input.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class JSONParser extends AbstractParser {
	private static final String NULL_STRING_JSON = "null";
	private DateParser dateParser = new DateParser();

	@Override
	public List<Service> parseServiceList(String rawData)
			throws DataParsingException {
		List<Service> result = new LinkedList<Service>();
		try {
			JSONArray serviceList = new JSONArray(rawData);
			for (int i = 0; i < serviceList.length(); i++) {
				JSONObject service = serviceList.getJSONObject(i);
				result.add(getService(service));
			}
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
		return result;
	}

	/**
	 * Builds a {@link Service} object from a {@link JSONObject}.
	 * 
	 * @param service
	 *            JSONObject which represents a service.
	 * @return An object with its state given by the JSONObject
	 * @throws JSONException
	 *             If the given service isn't correct.
	 * @throws DataParsingException
	 *             If the received parameters are not valid.
	 */
	private Service getService(JSONObject service) throws JSONException,
			DataParsingException {
		String code = getString(service, SERVICE_CODE_TAG);
		String name = getString(service, SERVICE_NAME_TAG);
		String description = getString(service, DESCRIPTION_TAG);
		Boolean metadata = getBoolean(service, METADATA_TAG);
		String group = getString(service, SERVICE_GROUP_TAG);
		String[] keywords = getKeywords(getString(service, KEYWORDS_TAG));
		Service.Type type = Service.Type.getFromString(getString(service,
				TYPE_TAG));
		checkParameters(code);
		return new Service(code, name, description, metadata, type, keywords,
				group);
	}

	@Override
	public ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException {
		try {
			JSONObject serviceDefinition = new JSONObject(rawData);
			String serviceCode = getString(serviceDefinition, SERVICE_CODE_TAG);
			JSONArray attributesArray = serviceDefinition
					.getJSONArray(ATTRIBUTES_TAG);
			checkParameters(serviceCode);
			return new ServiceDefinition(serviceCode,
					parseAttributeList(attributesArray));
		} catch (JSONException e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	/**
	 * Parses the list of attributes attached to a service definition.
	 * 
	 * @param attributesArray
	 *            The JSONArray object.
	 * @return List of attributes.
	 * @throws JSONException
	 *             If the given object is not correct.
	 * @throws DataParsingException
	 *             If the code parameter is missing.
	 */
	private List<AttributeInfo> parseAttributeList(JSONArray attributesArray)
			throws JSONException, DataParsingException {
		List<AttributeInfo> attributes = new LinkedList<AttributeInfo>();
		for (int i = 0; i < attributesArray.length(); i++) {
			JSONObject attribute = attributesArray.getJSONObject(i);
			Boolean variable = getBoolean(attribute, VARIABLE_TAG);
			String code = getString(attribute, CODE_TAG);
			Datatype datatype = Datatype.getFromString(getString(attribute,
					DATATYPE_TAG));
			Boolean required = getBoolean(attribute, REQUIRED_TAG);
			String datatypeDescription = getString(attribute,
					DATATYPE_DESCRIPTION_TAG);
			Integer order = getInteger(attribute, ORDER_TAG);
			String description = getString(attribute, DESCRIPTION_TAG);
			Map<String, String> values = null;
			if (attribute.has(VALUES_TAG)) {
				JSONArray valuesArray = attribute.getJSONArray(VALUES_TAG);
				values = new HashMap<String, String>();
				for (int j = 0; j < valuesArray.length(); j++) {
					JSONObject valueObject = valuesArray.getJSONObject(j);
					String key = getString(valueObject, KEY_TAG);
					String name = getString(valueObject, NAME_TAG);
					values.put(key, name);
				}
			}
			checkParameters(code);
			attributes.add(new AttributeInfo(variable, code, datatype,
					required, datatypeDescription, order, description, values));
		}
		return attributes;
	}

	@Override
	public ServiceRequestIdResponse parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException {
		try {
			JSONArray responsesArray = new JSONArray(rawData);
			if (responsesArray.length() == 0) {
				return null;
			}
			JSONObject response = responsesArray.getJSONObject(0);
			String token = getString(response, TOKEN_TAG);
			String serviceRequestId = getString(response,
					SERVICE_REQUEST_ID_TAG);
			checkParameters(token, serviceRequestId);
			return new ServiceRequestIdResponse(serviceRequestId, token);
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException {
		List<ServiceRequest> result = new LinkedList<ServiceRequest>();
		try {
			JSONArray serviceRequestsArray;
			if (rawData.contains("{\"service_requests\"")) {
				serviceRequestsArray = new JSONObject(rawData)
						.getJSONArray(SERVICE_REQUESTS_TAG);
			} else {
				serviceRequestsArray = new JSONArray(rawData);
			}
			for (int i = 0; i < serviceRequestsArray.length(); i++) {
				JSONObject serviceRequest = serviceRequestsArray
						.getJSONObject(i);
				result.add(parseServiceRequest(serviceRequest));
			}
			return result;
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	/**
	 * Builds a service request object from a JSONObject.
	 * 
	 * @param serviceRequest
	 *            JSONObject representing the service definition data.
	 * @return A service request object.
	 * @throws MalformedURLException
	 *             If the media URL isn't correct.
	 * @throws DataParsingException
	 *             If the received parameters are not valid.
	 * @throws JSONException
	 *             If there was any problem parsing any parameter.
	 */
	private ServiceRequest parseServiceRequest(JSONObject serviceRequest)
			throws MalformedURLException, DataParsingException, JSONException {
		String serviceRequestId = getString(serviceRequest,
				SERVICE_REQUEST_ID_TAG);
		Status status = Status.getFromString(getString(serviceRequest,
				STATUS_TAG));
		String statusNotes = getString(serviceRequest, STATUS_NOTES_TAG);
		String serviceName = getString(serviceRequest, SERVICE_NAME_TAG);
		String serviceCode = getString(serviceRequest, SERVICE_CODE_TAG);
		String description = getString(serviceRequest, DESCRIPTION_TAG);
		String agencyResponsible = getString(serviceRequest,
				AGENCY_RESPONSIBLE_TAG);
		String serviceNotice = getString(serviceRequest, SERVICE_NOTICE_TAG);
		Date requestedDatetime = dateParser.parseDate(getString(serviceRequest,
				REQUESTED_DATETIME_TAG));
		Date updatedDatetime = dateParser.parseDate(getString(serviceRequest,
				UPDATED_DATETIME_TAG));
		Date expectedDatetime = dateParser.parseDate(getString(serviceRequest,
				EXPECTED_DATETIME_TAG));
		String address = getString(serviceRequest, ADDRESS_TAG);
		Long addressId = getLong(serviceRequest, ADDRESS_ID_TAG);
		Integer zipCode = getInteger(serviceRequest, ZIPCODE_TAG);
		Float latitude = getFloat(serviceRequest, LATITUDE_TAG);
		Float longitude = getFloat(serviceRequest, LONGITUDE_TAG);
		String rawMediaUrl = getString(serviceRequest, MEDIA_URL_TAG).trim();
		URL mediaUrl = buildUrl(rawMediaUrl);
		checkParameters(serviceCode);
		return new ServiceRequest(serviceRequestId, status, statusNotes,
				serviceName, serviceCode, description, agencyResponsible,
				serviceNotice, requestedDatetime, updatedDatetime,
				expectedDatetime, address, addressId, zipCode, latitude,
				longitude, mediaUrl);
	}

	@Override
	public POSTServiceRequestResponse parsePostServiceRequestResponse(
			String rawData) throws DataParsingException {
		JSONArray postServiceRequestResponseArray;
		try {
			postServiceRequestResponseArray = new JSONArray(rawData);
			if (postServiceRequestResponseArray.length() > 0) {
				JSONObject postServiceRequestResponse = postServiceRequestResponseArray
						.getJSONObject(0);
				String token = getString(postServiceRequestResponse, TOKEN_TAG);
				String serviceRequestId = getString(postServiceRequestResponse,
						SERVICE_REQUEST_ID_TAG);
				String serviceNotice = getString(postServiceRequestResponse,
						SERVICE_NOTICE_TAG);
				String accountId = getString(postServiceRequestResponse,
						ACCOUNT_ID_TAG);
				checkParameters(serviceRequestId, token);
				return new POSTServiceRequestResponse(serviceRequestId, token,
						serviceNotice, accountId);
			}
			throw new DataParsingException(
					"The obtained response couldn't be parsed, it may be an error.");
		} catch (JSONException e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public GeoReportV2Error parseGeoReportV2Errors(String rawData)
			throws DataParsingException {
		try {
			JSONArray errorsArray = new JSONArray(rawData);
			if (errorsArray.length() > 0) {
				JSONObject errorObject = errorsArray.getJSONObject(0);
				String code = getString(errorObject, CODE_TAG);
				String description = getString(errorObject, DESCRIPTION_TAG);
				checkParameters(code, description);
				return new GeoReportV2Error(code, description);
			} else {
				throw new DataParsingException(
						"The obtained response is not an error object");
			}
		} catch (JSONException e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public ServiceDiscoveryInfo parseServiceDiscovery(String rawData)
			throws DataParsingException {
		throw new UnsupportedOperationException(
				"This operation is expected to be done by an XML parser.");
	}

	/**
	 * Searches the value of a given tag in a {@link JSONObject}.
	 * 
	 * @param object
	 *            Object to inspect.
	 * @param tag
	 *            Tag to search.
	 * @return The string value of the tag, <code>null</code> if it wasn't
	 *         found.
	 */
	private String getString(JSONObject object, String tag) {
		String result;
		try {
			result = object.getString(tag);
			return result.equals(NULL_STRING_JSON) ? "" : result;
		} catch (JSONException e) {
			return "";
		}
	}

	/**
	 * Searches the value of a given tag in a {@link JSONObject}.
	 * 
	 * @param object
	 *            Object to inspect.
	 * @param tag
	 *            Tag to search.
	 * @return The integer value of the tag, <code>null</code> if it wasn't
	 *         found.
	 * @throws JSONException
	 *             If there was any problem with the parsing.
	 */
	private Integer getInteger(JSONObject object, String tag)
			throws JSONException {
		Long result = getLong(object, tag);
		return result != null ? result.intValue() : null;

	}

	/**
	 * Searches the value of a given tag in a {@link JSONObject}.
	 * 
	 * @param object
	 *            Object to inspect.
	 * @param tag
	 *            Tag to search.
	 * @return The long value of the tag, <code>null</code> if it wasn't found.
	 * @throws JSONException
	 *             If there was any problem with the parsing.
	 */
	private Long getLong(JSONObject object, String tag) throws JSONException {
		if (object.has(tag)) {
			return object.getLong(tag);
		}
		return null;
	}

	/**
	 * Searches the value of a given tag in a {@link JSONObject}.
	 * 
	 * @param object
	 *            Object to inspect.
	 * @param tag
	 *            Tag to search.
	 * @return The float value of the tag, <code>null</code> if it wasn't found.
	 * @throws JSONException
	 *             If there was any problem with the parsing.
	 */
	private Float getFloat(JSONObject object, String tag) throws JSONException {
		Double result = getDouble(object, tag);
		return result != null ? result.floatValue() : null;
	}

	/**
	 * Searches the value of a given tag in a {@link JSONObject}.
	 * 
	 * @param object
	 *            Object to inspect.
	 * @param tag
	 *            Tag to search.
	 * @return The double value of the tag, <code>null</code> if it wasn't
	 *         found.
	 * @throws JSONException
	 *             If there was any problem with the parsing.
	 */
	private Double getDouble(JSONObject object, String tag)
			throws JSONException {
		if (object.has(tag)) {
			return object.getDouble(tag);
		}
		return null;
	}

	/**
	 * Parses a boolean.
	 * 
	 * @param object
	 *            Object to inspect.
	 * @param tag
	 *            Tag to search.
	 * @return <code>null</code> if it wasn't found.
	 * @throws JSONException
	 *             If there was any problem.
	 */
	private Boolean getBoolean(JSONObject object, String tag)
			throws JSONException {
		if (object.has(tag)) {
			return new Boolean(object.getBoolean(tag));
		}
		return null;
	}
}
