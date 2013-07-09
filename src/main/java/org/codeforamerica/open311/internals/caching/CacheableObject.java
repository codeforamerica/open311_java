package org.codeforamerica.open311.internals.caching;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

/**
 * A pair of serializable, expiration date.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class CacheableObject implements Serializable {

	private static final long serialVersionUID = -108175395476829305L;
	private Serializable object;
	private Date expirationTime;

	public CacheableObject(Serializable object, int hoursToLive) {
		super();
		this.object = object;
		this.expirationTime = new DateTime().plusHours(hoursToLive).toDate();
	}

	public CacheableObject(String base64object) {
		try {
			byte[] rawDataInBytes = Base64.decodeBase64(base64object);
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(rawDataInBytes));
			Object o = ois.readObject();
			ois.close();
			CacheableObject thisObject = (CacheableObject) o;
			this.object = thisObject.object;
			this.expirationTime = thisObject.expirationTime;
		} catch (Exception e) {
		}
	}

	public Object getObject() {
		if (expirationTime != null) {
			return expirationTime.after(new Date()) ? object : null;
		}
		return null;
	}

	/**
	 * Return a serialized version of the object.
	 * 
	 * @return A base64 encoded string version of the object.
	 */
	public String serialize() {
		return serialize(this);
	}

	public static String serialize(Serializable object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			oos.close();
			return new String(Base64.encodeBase64(baos.toByteArray()));
		} catch (IOException e) {
			return null;
		}
	}
}
