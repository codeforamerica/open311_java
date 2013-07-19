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
	QUEBEC("Quebec, QC", "http://api.ville.quebec.qc.ca/discovery.xml",
			"ville.quebec.qc.ca"), BONN("Bonn, Germany",
			"http://anliegen.bonn.de/georeport/v2/discovery.xml", ""), SAN_FRANCISCO(
			"San Francisco, CA",
			"https://open311.sfgov.org/dev/v2/discovery.xml", "sfgov.org"), WASHINGTON(
			"Washington D.C.",
			"http://app.311.dc.gov/cwi/Open311/discovery.xml", "dc.gov"), BOSTON(
			"Boston, MA",
			"https://mayors24.cityofboston.gov/open311/discovery.xml",
			"cityofboston.gov"), BALTIMORE("Baltimore, MD",
			"http://311.baltimorecity.gov/open311/discovery.xml",
			"baltimorecity.gov"), GRAND_RAPIDS("Grand Rapids, MI",
			"http://grcity.spotreporters.com/open311/discovery.xml",
			"grcity.us"), BROOKLINE("Brookline, MA",
			"http://spot.brooklinema.gov/open311/discovery.xml",
			"brooklinema.gov"), TORONTO("Toronto, ON",
			"https://secure.toronto.ca/open311test/discovery.xml", "toronto.ca"), BLOOMINGTON(
			"Bloomington, IN",
			"https://bloomington.in.gov/crm/open311/discovery.xml",
			"bloomington.in.gov"), CHICAGO("Chicago, IL",
			"http://311api.cityofchicago.org/open311/discovery.xml",
			"cityofchicago.org"), ALPHARETTA("City of Alpharetta, GA",
			"http://seeclickfix.com/alpharetta/open311/discovery.xml", ""), BAINBRIDGE_ISLAND(
			"Bainbridge Island, WA",
			"http://seeclickfix.com/bainbridge-island/open311/discovery.xml",
			""), CORONA("City of Corona, CA",
			"http://seeclickfix.com/corona/open311/discovery.xml", ""), DELEON(
			"City of DeLeon, TX",
			"http://seeclickfix.com/de-leon/open311/discovery.xml", ""), DUNWOODY(
			"City of Dunwoody, GA",
			"http://seeclickfix.com/dunwoody_ga/open311/discovery.xml", ""), HUNTSVILLE(
			"City of Huntsville, AL",
			"http://seeclickfix.com/huntsville/open311/discovery.xml", ""), NEWBERG(
			"City of Newberg, OR",
			"http://seeclickfix.com/newberg/open311/discovery.xml", ""), NEW_HAVEN(
			"City of New Haven, CT",
			"http://seeclickfix.com/new-haven/open311/discovery.xml", ""), RICHMOND(
			"City of Richmond",
			"http://seeclickfix.com/richmond/open311/discovery.xml", ""), RUSSELL_SPRINGS(
			"City of Russell Springs, KY",
			"http://seeclickfix.com/russell-springs/open311/discovery.xml", ""), TUCSON(
			"City of Tucson, AZ",
			"http://seeclickfix.com/tucson/open311/discovery.xml", ""), DARWIN(
			"Darwin, Australia",
			"http://seeclickfix.com/aus_darwin/open311/discovery.xml", ""), FONTANA(
			"Fontana City Hall",
			"http://seeclickfix.com/fontana/open311/discovery.xml", ""), HOWARD(
			"Howard County Department of Public Works, MD",
			"http://seeclickfix.com/md_howard-county/open311/discovery.xml", ""), MANOR(
			"Manor, TX", "http://seeclickfix.com/manor/open311/discovery.xml",
			""), NEWARK("Newark, OH",
			"http://seeclickfix.com/newark_2/open311/discovery.xml", ""), NEWNAN(
			"Newnan, GA",
			"http://seeclickfix.com/newnan/open311/discovery.xml", ""), OLATHE(
			"Olathe, KS",
			"http://seeclickfix.com/olathe/open311/discovery.xml", ""), RALEIGH(
			"Raleigh City Hall, NC",
			"http://seeclickfix.com/raleigh/open311/discovery.xml", ""), ROOSEVELT(
			"Roosevelt Island, NY",
			"http://seeclickfix.com/roosevelt-island/open311/discovery.xml", ""), HILLSBOROUGH(
			"Town of Hillsborough, CA",
			"http://seeclickfix.com/hillsborough/open311/discovery.xml", "");
	private String cityName;
	private String discoveryUrl;
	private String jurisdictionId;

	private City(String cityName, String discoveryUrl, String jurisdictionId) {
		this.cityName = cityName;
		this.discoveryUrl = discoveryUrl;
		this.jurisdictionId = jurisdictionId;
	}

	public String getCityName() {
		return cityName;
	}

	public String getDiscoveryUrl() {
		return discoveryUrl;
	}

	public String getJurisdictionId() {
		return jurisdictionId;
	}

}