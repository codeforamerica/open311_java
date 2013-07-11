package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codeforamerica.open311.facade.Format;

/**
 * Implementation using the <a href="http://hc.apache.org/">Apache
 * HttpComponents</> library.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class HTTPNetworkManager implements NetworkManager {

	private HttpClient httpClient;
	private Format format;

	public HTTPNetworkManager(Format format) {
		this.format = format;
		httpClient = new DefaultHttpClient();
	}

	@Override
	public String doGet(URL url) throws IOException {
		try {
			HttpGet httpGet = new HttpGet(url.toURI());
			httpGet.setHeader("Content-Type", format.getHTTPContentType());
			httpGet.setHeader("charset", CHARSET);
			return httpClient.execute(httpGet, new BasicResponseHandler());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public String doPost(URL url, String body) throws IOException {
		try {
			HttpPost httpPost = new HttpPost(url.toURI());
			httpPost.setHeader("Content-Type", POST_CONTENT_TYPE);
			httpPost.setHeader("charset", CHARSET);
			httpPost.setEntity(new StringEntity(body));
			return httpClient.execute(httpPost, new BasicResponseHandler());
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}
}
