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
}
