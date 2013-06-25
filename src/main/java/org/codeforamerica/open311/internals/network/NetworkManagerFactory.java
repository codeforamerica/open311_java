package org.codeforamerica.open311.internals.network;

import org.codeforamerica.open311.facade.Format;

/**
 * {@link NetworkManager} builder. Factory method pattern.
 * 
 * <b>NOTE</b>: You can implement this interface and create your own factory in
 * order to use your custom {@link NetworkManager}.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public interface NetworkManagerFactory {
	/**
	 * Builds an instance of the {@link NetworkManager} interface.
	 * 
	 * @param format
	 *            Content-type of the requests.
	 * @return An instance of a NetworkManager or <code>null</code> if the given
	 *         format is not supported.
	 */
	public NetworkManager createNetworkManager(Format format);
}
