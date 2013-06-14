package org.codeforamerica.open311.internals.parsing;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codeforamerica.open311.facade.data.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses XML files using DOM.
 * 
 * @author Santiago Munín González <santimunin@gmail.com>
 * 
 */
public class XMLParser extends DefaultHandler implements DataParser {

	private static final String SERVICE_TAG = "service";
	private static final String SERVICE_CODE_TAG = "service_code";
	private static final String SERVICE_NAME_TAG = "service_name";
	private static final String SERVICE_DESCRIPTION_TAG = "description";
	private static final String SERVICE_METADATA_TAG = "metadata";
	private static final String SERVICE_TYPE_TAG = "type";
	private static final String SERVICE_KEYWORDS_TAG = "keywords";
	private static final String SERVICE_KEYWORDS_SEPARATOR = ",";
	private static final String SERVICE_GROUP_TAG = "group";

	private DocumentBuilder dBuilder;

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
					String description = getTagContent(service,
							SERVICE_DESCRIPTION_TAG);
					boolean metadata = Boolean.parseBoolean(getTagContent(
							service, SERVICE_METADATA_TAG));
					String group = getTagContent(service, SERVICE_GROUP_TAG);
					String[] keywords = getKeywords(getTagContent(service,
							SERVICE_KEYWORDS_TAG));
					Service.Type type = Service.Type
							.getFromString(getTagContent(service,
									SERVICE_TYPE_TAG));
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
		String[] result = rawKeywords.split(SERVICE_KEYWORDS_SEPARATOR);
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}
}