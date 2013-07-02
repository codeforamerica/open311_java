package org.codeforamerica.open311.internals.parsing;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.Attribute.Datatype;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser extends AbstractParser {
	private static final String NULL_STRING_JSON = "null";

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
	 */
	private Service getService(JSONObject service) throws JSONException {
		String code = getString(service, SERVICE_CODE_TAG);
		String name = getString(service, SERVICE_NAME_TAG);
		String description = getString(service, DESCRIPTION_TAG);
		boolean metadata = service.getBoolean(METADATA_TAG);
		String group = getString(service, SERVICE_GROUP_TAG);
		String[] keywords = getKeywords(getString(service, KEYWORDS_TAG));
		Service.Type type = Service.Type.getFromString(getString(service,
				TYPE_TAG));
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
			List<Attribute> attributes = new LinkedList<Attribute>();
			for (int i = 0; i < attributesArray.length(); i++) {
				JSONObject attribute = attributesArray.getJSONObject(i);
				boolean variable = attribute.getBoolean(VARIABLE_TAG);
				String code = getString(attribute, CODE_TAG);
				Datatype datatype = Datatype.getFromString(getString(attribute,
						DATATYPE_TAG));
				boolean required = attribute.getBoolean(REQUIRED_TAG);
				String datatypeDescription = getString(attribute,
						DATATYPE_DESCRIPTION_TAG);
				int order = attribute.getInt(ORDER_TAG);
				String description = getString(attribute, DESCRIPTION_TAG);
				JSONArray valuesArray = attribute.getJSONArray(VALUES_TAG);
				Map<String, String> values = new HashMap<String, String>();
				for (int j = 0; j < valuesArray.length(); j++) {
					JSONObject valueObject = valuesArray.getJSONObject(j);
					String key = getString(valueObject, KEY_TAG);
					String name = getString(valueObject, NAME_TAG);
					values.put(key, name);
				}
				attributes.add(new Attribute(variable, code, datatype,
						required, datatypeDescription, order, description,
						values));
			}
			return new ServiceDefinition(serviceCode, attributes);
		} catch (JSONException e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public List<ServiceRequestIdResponse> parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException {
		List<ServiceRequestIdResponse> result = new LinkedList<ServiceRequestIdResponse>();
		try {
			JSONArray responsesArray = new JSONArray(rawData);
			for (int i = 0; i < responsesArray.length(); i++) {
				JSONObject response = responsesArray.getJSONObject(i);
				String token = getString(response, TOKEN_TAG);
				String serviceRequestId = getString(response,
						SERVICE_REQUEST_ID_TAG);
				result.add(new ServiceRequestIdResponse(serviceRequestId, token));
			}
			return result;
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException {
		List<ServiceRequest> result = new LinkedList<ServiceRequest>();
		try {
			JSONArray serviceRequestsArray = new JSONArray(rawData);
			for (int i = 0; i < serviceRequestsArray.length(); i++) {
				JSONObject serviceRequest = serviceRequestsArray
						.getJSONObject(i);
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
				String serviceNotice = getString(serviceRequest,
						SERVICE_NOTICE_TAG);
				DateParser dateParser = DateParser.getInstance();
				Date requestedDatetime = dateParser.parseDate(getString(
						serviceRequest, REQUESTED_DATETIME_TAG));
				Date updatedDatetime = dateParser.parseDate(getString(
						serviceRequest, UPDATED_DATETIME_TAG));
				Date expectedDatetime = dateParser.parseDate(getString(
						serviceRequest, EXPECTED_DATETIME_TAG));
				String address = getString(serviceRequest, ADDRESS_TAG);
				String addressId = getString(serviceRequest, ADDRESS_ID_TAG);
				int zipCode = serviceRequest.getInt(ZIPCODE_TAG);
				float latitude = (float) serviceRequest.getDouble(LATITUDE_TAG);
				float longitude = (float) serviceRequest
						.getDouble(LONGITUDE_TAG);
				String rawMediaUrl = getString(serviceRequest, MEDIA_URL_TAG)
						.trim();
				URL mediaUrl = rawMediaUrl.length() > 0 ? new URL(rawMediaUrl)
						: null;
				result.add(new ServiceRequest(serviceRequestId, status,
						statusNotes, serviceName, serviceCode, description,
						agencyResponsible, serviceNotice, requestedDatetime,
						updatedDatetime, expectedDatetime, address, addressId,
						zipCode, latitude, longitude, mediaUrl));
			}
			return result;
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public List<POSTServiceRequestResponse> parsePostServiceRequestResponse(
			String rawData) throws DataParsingException {
		List<POSTServiceRequestResponse> result = new LinkedList<POSTServiceRequestResponse>();
		JSONArray postServiceRequestResponseArray;
		try {
			postServiceRequestResponseArray = new JSONArray(rawData);

			for (int i = 0; i < postServiceRequestResponseArray.length(); i++) {
				JSONObject postServiceRequestResponse = postServiceRequestResponseArray
						.getJSONObject(i);
				String token = getString(postServiceRequestResponse, TOKEN_TAG);
				String serviceRequestId = getString(postServiceRequestResponse,
						SERVICE_REQUEST_ID_TAG);
				String serviceNotice = getString(postServiceRequestResponse,
						SERVICE_NOTICE_TAG);
				String accountId = getString(postServiceRequestResponse,
						ACCOUNT_ID_TAG);
				result.add(new POSTServiceRequestResponse(serviceRequestId,
						token, serviceNotice, accountId));
			}
			return result;
		} catch (JSONException e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	@Override
	public List<GeoReportV2Error> parseGeoReportV2Errors(String rawData)
			throws DataParsingException {
		List<GeoReportV2Error> errors = new LinkedList<GeoReportV2Error>();
		try {
			JSONArray errorsArray = new JSONArray(rawData);
			for (int i = 0; i < errorsArray.length(); i++) {
				JSONObject errorObject = errorsArray.getJSONObject(i);
				String code = getString(errorObject, CODE_TAG);
				String description = getString(errorObject, DESCRIPTION_TAG);
				errors.add(new GeoReportV2Error(code, description));
			}
			return errors;
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

}
