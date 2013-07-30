package org.codeforamerica.open311.facade.data.operations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.internals.parsing.DataParser;

/**
 * Contains all the necessary data to add a new service request to the server.
 * It allows to add some optional arguments through the method chaining
 * (setters).
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class POSTServiceRequestData {
	/**
	 * Required email syntax.
	 */
	private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
			"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);
	private Map<String, String> parameters;
	private List<Attribute> attributes;

	/**
	 * Basic constructor. It isn't public because this operation needs at least
	 * one of the location parameters in addition to the service code.
	 * 
	 * @param serviceCode
	 *            Code of the service related to the request.
	 * @param attributes
	 *            List of attributes, <code>null</code> value is allowed.
	 */
	private POSTServiceRequestData(String serviceCode,
			List<Attribute> attributes) {
		this.parameters = new HashMap<String, String>();
		this.attributes = new LinkedList<Attribute>();
		if (serviceCode != null && serviceCode.length() > 0) {
			parameters.put(DataParser.SERVICE_CODE_TAG, serviceCode);
			if (attributes != null) {
				this.attributes = attributes;
			}
		} else {
			throw new NoSuchFieldError(
					"One or more of the arguments are not valid.");
		}
	}

	/**
	 * Builds a POST service request data object from a lat & long location
	 * value.
	 * 
	 * @param serviceCode
	 *            Code of the service related to the request.
	 * @param latitude
	 *            Latitude using the (WGS84) projection.
	 * @param longitude
	 *            Longitude using the (WGS84) projection.
	 * @param attributes
	 *            List of attributes.
	 */
	public POSTServiceRequestData(String serviceCode, float latitude,
			float longitude, List<Attribute> attributes) {
		this(serviceCode, attributes);
		this.setLatLong(latitude, longitude);
	}

	/**
	 * Builds a POST service request data object from an address location value.
	 * 
	 * @param serviceCode
	 *            Code of the service related to the request.
	 * @param address
	 *            Human-readable address.
	 * @param attributes
	 *            List of attributes.
	 */
	public POSTServiceRequestData(String serviceCode, String address,
			List<Attribute> attributes) {
		this(serviceCode, attributes);
		if (address != null && address.length() > 0) {
			this.setAddress(address);
		} else {
			throw new NoSuchFieldError(
					"One or more of the arguments are not valid.");
		}
	}

	/**
	 * Builds a POST service request data object from an address_id location
	 * value.
	 * 
	 * @param serviceCode
	 *            Code of the service related to the request.
	 * @param addressId
	 *            The internal address ID used by a jurisdiction's master
	 *            address repository or other addressing system.
	 * @param attributes
	 *            List of attributes.
	 */
	public POSTServiceRequestData(String serviceCode, long addressId,
			List<Attribute> attributes) {
		this(serviceCode, attributes);
		this.setAddressId(addressId);
	}

	/**
	 * Sets a (latitude, longitude) coordinates.
	 * 
	 * @param latitude
	 *            Latitude using the (WGS84) projection.
	 * @param longitude
	 *            Longitude using the (WGS84) projection.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setLatLong(float latitude, float longitude) {
		parameters.put(DataParser.LATITUDE_TAG, String.valueOf(latitude));
		parameters.put(DataParser.LONGITUDE_TAG, String.valueOf(longitude));
		return this;
	}

	/**
	 * Sets an address parameter.
	 * 
	 * @param address
	 *            Human-readable address.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setAddress(String address) {
		tryToAddString(DataParser.ADDRESS_TAG, address);
		return this;
	}

	/**
	 * Sets an address_id parameter.
	 * 
	 * @param addressId
	 *            The internal address ID used by a jurisdiction's master
	 *            address repository or other addressing system.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setAddressId(long addressId) {
		tryToAddString(DataParser.ADDRESS_ID_TAG, String.valueOf(addressId));
		return this;
	}

	/**
	 * Sets an email.
	 * 
	 * @param email
	 *            The email address of the person submitting the request. It
	 *            requires a correct email syntax.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setEmail(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		if (matcher.find()) {
			tryToAddString(DataParser.EMAIL_TAG, email);
		}
		return this;
	}

	/**
	 * Sets a device_id.
	 * 
	 * @param deviceId
	 *            The unique device ID of the device submitting the request.
	 *            This is usually only used for mobile devices. Android devices
	 *            can use <code>TelephonyManager.getDeviceId()</code> and
	 *            iPhones can use
	 *            <code>[UIDevice currentDevice].uniqueIdentifier</code>.
	 * 
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setDeviceId(String deviceId) {
		tryToAddString(DataParser.DEVICE_ID_TAG, deviceId);
		return this;
	}

	/**
	 * Sets an account_id.
	 * 
	 * @param accountId
	 *            The unique ID for the user account of the person submitting
	 *            the request.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setAccountId(long accountId) {
		tryToAddString(DataParser.ACCOUNT_ID_TAG, String.valueOf(accountId));
		return this;
	}

	/**
	 * Sets the first_name.
	 * 
	 * @param firstName
	 *            The given name of the person submitting the request.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setFirstName(String firstName) {
		tryToAddString(DataParser.FIRST_NAME_TAG, firstName);
		return this;
	}

	/**
	 * Sets the last_name.
	 * 
	 * @param lastName
	 *            The family name of the person submitting the request .
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setLastName(String lastName) {
		tryToAddString(DataParser.LAST_NAME_TAG, lastName);
		return this;
	}

	/**
	 * Sets the phone.
	 * 
	 * @param phone
	 *            The phone number of the person submitting the request.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setPhone(String phone) {
		tryToAddString(DataParser.PHONE_TAG, phone);
		return this;
	}

	/**
	 * Sets a description.
	 * 
	 * @param description
	 *            A full description of the request or report being submitted.
	 *            This may contain line breaks, but not HTML or code. Otherwise,
	 *            this is free form text limited to 4,000 characters.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setDescription(String description) {
		tryToAddString(DataParser.DESCRIPTION_TAG, description);
		return this;
	}

	/**
	 * Sets a media URL.
	 * 
	 * @param mediaUrl
	 *            A URL to media associated with the request, eg an image.
	 * @return Same instance with the new parameter added.
	 */
	public POSTServiceRequestData setMediaUrl(String mediaUrl) {
		tryToAddString(DataParser.MEDIA_URL_TAG, mediaUrl);
		return this;
	}

	/**
	 * Tries to add a pair (key, value) to the parameter list. Key and value has
	 * to be valid parameters (not null and not empty).
	 * 
	 * @param key
	 *            Key of the pair.
	 * @param value
	 *            Value of the pair.
	 */
	private void tryToAddString(String key, String value) {
		if (key != null && key.length() > 0 && value != null
				&& value.length() > 0) {
			parameters.put(key, value);
		}
	}

	/**
	 * Builds a map containing all the arguments.
	 * 
	 * @return List of pairs (key, value) with the required arguments.
	 */
	public Map<String, String> getBodyRequestParameters() {
		Map<String, String> result = new HashMap<String, String>();
		result.putAll(parameters);
		return result;
	}

	/**
	 * Obtains the attributes related to the object.
	 * 
	 * @return A copy of the inner attribute list.
	 */
	public List<Attribute> getAttributes() {
		List<Attribute> result = new LinkedList<Attribute>();
		result.addAll(attributes);
		return result;
	}

}
