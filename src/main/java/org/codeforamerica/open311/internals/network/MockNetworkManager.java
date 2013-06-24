package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URL;

/**
 * Mock {@link NetworkManager}, useful for testing.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class MockNetworkManager implements NetworkManager {
	@Override
	public String doGet(URL url) throws IOException {
		if (url.toString().contains("simulateIOException")) {
			throw new IOException();
		}
		if (url.toString().contains("simulateAPIError")) {
			return errorXml();
		}
		if (url.toString().contains("discovery")) {
			return discoveryXml();
		}
		if (url.toString().contains("services.xml")) {
			return serviceListXml();
		}
		if (url.toString().contains("services/")) {
			return serviceDefinitionXml();
		}
		if (url.toString().contains("tokens/")) {
			return serviceRequestIdFromATokenXml();
		}
		if (url.toString().contains("requests")) {
			return serviceRequestsXml();
		}
		return "";
	}

	@Override
	public String doPost(URL url, String body) throws IOException {
		if (url.toString().contains("simulateIOException")) {
			throw new IOException();
		}
		if (url.toString().contains("simulateAPIError")) {
			return errorXml();
		}
		if (url.toString().contains("requests.xml")) {
			// Test api key
			if (url.toString().contains("api_key")
					&& url.toString().contains("api_key=key")) {
				return postServiceRequestResponseXml();
			} else {
				return postServiceRequestResponseXml();
			}
		}

		return "";
	}

	/**
	 * Mock service list.
	 * 
	 * @return service list XML.
	 */
	private String serviceListXml() {
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

	/**
	 * Mock service definition.
	 * 
	 * @return service definition XML.
	 */
	private String serviceDefinitionXml() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_definition>"
				+ "<service_code>DMV66</service_code><attributes><attribute>"
				+ "<variable>true</variable><code>WHISHETN</code><datatype>singlevaluelist</datatype>"
				+ "<required>true</required><datatype_description></datatype_description><order>1</order>"
				+ "<description>What is the ticket/tag/DL number?</description><values><value>"
				+ "<key>123</key><name>Ford</name></value><value><key>124</key><name>Chrysler</name>"
				+ "</value></values></attribute></attributes></service_definition>";
	}

	/**
	 * Mock service request id from a token.
	 * 
	 * @return XML.
	 */
	private String serviceRequestIdFromATokenXml() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests>"
				+ "<request><service_request_id>638344</service_request_id>"
				+ "<token>12345</token></request><request><service_request_id>"
				+ "111</service_request_id><token>12345</token>"
				+ "</request></service_requests>";
	}

	/**
	 * Mock service requests.
	 * 
	 * @return XML.
	 */
	private String serviceRequestsXml() {
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
	 * Mock post service request response.
	 * 
	 * @return XML.
	 */
	public String postServiceRequestResponseXml() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><service_requests><request>"
				+ "<service_request_id>293944</service_request_id><service_notice>"
				+ "The City will inspect and require the responsible party to correct "
				+ "within 24 hours and/or issue a Correction Notice or Notice of Violation "
				+ "of the Public Works Code</service_notice><account_id/></request>"
				+ "</service_requests>";
	}

	/**
	 * Mock error.
	 * 
	 * @return XML.
	 */
	public String errorXml() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?><errors><error><code>403</code><description>Invalid api_key received -- can't proceed with create_request.</description></error>"
				+ "<error><code>404</code><description>Whatever</description></error></errors>";
	}
	/**
	 * Mock service discovery.
	 * @return
	 */
	public String discoveryXml() {
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
				+ "<type>test</type><formats><format>text/xml</format></formats>"
				+ "</endpoint><endpoint><specification>http://wiki.open311.org/GeoReport_v2</specification>"
				+ "<url>https://open311.sfgov.org/v2</url><changeset>"
				+ "2011-04-25T17:48:34Z</changeset><type>production</type><formats>"
				+ "<format>text/xml</format></formats></endpoint>"
				+ "<endpoint><specification>http://wiki.open311.org/GeoReport_v1</specification>"
				+ "<url>https://open311.sfgov.org/dev/v1</url>"
				+ "<changeset>2011-04-20T17:48:34Z</changeset><type>test</type><formats>"
				+ "<format>text/xml</format></formats></endpoint>"
				+ "<endpoint><specification>http://wiki.open311.org/GeoReport_v1"
				+ "</specification><url>https://open311.sfgov.org/v1</url>"
				+ "<changeset>2011-04-25T17:48:34Z</changeset>"
				+ "<type>production</type><formats><format>text/xml</format></formats>"
				+ "</endpoint></endpoints></discovery>";
	}
}
