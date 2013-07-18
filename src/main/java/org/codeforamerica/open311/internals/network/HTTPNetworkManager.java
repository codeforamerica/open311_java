package org.codeforamerica.open311.internals.network;

import java.io.IOException;
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

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
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
		this.httpClient = getNewHttpClient();
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
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

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
