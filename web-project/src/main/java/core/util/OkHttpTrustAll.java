package core.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

public class OkHttpTrustAll {

	/*
	 * This is very bad practice and should NOT be used in production.
	 */
	private static final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[] {};
		}
	} };
	private static final SSLContext trustAllSslContext;
	static {
		try {
			trustAllSslContext = SSLContext.getInstance("SSL");
			trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException(e);
		}
	}
	private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

	/*
	 * This should not be used in production unless you really don't care about the
	 * security. Use at your own risk.
	 */
	public static OkHttpClient trustAllSslClient(OkHttpClient client) {
		Builder builder = client.newBuilder();
		builder.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager) trustAllCerts[0]);
		builder.hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		return builder.build();
	}
}
