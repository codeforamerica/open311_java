package org.codeforamerica.open311.facade.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;

/**
 * This class maintains a map between {@link APIWrapper} and {@link Service}
 * instances. This is useful to allow a service to request its service
 * definition without knowing the {@link APIWrapper} which created it in
 * advance.
 * 
 * This class is only called by instances of classes from its package.
 * 
 * Singleton class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class RelationshipManager {
	/**
	 * Unique instance of the class.
	 */
	private static RelationshipManager instance = new RelationshipManager();
	/**
	 * Maintain a list of pairs (List<{@link Service}>, {@link APIWrapper})
	 * instances.
	 */
	private Map<List<Service>, APIWrapper> serviceWrapperRelationships = new HashMap<List<Service>, APIWrapper>();

	private RelationshipManager() {

	}

	/**
	 * 
	 * @return Unique instance of the class.
	 */
	public static RelationshipManager getInstance() {
		return instance;
	}

	/**
	 * Adds a relationship between a List<{@link Service}> and the
	 * {@link APIWrapper} which generated it.
	 * 
	 * @param services
	 *            list of services.
	 * @param originator
	 *            {@link Service} generator.
	 */
	public void addServiceWrapperRelationship(List<Service> services,
			APIWrapper originator) {
		if (services != null && originator != null) {
			serviceWrapperRelationships.put(services, originator);
		}
	}

	/**
	 * Gets a service definition given a service. This operation could involve a
	 * lot of time because it calls the
	 * {@link APIWrapper#getServiceDefinition(String)} method.
	 * 
	 * @param service
	 *            Instance of a service.
	 * @return Its server definition.
	 * @throws APIWrapperException
	 *             If there is not an {@link APIWrapper} which created it.
	 */
	/* package */ServiceDefinition getServiceDefinitionFromService(
			Service service) throws APIWrapperException {
		if (service == null) {
			throw new NullPointerException();
		}
		APIWrapper wrapper = null;
		for (Entry<List<Service>, APIWrapper> entry : serviceWrapperRelationships
				.entrySet()) {
			if (entry.getKey().contains(service)) {
				wrapper = entry.getValue();
				break;
			}
		}
		if (wrapper == null) {
			throw new APIWrapperException("Couldn't obtain a related wrapper",
					Error.NOT_CREATED_BY_A_WRAPPER, null);
		}
		return wrapper.getServiceDefinition(service.getServiceCode());
	}
}
