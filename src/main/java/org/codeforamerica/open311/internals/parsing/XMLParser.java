package org.codeforamerica.open311.internals.parsing;

import java.io.ByteArrayInputStream;
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
					" Cannot be created a DocumentBuilder which satisfies the configuration requested.");
		}
	}

	@Override
	public List<Service> parseServiceList(String rawData)
			throws DataParsingException {
		List<Service> result = new LinkedList<Service>();
		try {
			Document doc = dBuilder.parse(new InputSource(
					new ByteArrayInputStream(rawData
							.getBytes(DataParser.TEXT_FORMAT))));
			doc.getDocumentElement().normalize();

			NodeList serviceNodeList = doc.getElementsByTagName(SERVICE_TAG);
			for (int i = 0; i < serviceNodeList.getLength(); i++) {
				Node serviceNode = serviceNodeList.item(i);

				if (serviceNode.getNodeType() == Node.ELEMENT_NODE) {
					Element service = (Element) serviceNode;
					String code = getTagContent(service, SERVICE_CODE_TAG);
					String name = getTagContent(service, SERVICE_NAME_TAG);
					String description = getTagContent(service, DESCRIPTION_TAG);
					boolean metadata = Boolean.parseBoolean(getTagContent(
							service, METADATA_TAG));
					String group = getTagContent(service, SERVICE_GROUP_TAG);
					String[] keywords = getKeywords(getTagContent(service,
							KEYWORDS_TAG));
					Service.Type type = Service.Type
							.getFromString(getTagContent(service, TYPE_TAG));
					result.add(new Service(code, name, description, metadata,
							type, keywords, group));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return result;
	}

	@Override
	public ServiceDefinition parseServiceDefinition(String rawData)
			throws DataParsingException {
		try {
			Document doc = dBuilder.parse(new InputSource(
					new ByteArrayInputStream(rawData
							.getBytes(DataParser.TEXT_FORMAT))));
			doc.getDocumentElement().normalize();

			NodeList serviceDefinitionNodeList = doc
					.getElementsByTagName(SERVICE_DEFINITION_TAG);
			for (int i = 0; i < serviceDefinitionNodeList.getLength(); i++) {
				Node serviceDefinitionNode = serviceDefinitionNodeList.item(i);
				if (serviceDefinitionNode.getNodeType() == Node.ELEMENT_NODE) {
					Element serviceDefinitionElement = (Element) serviceDefinitionNode;
					String serviceCode = getTagContent(
							serviceDefinitionElement, SERVICE_CODE_TAG);
					NodeList attributesNodeList = serviceDefinitionElement
							.getElementsByTagName(ATTRIBUTE_TAG);
					List<Attribute> attributes = new LinkedList<Attribute>();
					for (int j = 0; j < attributesNodeList.getLength(); j++) {
						Node attributeNode = attributesNodeList.item(j);
						if (serviceDefinitionNode.getNodeType() == Node.ELEMENT_NODE) {
							Element attributeElement = (Element) attributeNode;
							boolean variable = Boolean
									.parseBoolean(getTagContent(
											attributeElement, VARIABLE_TAG));
							String code = getTagContent(attributeElement,
									CODE_TAG);
							Datatype datatype = Datatype
									.getFromString(getTagContent(
											attributeElement, DATATYPE_TAG));
							boolean required = Boolean
									.parseBoolean(getTagContent(
											attributeElement, REQUIRED_TAG));
							String datatypeDescription = getTagContent(
									attributeElement, DATATYPE_DESCRIPTION_TAG);
							int order = Integer.parseInt(getTagContent(
									attributeElement, ORDER_TAG));
							String description = getTagContent(
									attributeElement, DESCRIPTION_TAG);
							Map<String, String> values = new HashMap<String, String>();
							NodeList valuesNodeList = attributeElement
									.getElementsByTagName(VALUE_TAG);
							for (int k = 0; k < valuesNodeList.getLength(); k++) {
								Node valueNode = valuesNodeList.item(k);
								if (valueNode.getNodeType() == Node.ELEMENT_NODE) {
									Element valueElement = (Element) valueNode;
									String key = getTagContent(valueElement,
											KEY_TAG);
									String name = getTagContent(valueElement,
											NAME_TAG);
									values.put(key, name);
								}
							}
							attributes.add(new Attribute(variable, code,
									datatype, required, datatypeDescription,
									order, description, values));
						}

					}
					return new ServiceDefinition(serviceCode, attributes);
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return null;
	}

	@Override
	public List<ServiceRequestIdResponse> parseServiceRequestIdFromAToken(
			String rawData) throws DataParsingException {
		List<ServiceRequestIdResponse> result = new LinkedList<ServiceRequestIdResponse>();
		Document doc;
		try {
			doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(
					rawData.getBytes(DataParser.TEXT_FORMAT))));
			doc.getDocumentElement().normalize();
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					Element serviceRequestIdElement = (Element) serviceRequestIdNode;
					String token = getTagContent(serviceRequestIdElement,
							TOKEN_TAG);
					String serviceRequestId = getTagContent(
							serviceRequestIdElement, SERVICE_REQUEST_ID_TAG);
					result.add(new ServiceRequestIdResponse(serviceRequestId,
							token));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return result;
	}

	@Override
	public List<ServiceRequest> parseServiceRequests(String rawData)
			throws DataParsingException {
		List<ServiceRequest> result = new LinkedList<ServiceRequest>();
		Document doc;
		try {
			doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(
					rawData.getBytes(DataParser.TEXT_FORMAT))));
			doc.getDocumentElement().normalize();
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					Element serviceRequestIdElement = (Element) serviceRequestIdNode;
					String serviceRequestId = getTagContent(
							serviceRequestIdElement, SERVICE_REQUEST_ID_TAG);
					Status status = Status.getFromString(getTagContent(
							serviceRequestIdElement, STATUS_TAG));
					String statusNotes = getTagContent(serviceRequestIdElement,
							STATUS_NOTES_TAG);
					String serviceName = getTagContent(serviceRequestIdElement,
							SERVICE_NAME_TAG);
					String serviceCode = getTagContent(serviceRequestIdElement,
							SERVICE_CODE_TAG);
					String description = getTagContent(serviceRequestIdElement,
							DESCRIPTION_TAG);
					String agencyResponsible = getTagContent(
							serviceRequestIdElement, AGENCY_RESPONSIBLE_TAG);
					String serviceNotice = getTagContent(
							serviceRequestIdElement, SERVICE_NOTICE_TAG);
					DateParsingUtils dateParser = DateParsingUtils
							.getInstance();
					Date requestedDatetime = dateParser
							.parseDate((getTagContent(serviceRequestIdElement,
									REQUESTED_DATETIME_TAG)));
					Date updatedDatetime = dateParser.parseDate((getTagContent(
							serviceRequestIdElement, UPDATED_DATETIME_TAG)));
					Date expectedDatetime = dateParser
							.parseDate((getTagContent(serviceRequestIdElement,
									EXPECTED_DATETIME_TAG)));
					String address = getTagContent(serviceRequestIdElement,
							ADDRESS_TAG);
					String addressId = getTagContent(serviceRequestIdElement,
							ADDRESS_ID_TAG);
					int zipCode = Integer.parseInt(getTagContent(
							serviceRequestIdElement, ZIPCODE_TAG));
					float latitude = Float.parseFloat(getTagContent(
							serviceRequestIdElement, LATITUDE_TAG));
					float longitude = Float.parseFloat(getTagContent(
							serviceRequestIdElement, LONGITUDE_TAG));
					URL mediaUrl = new URL(getTagContent(
							serviceRequestIdElement, MEDIA_URL_TAG));
					result.add(new ServiceRequest(serviceRequestId, status,
							statusNotes, serviceName, serviceCode, description,
							agencyResponsible, serviceNotice,
							requestedDatetime, updatedDatetime,
							expectedDatetime, address, addressId, zipCode,
							latitude, longitude, mediaUrl));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return result;
	}

	@Override
	public List<PostServiceRequestResponse> parsePostServiceRequestResponse(
			String rawData) throws DataParsingException {
		List<PostServiceRequestResponse> result = new LinkedList<PostServiceRequestResponse>();
		Document doc;
		try {
			doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(
					rawData.getBytes(DataParser.TEXT_FORMAT))));
			doc.getDocumentElement().normalize();
			NodeList serviceRequestsIdList = doc
					.getElementsByTagName(SERVICE_REQUEST_TAG);
			for (int i = 0; i < serviceRequestsIdList.getLength(); i++) {
				Node serviceRequestIdNode = serviceRequestsIdList.item(i);
				if (serviceRequestIdNode.getNodeType() == Node.ELEMENT_NODE) {
					Element serviceRequestIdElement = (Element) serviceRequestIdNode;
					String token = getTagContent(serviceRequestIdElement,
							TOKEN_TAG);
					String serviceRequestId = getTagContent(
							serviceRequestIdElement, SERVICE_REQUEST_ID_TAG);
					String serviceNotice = getTagContent(
							serviceRequestIdElement, SERVICE_NOTICE_TAG);
					String accountId = getTagContent(serviceRequestIdElement,
							ACCOUNT_ID_TAG);
					result.add(new PostServiceRequestResponse(serviceRequestId,
							token, serviceNotice, accountId));
				}
			}
		} catch (Exception e) {
			throw new DataParsingException();
		}
		return result;
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