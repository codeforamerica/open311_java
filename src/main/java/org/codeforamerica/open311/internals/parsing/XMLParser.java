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

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.Attribute.Datatype;
import org.codeforamerica.open311.facade.data.PostServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
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
public class XMLParser implements DataParser {

	private DocumentBuilder dBuilder;

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
			throw new DataParsingException();
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
		boolean metadata = Boolean.parseBoolean(getTagContent(serviceElement,
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
			throw new DataParsingException();
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
		List<Attribute> attributes = new LinkedList<Attribute>();
		for (int j = 0; j < attributesNodeList.getLength(); j++) {
			Node attributeNode = attributesNodeList.item(j);
			if (attributeNode.getNodeType() == Node.ELEMENT_NODE) {
				Element attributeElement = (Element) attributeNode;
				boolean variable = Boolean.parseBoolean(getTagContent(
						attributeElement, VARIABLE_TAG));
				String code = getTagContent(attributeElement, CODE_TAG);
				Datatype datatype = Datatype.getFromString(getTagContent(
						attributeElement, DATATYPE_TAG));
				boolean required = Boolean.parseBoolean(getTagContent(
						attributeElement, REQUIRED_TAG));
				String datatypeDescription = getTagContent(attributeElement,
						DATATYPE_DESCRIPTION_TAG);
				int order = Integer.parseInt(getTagContent(attributeElement,
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
				attributes.add(new Attribute(variable, code, datatype,
						required, datatypeDescription, order, description,
						values));
			}

		}
		return new ServiceDefinition(serviceCode, attributes);
	}

	@Override
	public List<ServiceRequestIdResponse> parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException {
		List<ServiceRequestIdResponse> result = new LinkedList<ServiceRequestIdResponse>();
		try {
			Document doc = getDocument(rawData);
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					result.add(getServiceRequestIdResponse((Element) serviceRequestIdNode));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return result;
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
			throw new DataParsingException();
		}
		return result;
	}

	/**
	 * Builds a {@link ServiceRequest} object from an {@link Element} of the
	 * DOM.
	 * 
	 * @param serviceRequestIdElement
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
		DateParsingUtils dateParser = DateParsingUtils.getInstance();
		Date requestedDatetime = dateParser.parseDate((getTagContent(
				serviceRequestElement, REQUESTED_DATETIME_TAG)));
		Date updatedDatetime = dateParser.parseDate((getTagContent(
				serviceRequestElement, UPDATED_DATETIME_TAG)));
		Date expectedDatetime = dateParser.parseDate((getTagContent(
				serviceRequestElement, EXPECTED_DATETIME_TAG)));
		String address = getTagContent(serviceRequestElement, ADDRESS_TAG);
		String addressId = getTagContent(serviceRequestElement, ADDRESS_ID_TAG);
		int zipCode = Integer.parseInt(getTagContent(serviceRequestElement,
				ZIPCODE_TAG));
		float latitude = Float.parseFloat(getTagContent(serviceRequestElement,
				LATITUDE_TAG));
		float longitude = Float.parseFloat(getTagContent(serviceRequestElement,
				LONGITUDE_TAG));
		URL mediaUrl = new URL(getTagContent(serviceRequestElement,
				MEDIA_URL_TAG).trim());
		return new ServiceRequest(serviceRequestId, status, statusNotes,
				serviceName, serviceCode, description, agencyResponsible,
				serviceNotice, requestedDatetime, updatedDatetime,
				expectedDatetime, address, addressId, zipCode, latitude,
				longitude, mediaUrl);
	}

	@Override
	public List<PostServiceRequestResponse> parsePostServiceRequestResponse(
			String rawData) throws DataParsingException {
		List<PostServiceRequestResponse> result = new LinkedList<PostServiceRequestResponse>();
		try {
			Document doc = getDocument(rawData);
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					result.add(getPostServiceRequestResponse((Element) serviceRequestIdNode));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return result;
	}

	/**
	 * Builds a {@link PostServiceRequestResponse} object from an
	 * {@link Element} of the DOM.
	 * 
	 * @param postServiceRequestResponseElement
	 *            A valid element node of a post service request response..
	 * @return An object wrapping the information contained in the given
	 *         element.
	 */
	private PostServiceRequestResponse getPostServiceRequestResponse(
			Element postServiceRequestResponseElement) {
		String token = getTagContent(postServiceRequestResponseElement,
				TOKEN_TAG);
		String serviceRequestId = getTagContent(
				postServiceRequestResponseElement, SERVICE_REQUEST_ID_TAG);
		String serviceNotice = getTagContent(postServiceRequestResponseElement,
				SERVICE_NOTICE_TAG);
		String accountId = getTagContent(postServiceRequestResponseElement,
				ACCOUNT_ID_TAG);
		return new PostServiceRequestResponse(serviceRequestId, token,
				serviceNotice, accountId);
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
	 * Parses a comma separated list of keywords.
	 * 
	 * @param rawKeywords
	 * @return An array of strings (keywords).
	 */
	private String[] getKeywords(String rawKeywords) {
		String[] result = rawKeywords.split(KEYWORDS_SEPARATOR);
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}
}