package org.codeforamerica.open311.internals.network;

import org.codeforamerica.open311.facade.Format;

/**
 * Returns instances of the {@link MockNetworkManager} class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class MockNetworkManagerFactory implements NetworkManagerFactory {

	@Override
	public NetworkManager createNetworkManager(Format format) {
		return new MockNetworkManager();
	}

}
