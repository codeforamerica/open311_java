package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URL;

/**
 * Specifies the required operations of a NetworkManager
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public interface NetworkManager {
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
	 * @param body
	 *            Body of the POST operation.
	 * @return Server response.
	 * @throws IOException
	 *             If there was any problem with the connection.
	 */
	public String doPost(URL url, String body) throws IOException;
}
