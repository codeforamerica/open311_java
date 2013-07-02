package org.codeforamerica.open311.internals.parsing;

import java.util.LinkedList;
import java.util.List;

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
	 * @param service JSONObject which represents a service.
	 * @return An object with its state given by the JSONObject
	 * @throws JSONException
	 */
	private Service getService(JSONObject service) throws JSONException {
		String code = service.getString(SERVICE_CODE_TAG);
		String name = service.getString(SERVICE_NAME_TAG);
		String description = service.getString(DESCRIPTION_TAG);
		boolean metadata = service.getBoolean(METADATA_TAG);
		String group = service.getString(SERVICE_GROUP_TAG);
		String[] keywords = getKeywords(service.getString(KEYWORDS_TAG));
		Service.Type type = Service.Type.getFromString(service
				.getString(TYPE_TAG));
		return new Service(code, name, description, metadata, type, keywords,
				group);
	}

	@Override
	public ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceRequestIdResponse> parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<POSTServiceRequestResponse> parsePostServiceRequestResponse(
			String rawData) throws DataParsingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GeoReportV2Error> parseGeoReportV2Errors(String rawData)
			throws DataParsingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceDiscoveryInfo parseServiceDiscovery(String rawData)
			throws DataParsingException {
		// TODO Auto-generated method stub
		return null;
	}

}
