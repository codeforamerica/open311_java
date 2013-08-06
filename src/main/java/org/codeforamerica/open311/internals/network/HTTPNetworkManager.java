package org.codeforamerica.open311.internals.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
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
	private static final int TIMEOUT = 5000;

	public HTTPNetworkManager(Format format) {
		this.format = format;
		this.httpClient = getNewHttpClient();
	}

	@Override
	public String doGet(URL url) throws IOException {
		try {
			HttpGet httpGet = new HttpGet(url.toURI());
			httpGet.setHeader("Content-Type", format.getHTTPContentType());
			httpGet.setHeader("charset", CHARSET);
			HttpResponse response = httpClient.execute(httpGet);
			return EntityUtils.toString(response.getEntity(), CHARSET);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public String doPost(URL url, Map<String, String> parameters)
			throws IOException {
		try {
			HttpPost httpPost = new HttpPost(url.toURI());
			httpPost.setHeader("Content-Type", POST_CONTENT_TYPE);
			httpPost.setHeader("charset", CHARSET);
			httpPost.setEntity(generateHttpEntityFromParameters(parameters));
			HttpResponse response = httpClient.execute(httpPost);
			return EntityUtils.toString(response.getEntity(), CHARSET);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}

	/**
	 * Builds an {@link HttpEntity} with all the given parameters.
	 * 
	 * @param parameters
	 *            A list of parameters of a POST request.
	 * @return An {@link UrlEncodedFormEntity} with the given parameters.
	 * @throws UnsupportedEncodingException
	 *             if the default encoding isn't supported.
	 */
	private HttpEntity generateHttpEntityFromParameters(
			Map<String, String> parameters) throws UnsupportedEncodingException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		if (parameters != null) {
			for (Entry<String, String> parameterEntry : parameters.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(parameterEntry
						.getKey(), parameterEntry.getValue()));
			}
		}
		return new UrlEncodedFormEntity(nameValuePairs);
	}

	/**
	 * Builds a {@link HttpClient} which allows non trusted SSL certificates.
	 * 
	 * @return {@link HttpClient} which allows non trusted SSL certificates.
	 */
	private HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	/**
	 * A SSLSocketFactory which allows non trusted SSL certificates.
	 * 
	 * @author Santiago Munín <santimunin@gmail.com>
	 * 
	 */
	private class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}
}
