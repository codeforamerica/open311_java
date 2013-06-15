package org.codeforamerica.open311.internals.parsing;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.Attribute.Datatype;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Parses XML files using DOM.
 * 
 * @author Santiago Munín González <santimunin@gmail.com>
 * 
 */
public class XMLParser implements DataParser {

	private static final String SERVICE_TAG = "service";
	private static final String SERVICE_CODE_TAG = "service_code";
	private static final String SERVICE_NAME_TAG = "service_name";
	private static final String DESCRIPTION_TAG = "description";
	private static final String METADATA_TAG = "metadata";
	private static final String TYPE_TAG = "type";
	private static final String KEYWORDS_TAG = "keywords";
	private static final String KEYWORDS_SEPARATOR = ",";
	private static final String SERVICE_GROUP_TAG = "group";
	private static final String SERVICE_DEFINITION_TAG = "service_definition";
	private static final String ATTRIBUTE_TAG = "attribute";
	private static final String VARIABLE_TAG = "variable";
	private static final String CODE_TAG = "code";
	private static final String DATATYPE_TAG = "datatype";
	private static final String REQUIRED_TAG = "required";
	private static final String DATATYPE_DESCRIPTION_TAG = "datatype_description";
	private static final String ORDER_TAG = "order";
	private static final String VALUE_TAG = "value";
	private static final String KEY_TAG = "key";
	private static final String NAME_TAG = "name";

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

	/**
	 * Parses a list of services (XML/UTF-8 format).
	 * 
	 * @returns List of service objects.
	 */
	@Override
	public List<Service> parseServiceList(String rawData) {
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
			return null;
			// TODO parsing exception
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
	 * @return content of the tag inside the element.
	 */
	private String getTagContent(Element element, String tag) {
		return element.getElementsByTagName(tag).item(0).getTextContent();
	}

	/**
	 * 
	 * @param rawKeywords
	 * @return
	 */
	private String[] getKeywords(String rawKeywords) {
		String[] result = rawKeywords.split(KEYWORDS_SEPARATOR);
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}

	@Override
	public ServiceDefinition parseServiceDefinition(String rawData) {
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
			// TODO
		}
		return null;
	}

}