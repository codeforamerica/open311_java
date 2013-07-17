package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URL;

import org.codeforamerica.open311.facade.Format;

/**
 * Mock {@link NetworkManager}, useful for testing.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class MockNetworkManager implements NetworkManager {
	private Format format = Format.XML;

	@Override
	public String doGet(URL url) throws IOException {
		if (url.toString().contains("simulateIOException")) {
			throw new IOException();
		}
		if (format == Format.XML) {
			return XMLResponse(url);
		}
		if (format == Format.JSON) {
			return JSONResponse(url);
		}
		return "";
	}

	@Override
	public String doPost(URL url, String body) throws IOException {
		if (url.toString().contains("simulateIOException")) {
			throw new IOException();
		}
		if (format == Format.XML) {
			return XMLPOSTResponse(url);
		}
		if (format == Format.JSON) {
			return JSONPOSTResponse(url);
		}
		return "";

	}

	/**
	 * Selects the response if the format is XML.
	 * 
	 * @param url
	 *            Request URL.
	 * @return Empty if it doesn't find any suitable response.
	 */
	private String XMLResponse(URL url) {
		if (url.toString().contains("simulateAPIError")) {
			return errorXML();
		}
		if (url.toString().contains("discovery")) {
			return discoveryXML();
		}
		if (url.toString().contains("services.xml")) {
			return serviceListXML();
		}
		if (url.toString().contains("services/")) {
			return serviceDefinitionXML();
		}
		if (url.toString().contains("tokens/")) {
			return serviceRequestIdFromATokenXML();
		}
		if (url.toString().contains("requests")) {
			return serviceRequestsXML();
		}
		return "";
	}

	/**
	 * Selects the response if the format is JSON.
	 * 
	 * @param url
	 *            Request URL.
	 * @return Empty if it doesn't find any suitable response.
	 */
	private String JSONResponse(URL url) {
		if (url.toString().contains("services.json")) {
			return serviceListJSON();
		}
		if (url.toString().contains("services/")) {
			return serviceDefinitionJSON();
		}
		if (url.toString().contains("tokens/")) {
			return serviceRequestIdFromATokenJSON();
		}
		if (url.toString().contains("requests")) {
			return serviceRequestsJSON();
		}
		return "";
	}

	/**
	 * Selects the response if the format is JSON.
	 * 
	 * @param url
	 *            Request URL.
	 * @return Empty if it doesn't find any suitable response.
	 */
	private String XMLPOSTResponse(URL url) {
		if (url.toString().contains("simulateAPIError")) {
			return errorXML();
		}
		if (url.toString().contains("requests.xml")) {
			// Test api key
			if (url.toString().contains("api_key")) {
				if (url.toString().contains("api_key=key")) {
					return postServiceRequestResponseXML();
				} else
					return errorXML();
			} else {
				return postServiceRequestResponseXML();
			}
		}
		return "";
	}

	/**
	 * Selects the response if the format is JSON.
	 * 
	 * @param url
	 *            Request URL.
	 * @return Empty if it doesn't find any suitable response.
	 */
	private String JSONPOSTResponse(URL url) {
		if (url.toString().contains("simulateAPIError")) {
			return errorJSON();
		}
		if (url.toString().contains("requests.json")) {
			// Test api key
			if (url.toString().contains("api_key")) {
				if (url.toString().contains("api_key=key")) {
					return postServiceRequestResponseJSON();
				} else
					return errorJSON();
			} else {
				return postServiceRequestResponseJSON();
			}
		}
		return "";
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * Mock service list.
	 * 
	 * @return service list XML.
	 */
	private String serviceListXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><services><service><service_code>"
				+ "001</service_code><service_name>Cans left out 24x7</service_name><description>"
				+ "Garbage or recycling cans that have been left out for more than 24 hours after"
				+ " collection. Violators will be cited.</description><metadata>true</metadata>"
				+ "<type>realtime</type><keywords>lorem, ipsum, dolor</keywords><group>sanitation"
				+ "</group></service><service><service_code>002</service_code><metadata>true</metadata>"
				+ "<type>realtime</type><keywords>lorem, ipsum, dolor</keywords><group>street</group>"
				+ "<service_name>Construction plate shifted</service_name>"
				+ "<description>Metal construction plate covering the street or sidewalk has been moved."
				+ "</description></service></services>";
	}

	private String serviceListJSON() {
		return "[{\"service_code\":\"001\",\"service_name\":\"Cans left out 24x7\",\"description\":\"Garbage or recycling cans that have been left out for more than 24 hours after collection. Violators will be cited.\",\"metadata\":true,\"type\":\"realtime\","
				+ "\"keywords\":\"lorem, ipsum, dolor\",\"group\":\"sanitation\"},{\"service_code\":\"002\",\"metadata\":true,\"type\":\"realtime\",\"keywords\":\"lorem, ipsum, dolor\",\"group\":\"street\",\"service_name\":\"Construction plate shifted\","
				+ "\"description\":\"Metal construction plate covering the street or sidewalk has been moved.\"}]";
	}

	/**
	 * Mock service definition.
	 * 
	 * @return service definition XML.
	 */
	private String serviceDefinitionXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_definition>"
				+ "<service_code>DMV66</service_code><attributes><attribute>"
				+ "<variable>true</variable><code>WHISHETN</code><datatype>singlevaluelist</datatype>"
				+ "<required>true</required><datatype_description></datatype_description><order>1</order>"
				+ "<description>What is the ticket/tag/DL number?</description><values><value>"
				+ "<key>123</key><name>Ford</name></value><value><key>124</key><name>Chrysler</name>"
				+ "</value></values></attribute></attributes></service_definition>";
	}

	/**
	 * Mock service definition.
	 * 
	 * @return service definition JSON.
	 */
	private String serviceDefinitionJSON() {
		return "{\"service_code\":\"DMV66\",\"attributes\":[{\"variable\":true,\"code\":\"WHISHETN\",\"datatype\":\"singlevaluelist\","
				+ "\"required\":true,\"datatype_description\":null,\"order\":1,\"description\":\"What is the ticket/tag/DL number?\","
				+ "\"values\":[{\"key\":123,\"name\":\"Ford\"},{\"key\":124,\"name\":\"Chrysler\"}]}]}";
	}

	/**
	 * Mock service request id from a token.
	 * 
	 * @return XML.
	 */
	private String serviceRequestIdFromATokenXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests>"
				+ "<request><service_request_id>638344</service_request_id>"
				+ "<token>12345</token></request></service_requests>";
	}

	/**
	 * Mock service request id from a token.
	 * 
	 * @return JSON.
	 */
	private String serviceRequestIdFromATokenJSON() {
		return "[{\"service_request_id\":638344,\"token\":12345},{\"service_request_id\":111,\"token\":12345}]";
	}

	/**
	 * Mock service requests.
	 * 
	 * @return XML.
	 */
	private String serviceRequestsXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests>"
				+ "<request><service_request_id>638344</service_request_id>"
				+ "<status>closed</status><status_notes>Duplicate request."
				+ "</status_notes><service_name>Sidewalk and Curb Issues</service_name>"
				+ "<service_code>006</service_code><description></description>"
				+ "<agency_responsible></agency_responsible><service_notice></service_notice>"
				+ "<requested_datetime>2010-04-14T06:37:38-08:00</requested_datetime>"
				+ "<updated_datetime>2010-04-14T06:37:38-08:00</updated_datetime>"
				+ "<expected_datetime>2010-04-15T06:37:38-08:00</expected_datetime><address>"
				+ "8TH AVE and JUDAH ST</address>"
				+ "<address_id>545483</address_id><zipcode>94122</zipcode>"
				+ "<lat>37.762221815</lat><long>-122.4651145</long>"
				+ "<media_url>http://city.gov.s3.amazonaws.com/requests/media/638344.jpg "
				+ "</media_url></request>"
				+ "<request><service_request_id>638349</service_request_id><status>open</status"
				+ "><status_notes></status_notes><service_name>Sidewalk and Curb Issues</service_name>"
				+ "<service_code>006</service_code><description></description><agency_responsible>"
				+ "</agency_responsible>"
				+ "<service_notice></service_notice><requested_datetime>2010-04-19T06:37:38-08:00"
				+ "</requested_datetime><updated_datetime>2010-04-19T06:37:38-08:00</updated_datetime>"
				+ "<expected_datetime>2010-04-19T06:37:38-08:00</expected_datetime>"
				+ "<address>8TH AVE and JUDAH ST</address><address_id>545483"
				+ "</address_id><zipcode>94122</zipcode>"
				+ "<lat>37.762221815</lat><long>-122.4651145</long>"
				+ "<media_url>http://city.gov.s3.amazonaws.com/requests/media/638349.jpg </media_url>"
				+ "</request></service_requests>";
	}

	/**
	 * Mock service requests.
	 * 
	 * @return JSON.
	 */
	private String serviceRequestsJSON() {
		return "[{\"service_request_id\":638344,\"status\":\"closed\",\"status_notes\":"
				+ "\"Duplicate request.\",\"service_name\":\"Sidewalk and Curb Issues\","
				+ "\"service_code\":\"006\",\"description\":null,\"agency_responsible\":null,"
				+ "\"service_notice\":null,\"requested_datetime\":\"2010-04-14T06:37:38-08:00\","
				+ "\"updated_datetime\":\"2010-04-14T06:37:38-08:00\",\"expected_datetime\":"
				+ "\"2010-04-15T06:37:38-08:00\",\"address\":\"8TH AVE and JUDAH ST\","
				+ "\"address_id\":545483,\"zipcode\":94122,\"lat\":37.762221815,\"long\":"
				+ "-122.4651145,\"media_url\":\"http://city.gov.s3.amazonaws.com/requests"
				+ "/media/638344.jpg \"},{\"service_request_id\":638349,\"status\":\"open\","
				+ "\"status_notes\":null,\"service_name\":\"Sidewalk and Curb Issues\","
				+ "\"service_code\":\"006\",\"description\":null,\"agency_responsible\":null,"
				+ "\"service_notice\":null,\"requested_datetime\":\"2010-04-19T06:37:38-08:00\","
				+ "\"updated_datetime\":\"2010-04-19T06:37:38-08:00\",\"expected_datetime\":\""
				+ "2010-04-19T06:37:38-08:00\",\"address\":\"8TH AVE and JUDAH ST\",\"address_id\":"
				+ "545483,\"zipcode\":94122,\"lat\":37.762221815,\"long\":-122.4651145,"
				+ "\"media_url\":\"http://city.gov.s3.amazonaws.com/requests/media/638349.jpg\"}]";
	}

	/**
	 * Mock post service request response.
	 * 
	 * @return XML.
	 */
	public String postServiceRequestResponseXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests><request>"
				+ "<service_request_id>293944</service_request_id><service_notice>"
				+ "The City will inspect and require the responsible party to correct "
				+ "within 24 hours and/or issue a Correction Notice or Notice of Violation "
				+ "of the Public Works Code</service_notice><account_id/></request>"
				+ "</service_requests>";
	}

	/**
	 * Mock post service request response.
	 * 
	 * @return JSON.
	 */
	public String postServiceRequestResponseJSON() {
		return "[{\"service_request_id\":293944,\"service_notice\":"
				+ "\"The City will inspect and require the responsible party to correct within 24"
				+ " hours and/or issue a Correction Notice or Notice of Violation of the Public Works Code\","
				+ "\"account_id\":null}]";
	}

	/**
	 * Mock error.
	 * 
	 * @return XML.
	 */
	public String errorXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><errors><error><code>403</code><description>Invalid api_key received -- can't proceed with create_request.</description></error>"
				+ "<error><code>404</code><description>Whatever</description></error></errors>";
	}

	/**
	 * Mock error.
	 * 
	 * @return JSON.
	 */
	public String errorJSON() {
		return "[{\"code\":403,\"description\":\"Invalid api_key received -- can't proceed with create_request.\"},{\"code\":404,\"description\":\"Whatever\"}]";
	}

	/**
	 * Mock service discovery.
	 * 
	 * @return XML.
	 */
	public String discoveryXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><discovery xmlns:m=\"http://org/sfgov/sf311v2/services\" "
				+ "xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<changeset>2011-04-05T17:48:34Z</changeset><contact>Please email "
				+ "( content.311@sfgov.org )  or call ( 415-701-2311 ) for assistance "
				+ "or to report bugs</contact><key_service>To get an API_KEY please "
				+ "visit this website:  http://apps.sfgov.org/Open311API/?page_id=486"
				+ "</key_service><endpoints>"
				+ "<endpoint><specification>http://wiki.open311.org/GeoReport_v2"
				+ "</specification><url>https://open311.sfgov.org/dev/v2</url>"
				+ "<changeset>2011-04-20T17:48:34Z</changeset>"
				+ "<type>test</type><formats><format>text/XML</format></formats>"
				+ "</endpoint><endpoint><specification>http://wiki.open311.org/GeoReport_v2</specification>"
				+ "<url>https://open311.sfgov.org/v2</url><changeset>"
				+ "2011-04-25T17:48:34Z</changeset><type>production</type><formats>"
				+ "<format>text/XML</format></formats></endpoint>"
				+ "<endpoint><specification>http://wiki.open311.org/GeoReport_v1</specification>"
				+ "<url>https://open311.sfgov.org/dev/v1</url>"
				+ "<changeset>2011-04-20T17:48:34Z</changeset><type>test</type><formats>"
				+ "<format>text/XML</format></formats></endpoint>"
				+ "<endpoint><specification>http://wiki.open311.org/GeoReport_v1"
				+ "</specification><url>https://open311.sfgov.org/v1</url>"
				+ "<changeset>2011-04-25T17:48:34Z</changeset>"
				+ "<type>production</type><formats><format>text/xml</format></formats>"
				+ "</endpoint></endpoints></discovery>";
	}
}
