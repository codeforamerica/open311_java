package org.codeforamerica.open311.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codeforamerica.open311.facade.data.AttributeInfo;
import org.codeforamerica.open311.facade.data.AttributeInfo.Datatype;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.codeforamerica.open311.internals.parsing.DateParser;

/**
 * This class contains test which will be use for more than one test class. For
 * example, the data parsing of a GET Service has to return the same as the
 * whole GET Service done by the APIWrapper.
 */
public class GlobalTests {
	private static DateParser dateParser = new DateParser();

	public static void serviceListTest(List<Service> services) {
		assertEquals(services.size(), 2);
		Service service1 = services.get(0);
		assertEquals(service1.getServiceCode(), "001");
		assertEquals(service1.getServiceName(), "Cans left out 24x7");
		assertEquals(service1.getDescription(),
				"Garbage or recycling cans that have been left "
						+ "out for more than 24 hours after collection. "
						+ "Violators will be cited.");
		assertTrue(service1.hasMetadata());
		assertEquals(service1.getType(), Service.Type.REALTIME);
		assertEquals(service1.getGroup(), "sanitation");
		String[] keywordList = service1.getKeywords();
		assertEquals(keywordList.length, 3);
		assertEquals(keywordList[0], "lorem");
		assertEquals(keywordList[1], "ipsum");
		assertEquals(keywordList[2], "dolor");
	}

	public static void serviceDefinitionTest(ServiceDefinition serviceDefinition) {
		assertEquals(serviceDefinition.getServiceCode(), "DMV66");
		AttributeInfo at1 = serviceDefinition.getAttributes().get(0);
		assertEquals(at1.isVariable(), true);
		assertEquals(at1.getCode(), "WHISHETN");
		assertEquals(at1.getDatatype(), Datatype.SINGLEVALUELIST);
		assertEquals(at1.isRequired(), true);
		assertEquals(at1.getDatatypeDescription(), "");
		assertEquals(at1.getOrder(), 1);
		assertEquals(at1.getDescription(), "What is the ticket/tag/DL number?");
		assertEquals(at1.getValues().get("123"), "Ford");
		assertEquals(at1.getValues().get("124"), "Chrysler");
	}

	public static void serviceIdFromTokenTest(ServiceRequestIdResponse id) {
		assertEquals(id.getToken(), "12345");
		assertEquals(id.getServiceRequestId(), "638344");
	}

	public static void serviceRequestTest(ServiceRequest sr1)
			throws MalformedURLException {
		assertEquals(sr1.getServiceRequestId(), "638344");
		assertEquals(sr1.getStatus(), Status.CLOSED);
		assertEquals(sr1.getStatusNotes(), "Duplicate request.");
		assertEquals(sr1.getServiceName(), "Sidewalk and Curb Issues");
		assertEquals(sr1.getServiceCode(), "006");
		assertEquals(sr1.getDescription(), "");
		assertEquals(sr1.getAgencyResponsible(), "");
		assertEquals(sr1.getServiceNotice(), "");
		assertEquals(dateParser.printDate(sr1.getRequestedDatetime()),
				dateParser.printDate(dateParser
						.parseDate("2010-04-14T06:37:38-08:00")));
		assertEquals(dateParser.printDate(sr1.getUpdatedDatetime()),
				dateParser.printDate(dateParser
						.parseDate("2010-04-14T06:37:38-08:00")));
		assertEquals(dateParser.printDate(sr1.getExpectedDatetime()),
				dateParser.printDate(dateParser
						.parseDate("2010-04-15T06:37:38-08:00")));
		assertEquals(sr1.getAddress(), "8TH AVE and JUDAH ST");
		assertEquals(sr1.getAddressId(), 545483);
		assertEquals(sr1.getZipCode(), 94122);
		assertTrue(sr1.getLatitude() == 37.762221815F);
		assertTrue(sr1.getLongitude() == -122.4651145F);
		assertEquals(sr1.getMediaUrl(), new URL(
				"http://city.gov.s3.amazonaws.com/requests/media/638344.jpg"));
	}

	public static void serviceRequestsTest(List<ServiceRequest> requests)
			throws MalformedURLException {
		assertEquals(requests.size(), 2);
		ServiceRequest sr1 = requests.get(0);
		serviceRequestTest(sr1);
	}

	public static void postServiceRequestsTest(
			POSTServiceRequestResponse response) {
		assertEquals(response.getAccountId(), "");
		assertEquals(response.getToken(), "");
		assertEquals(response.getServiceRequestId(), "293944");
		assertEquals(
				response.getServiceNotice(),
				"The City will inspect and require the responsible party to correct within 24 hours and/or issue a Correction Notice or Notice of Violation of the Public Works Code");
	}

	public static void errorTest(List<GeoReportV2Error> error) {

		assertEquals(error.size(), 2);
		GeoReportV2Error error1 = error.get(0);
		assertEquals(error1.getCode(), "403");
		assertEquals(error1.getDescription(),
				"Invalid api_key received -- can't proceed with create_request.");
		assertEquals(
				error1.toString(),
				"GeoReportError #403: Invalid api_key received -- can't proceed with create_request.");
		GeoReportV2Error error2 = error.get(1);
		assertEquals(error2.getCode(), "404");
		assertEquals(error2.getDescription(), "Whatever");
		assertEquals(error2.toString(), "GeoReportError #404: Whatever");
	}

}
