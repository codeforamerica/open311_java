package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.codeforamerica.open311.facade.Format;

/**
 * Specifies the required operations of a NetworkManager
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public interface NetworkManager {

	public static final String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";
	public static final String CHARSET = "utf-8";

	/**
	 * Sends a GET HTTP request.
	 * 
	 * @param url
	 *            Target.
	 * @return Server response.
	 * @throws IOException
	 *             If there was any problem with the connection.
	 */
	public String doGet(URL url) throws IOException;

	/**
	 * Sends a POST HTTP request.
	 * 
	 * @param url
	 *            Target.
	 * @param parameters
	 *            Parameters of the POST operation.
	 * @return Server response.
	 * @throws IOException
	 *             If there was any problem with the connection.
	 */
	public String doPost(URL url, Map<String, String> parameters) throws IOException;

	/**
	 * Sets the desired format of the requests.
	 * 
	 * @param format
	 *            A serialization format (XML or JSON).
	 */
	public void setFormat(Format format);
}
