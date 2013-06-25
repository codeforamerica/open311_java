package org.codeforamerica.open311.facade;

/**
 * An enumeration of cities which contains the service discovery urls and
 * jurisdiction IDs. <b>NOTE:</b> If you need a city which doesn't appear here,
 * send an email to the author or open an issue in the <a
 * href="https://github.com/codeforamerica/open311_java">GitHub repository</a>.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public enum City {

	SAN_FRANCISCO("https://open311.sfgov.org/dev/v2/discovery.xml", "sfgov.org"), WASHINGTON(
			"http://app.311.dc.gov/cwi/Open311/discovery.xml", "dc.gov"), BOSTON(
			"https://mayors24.cityofboston.gov/open311/discovery.xml",
			"cityofboston.gov"), QUEBEC(
			"http://api.ville.quebec.qc.ca/discovery.xml", "ville.quebec.qc.ca"), BONN(
			"http://anliegen.bonn.de/georeport/v2/discovery.xml", "");

	private String discoveryUrl;
	private String jurisdictionId;

	private City(String discoveryUrl, String jurisdictionId) {
		this.discoveryUrl = discoveryUrl;
		this.jurisdictionId = jurisdictionId;
	}

	public String getDiscoveryUrl() {
		return discoveryUrl;
	}

	public String getJurisdictionId() {
		return jurisdictionId;
	}

}
