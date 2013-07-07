package org.codeforamerica.open311.facade.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codeforamerica.open311.facade.EndpointType;
import org.codeforamerica.open311.facade.Format;

/**
 * Wraps the information relative to a single endpoint.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 */
public class Endpoint implements Serializable {
	private static final long serialVersionUID = -5512681512770606630L;
	private String specificationUrl;
	private String url;
	private Date changeset;
	private EndpointType type;
	private List<Format> formats;

	public Endpoint(String specificationUrl, String url, Date changeset,
			EndpointType type, List<Format> formats) {
		super();
		this.specificationUrl = specificationUrl;
		this.url = url;
		this.changeset = changeset;
		this.type = type;
		this.formats = formats;
	}

	public String getSpecificationUrl() {
		return specificationUrl;
	}

	public String getUrl() {
		return url;
	}

	public Date getChangeset() {
		return changeset;
	}

	public EndpointType getType() {
		return type;
	}

	/**
	 * Returns the more suitable format. Tries JSON before XML.
	 * 
	 * @return A format allowed by the endpoint.
	 */
	public Format getBestFormat() {
		if (formats.contains(Format.JSON)) {
			return Format.JSON;
		}
		if (formats.contains(Format.XML)) {
			return Format.XML;
		}
		return null;

	}
}
