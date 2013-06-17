package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * @author: Santiago Munín <santimunin@gmail.com>
 */
public class MockNetworkManager implements NetworkManager {
	@Override
	public String doGet(URL url, Map<String, String> parameters)
			throws IOException {
		if (url.toString().contains("services.xml")) {
			return serviceListXml();
		}
		if (url.toString().contains("services/")) {
			return serviceDefinitionXml();
		}
		if (url.toString().contains("tokens/")) {
			return serviceRequestIdFromATokenXml();
		}
		if (url.toString().contains("requests.xml")) {
			return serviceRequestsXml();
		}
		return "";
	}

	@Override
	public String doPost(URL url, Map<String, String> parameters)
			throws IOException {
		throw new UnsupportedOperationException();
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
}
