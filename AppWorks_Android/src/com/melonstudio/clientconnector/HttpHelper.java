package com.melonstudio.clientconnector;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpHelper {

	/**
	 * do the interaction with the server using the http protocal
	 * 
	 * @param json
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public String send_request(String json, String url)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost post = new HttpPost(url);
		HttpEntity entity = new StringEntity(json, "UTF-8");
		post.setEntity(entity);

		HttpResponse response = httpClient.execute(post);
		String result = process_response(response);

		return result;
	}

	/**
	 * get the HttpResponse Object, analyse the body of the Response. The body
	 * of the response is a json string, and return the json string to the
	 * calling method
	 * 
	 * @param response
	 *            An HttpResponse Object
	 * @return the json String
	 * @throws ParseException
	 * @throws IOException
	 */
	private String process_response(HttpResponse response)
			throws ParseException, IOException {
		String result = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			/**
			 * Server runs properly
			 */
			result = EntityUtils.toString(response.getEntity());
		} else {
			/**
			 * There is something wrong with the server
			 */
			// TODO
			result = EntityUtils.toString(response.getEntity());
		}
		return result;
	}

	// public String uploadIconImg(byte[] bytes, String url) {
	// HttpClient httpClient = new DefaultHttpClient();
	//
	// HttpPost post = new HttpPost(url);
	// HttpParams params = new BasicHttpParams();
	// params.setParameter("file", new ByteArrayInputStream(bytes));
	// post.setParams(params);
	// post.addHeader("Content-Type", "image/png");
	// try {
	// HttpResponse response = httpClient.execute(post);
	// String result = process_response(response);
	// return result;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
}
