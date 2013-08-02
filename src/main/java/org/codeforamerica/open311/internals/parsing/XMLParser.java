package org.codeforamerica.open311.internals.parsing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codeforamerica.open311.facade.EndpointType;
import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.data.AttributeInfo;
import org.codeforamerica.open311.facade.data.AttributeInfo.Datatype;
import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Parses XML files using DOM.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class XMLParser extends AbstractParser {

	private DocumentBuilder dBuilder;
	private DateParser dateParser = new DateParser();

	/**
	 * Creates an instance of an XMLParser creating a {@link DocumentBuilder}.
	 */
	public XMLParser() {
		try {
			dBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new Error(
					"Cannot create a DocumentBuilder which satisfies the configuration requested.");
		}
	}

	@Override
	public List<Service> parseServiceList(String rawData)
			throws DataParsingException {
		List<Service> result = new LinkedList<Service>();
		try {
			Document doc = getDocument(rawData);
			NodeList serviceNodeList = doc.getElementsByTagName(SERVICE_TAG);
			for (int i = 0; i < serviceNodeList.getLength(); i++) {
				Node serviceNode = serviceNodeList.item(i);
				if (serviceNode.getNodeType() == Node.ELEMENT_NODE) {
					result.add(getService((Element) serviceNode));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
		return result;
	}

	/**
	 * Builds a {@link Service} object from an {@link Element} of the DOM.
	 * 
	 * @param serviceElement
	 *            A valid element node of a service.
	 * @return An object wrapping the information contained in the given
	 *         element.
	 */
	private Service getService(Element serviceElement) {
		String code = getTagContent(serviceElement, SERVICE_CODE_TAG);
		String name = getTagContent(serviceElement, SERVICE_NAME_TAG);
		String description = getTagContent(serviceElement, DESCRIPTION_TAG);
		Boolean metadata = parseBoolean(getTagContent(serviceElement,
				METADATA_TAG));
		String group = getTagContent(serviceElement, SERVICE_GROUP_TAG);
		String[] keywords = getKeywords(getTagContent(serviceElement,
				KEYWORDS_TAG));
		Service.Type type = Service.Type.getFromString(getTagContent(
				serviceElement, TYPE_TAG));
		return new Service(code, name, description, metadata, type, keywords,
				group);
	}

	@Override
	public ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException {
		try {
			Document doc = getDocument(rawData);
			NodeList serviceDefinitionNodeList = doc
					.getElementsByTagName(SERVICE_DEFINITION_TAG);
			for (int i = 0; i < serviceDefinitionNodeList.getLength(); i++) {
				Node serviceDefinitionNode = serviceDefinitionNodeList.item(i);
				if (serviceDefinitionNode.getNodeType() == Node.ELEMENT_NODE) {
					return getServiceDefinition((Element) serviceDefinitionNode);
				}
			}
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
		return null;
	}

	/**
	 * Builds a {@link ServiceDefinition} object from an {@link Element} of the
	 * DOM.
	 * 
	 * @param serviceDefinitionElement
	 *            A valid element node of a service definition.
	 * @return An object wrapping the information contained in the given
	 *         element.
	 */
	private ServiceDefinition getServiceDefinition(
			Element serviceDefinitionElement) {
		String serviceCode = getTagContent(serviceDefinitionElement,
				SERVICE_CODE_TAG);
		NodeList attributesNodeList = serviceDefinitionElement
				.getElementsByTagName(ATTRIBUTE_TAG);
		List<AttributeInfo> attributes = new LinkedList<AttributeInfo>();
		for (int j = 0; j < attributesNodeList.getLength(); j++) {
			Node attributeNode = attributesNodeList.item(j);
			if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element attributeElement = (Element) attributeNode;
				Boolean variable = parseBoolean(getTagContent(attributeElement,
						VARIABLE_TAG));
				String code = getTagContent(attributeElement, CODE_TAG);
				Datatype datatype = Datatype.getFromString(getTagContent(
						attributeElement, DATATYPE_TAG));
				Boolean required = parseBoolean(getTagContent(attributeElement,
						REQUIRED_TAG));
				String datatypeDescription = getTagContent(attributeElement,
						DATATYPE_DESCRIPTION_TAG);
				Integer order = parseInt(getTagContent(attributeElement,
						ORDER_TAG));
				String description = getTagContent(attributeElement,
						DESCRIPTION_TAG);
				Map<String, String> values = new HashMap<String, String>();
				NodeList valuesNodeList = attributeElement
						.getElementsByTagName(VALUE_TAG);
				for (int k = 0; k < valuesNodeList.getLength(); k++) {
					Node valueNode = valuesNodeList.item(k);
					if (valueNode.getNodeType() == Node.ELEMENT_NODE) {
						Element valueElement = (Element) valueNode;
						String key = getTagContent(valueElement, KEY_TAG);
						String name = getTagContent(valueElement, NAME_TAG);
						values.put(key, name);
					}
				}
				attributes.add(new AttributeInfo(variable, code, datatype,
						required, datatypeDescription, order, description,
						values));
			}

		}
		return new ServiceDefinition(serviceCode, attributes);
	}

	@Override
	public ServiceRequestIdResponse parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException {
		try {
			Document doc = getDocument(rawData);
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					return getServiceRequestIdResponse((Element) serviceRequestIdNode);
				}
			}
			return null;
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	/**
	 * Builds a {@link ServiceRequestIdResponse} object from an {@link Element}
	 * of the DOM.
	 * 
	 * @param serviceRequestIdElement
	 *            A valid element node of a service request id.
	 * @return An object wrapping the information contained in the given
	 *         element.
	 */
	private ServiceRequestIdResponse getServiceRequestIdResponse(
			Element serviceRequestIdElement) {
		String token = getTagContent(serviceRequestIdElement, TOKEN_TAG);
		String serviceRequestId = getTagContent(serviceRequestIdElement,
				SERVICE_REQUEST_ID_TAG);
		return new ServiceRequestIdResponse(serviceRequestId, token);
	}

	@Override
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException {
		List<ServiceRequest> result = new LinkedList<ServiceRequest>();
		try {
			Document doc = getDocument(rawData);
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					result.add(getServiceRequest((Element) serviceRequestIdNode));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
		return result;
	}

	/**
	 * Builds a {@link ServiceRequest} object from an {@link Element} of the
	 * DOM.
	 * 
	 * @param serviceRequestElement
	 *            A valid element node of a service request.
	 * @return An object wrapping the information contained in the given
	 *         element.
	 * @throws MalformedURLException
	 *             If the URL given in the {@link Element} object is not valid.
	 */
	private ServiceRequest getServiceRequest(Element serviceRequestElement)
			throws MalformedURLException {
		String serviceRequestId = getTagContent(serviceRequestElement,
				SERVICE_REQUEST_ID_TAG);
		Status status = Status.getFromString(getTagContent(
				serviceRequestElement, STATUS_TAG));
		String statusNotes = getTagContent(serviceRequestElement,
				STATUS_NOTES_TAG);
		String serviceName = getTagContent(serviceRequestElement,
				SERVICE_NAME_TAG);
		String serviceCode = getTagContent(serviceRequestElement,
				SERVICE_CODE_TAG);
		String description = getTagContent(serviceRequestElement,
				DESCRIPTION_TAG);
		String agencyResponsible = getTagContent(serviceRequestElement,
				AGENCY_RESPONSIBLE_TAG);
		String serviceNotice = getTagContent(serviceRequestElement,
				SERVICE_NOTICE_TAG);
		Date requestedDatetime = dateParser.parseDate((getTagContent(
				serviceRequestElement, REQUESTED_DATETIME_TAG)));
		Date updatedDatetime = dateParser.parseDate((getTagContent(
				serviceRequestElement, UPDATED_DATETIME_TAG)));
		Date expectedDatetime = dateParser.parseDate((getTagContent(
				serviceRequestElement, EXPECTED_DATETIME_TAG)));
		String address = getTagContent(serviceRequestElement, ADDRESS_TAG);
		Long addressId = parseLong(getTagContent(serviceRequestElement,
				ADDRESS_ID_TAG));
		Integer zipCode = parseInt(getTagContent(serviceRequestElement,
				ZIPCODE_TAG));
		Float latitude = parseFloat(getTagContent(serviceRequestElement,
				LATITUDE_TAG));
		Float longitude = parseFloat(getTagContent(serviceRequestElement,
				LONGITUDE_TAG));
		String rawMediaUrl = getTagContent(serviceRequestElement, MEDIA_URL_TAG)
				.trim();
		URL mediaUrl = buildUrl(rawMediaUrl);
		return new ServiceRequest(serviceRequestId, status, statusNotes,
				serviceName, serviceCode, description, agencyResponsible,
				serviceNotice, requestedDatetime, updatedDatetime,
				expectedDatetime, address, addressId, zipCode, latitude,
				longitude, mediaUrl);
	}

	@Override
	public POSTServiceRequestResponse parsePostServiceRequestResponse(
			String rawData) throws DataParsingException {
		try {
			Document doc = getDocument(rawData);
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					return getPostServiceRequestResponse((Element) serviceRequestIdNode);
				}
			}
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
		throw new DataParsingException(
				"The obtained response couldn't be parsed, it may be an error.");
	}

	/**
	 * Builds a {@link POSTServiceRequestResponse} object from an
	 * {@link Element} of the DOM.
	 * 
	 * @param postServiceRequestResponseElement
	 *            A valid element node of a post service request response..
	 * @return An object wrapping the information contained in the given
	 *         element.
	 */
	private POSTServiceRequestResponse getPostServiceRequestResponse(
			Element postServiceRequestResponseElement) {
		String token = getTagContent(postServiceRequestResponseElement,
				TOKEN_TAG);
		String serviceRequestId = getTagContent(
				postServiceRequestResponseElement, SERVICE_REQUEST_ID_TAG);
		String serviceNotice = getTagContent(postServiceRequestResponseElement,
				SERVICE_NOTICE_TAG);
		String accountId = getTagContent(postServiceRequestResponseElement,
				ACCOUNT_ID_TAG);
		return new POSTServiceRequestResponse(serviceRequestId, token,
				serviceNotice, accountId);
	}

	@Override
	public GeoReportV2Error parseGeoReportV2Errors(String rawData)
			throws DataParsingException {
		try {
			Document doc = getDocument(rawData);
			NodeList errorNodes = doc.getElementsByTagName(ERROR_TAG);
			for (int i = 0; i < errorNodes.getLength(); i++) {
				Node errorNode = errorNodes.item(i);
				if (errorNode.getNodeType() == Node.ELEMENT_NODE) {
					return getGeoReportErrorFromXMLElement((Element) errorNode);
				}
			}
			throw new DataParsingException(
					"The obtained response is not an error object");
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
	}

	/**
	 * Builds a {@link GeoReportV2Error} object from an {@link Element} of the
	 * DOM.
	 * 
	 * @param geoReportErrorElement
	 *            Valid element node of a geo report error.
	 * @return An object wrapping the information contained in the given
	 *         element.
	 */
	private GeoReportV2Error getGeoReportErrorFromXMLElement(
			Element geoReportErrorElement) {
		String code = getTagContent(geoReportErrorElement, CODE_TAG);
		String description = getTagContent(geoReportErrorElement,
				DESCRIPTION_TAG);
		return new GeoReportV2Error(code, description);
	}

	@Override
	public ServiceDiscoveryInfo parseServiceDiscovery(String rawData)
			throws DataParsingException {
		try {
			Document doc = getDocument(rawData);
			NodeList serviceDiscoveryNodes = doc
					.getElementsByTagName(DISCOVERY_TAG);
			for (int i = 0; i < serviceDiscoveryNodes.getLength(); i++) {
				Node serviceDiscoveryNode = serviceDiscoveryNodes.item(i);
				if (serviceDiscoveryNode.getNodeType() == Node.ELEMENT_NODE) {
					Element serviceDiscoveryElement = (Element) serviceDiscoveryNode;
					Date changeset = dateParser.parseDate(getTagContent(
							serviceDiscoveryElement, CHANGESET_TAG));
					String contact = getTagContent(serviceDiscoveryElement,
							CONTACT_TAG);
					String keyService = getTagContent(serviceDiscoveryElement,
							KEY_SERVICE_TAG);
					return new ServiceDiscoveryInfo(changeset, contact,
							keyService,
							parseEndpoints(serviceDiscoveryElement
									.getElementsByTagName(ENDPOINT_TAG),
									serviceDiscoveryElement));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException(e.getMessage());
		}
		return null;
	}

	/**
	 * Builds a list of endpoints.
	 * 
	 * @param endpointsList
	 *            List of nodes with the "endpoint" tag.
	 * @param serviceDiscoveryElement
	 *            In some service discovery files, the specification url is not
	 *            inside the endpoint field but in the same level. This
	 *            parameter allows to fetch it in that case.
	 * @return List of endpoints.
	 */
	private List<Endpoint> parseEndpoints(NodeList endpointsList,
			Element serviceDiscoveryElement) {
		List<Endpoint> result = new LinkedList<Endpoint>();
		for (int i = 0; i < endpointsList.getLength(); i++) {
			Node endpointNode = endpointsList.item(i);
			if (endpointNode.getNodeType() == Node.ELEMENT_NODE) {
				Element endpointElement = (Element) endpointNode;
				String specificationUrl = getTagContent(endpointElement,
						SPECIFICATION_TAG);
				if (specificationUrl.length() == 0) {
					specificationUrl = getTagContent(serviceDiscoveryElement,
							SPECIFICATION_TAG);
				}
				String url = getTagContent(endpointElement, URL_TAG);
				Date changeset = dateParser.parseDate(getTagContent(
						endpointElement, CHANGESET_TAG));
				EndpointType type = EndpointType.getFromString(getTagContent(
						endpointElement, TYPE_TAG));
				NodeList formatNodes = endpointElement
						.getElementsByTagName(FORMAT_TAG);
				List<Format> formats = new LinkedList<Format>();
				for (int j = 0; j < formatNodes.getLength(); j++) {
					Node formatNode = formatNodes.item(j);
					if (formatNode.getNodeType() == Node.ELEMENT_NODE) {
						Element formatElement = (Element) formatNode;
						formats.add(Format
								.getFromHTTPContentTypeString(formatElement
										.getTextContent()));
					}
				}
				result.add(new Endpoint(specificationUrl, url, changeset, type,
						formats));
			}
		}
		return result;
	}

	/**
	 * Returns a {@link Document} representing the DOM of the XML string.
	 * 
	 * @param rawData
	 *            XML UTF-8 encoded string.
	 * @return A document built with the given data.
	 * @throws UnsupportedEncodingException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Document getDocument(String rawData)
			throws UnsupportedEncodingException, SAXException, IOException {
		Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(
				rawData.getBytes(DataParser.TEXT_FORMAT))));
		doc.getDocumentElement().normalize();
		return doc;
	}

	/**
	 * Returns the value of a tag inside an element.
	 * 
	 * @param element
	 *            Element.
	 * @param tag
	 *            Tag inside the element.
	 * @return content of the tag inside the element or <code>""</code> if the
	 *         tag doesn't exists.
	 */
	private String getTagContent(Element element, String tag) {
		NodeList nodeList = element.getElementsByTagName(tag);
		if (nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		}
		return "";
	}

	/**
	 * Parses a string and returns an integer. <b>NOTE<b>: Be careful and notice
	 * that this function will return a <code>null</code> if the given string is
	 * not valid. This approach works well here, but not generally.
	 * 
	 * @param rawInt
	 *            A string which represents an integer number.
	 * @return The integer value (or <code>null</code> if the rawInt is empty or
	 *         <code>null</code>).
	 */
	private int parseInt(String rawInt) {
		try {
			return (rawInt != null && rawInt.length() > 0) ? Integer
					.parseInt(rawInt) : 0;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Parses a string and returns a long. <b>NOTE<b>: Be careful and notice
	 * that this function will return a <code>null</code> if the given string is
	 * not valid. This approach works well here, but not generally.
	 * 
	 * @param rawLong
	 *            A string which represents a long number.
	 * @return The long value (or <code>null</code> if the rawLong is empty or
	 *         <code>null</code>).
	 */
	private long parseLong(String rawLong) {
		try {
			return (rawLong != null && rawLong.length() > 0) ? Long
					.parseLong(rawLong) : 0;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Parses a string and returns a float. <b>NOTE<b>: Be careful and notice
	 * that this function will return a <code>null</code> if the given string is
	 * not valid.
	 * 
	 * @param rawFloat
	 *            A string which represents a float number.
	 * @return The float value (or <code>null</code> if the rawFloat is empty or
	 *         <code>null</code>).
	 */
	private Float parseFloat(String rawFloat) {
		return (rawFloat != null && rawFloat.length() > 0) ? Float
				.valueOf(rawFloat) : null;
	}

	/**
	 * Parses a string and returns a boolean. <b>NOTE<b>: Be careful and notice
	 * that this function will return a <code>null</code> if the given string is
	 * not valid.
	 * 
	 * @param rawBoolean
	 *            A string which represents a boolean.
	 * @return The float value (or <code>null</code> if the rawBoolean is empty
	 *         or <code>null</code>).
	 */
	private Boolean parseBoolean(String rawBoolean) {
		return (rawBoolean != null && rawBoolean.length() > 0) ? Boolean
				.valueOf(rawBoolean) : null;
	}
}