package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codeforamerica.open311.facade.Format;

/**
 * Implementation using the <a href="http://hc.apache.org/">Apache
 * HttpComponents</> library.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class HTTPNetworkManager implements NetworkManager {

	private CloseableHttpClient httpClient;
	private Format format;

	public HTTPNetworkManager(Format format) {
		HttpClientBuilder builder = HttpClientBuilder.create();
		httpClient = builder.build();
	}

	@Override
	public String doGet(URL url) throws IOException {
		try {
			HttpGet httpGet = new HttpGet(url.toURI());
			httpGet.setHeader("Content-Type", format.getHTTPContentType());
			httpGet.setHeader("charset", CHARSET);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute(httpGet, responseHandler);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		} finally {
			httpClient.close();
		}
	}

	@Override
	public String doPost(URL url, String body) throws IOException {
		try {
			HttpPost httpPost = new HttpPost(url.toURI());
			httpPost.setHeader("Content-Type", POST_CONTENT_TYPE);
			httpPost.setHeader("charset", CHARSET);
			StringEntity entity = new StringEntity(body);
			httpPost.setEntity(entity);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return httpClient.execute(httpPost, responseHandler);
		} catch (URISyntaxException e) {
			throw new IOException(e);
		} finally {
			httpClient.close();
		}
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}
}
