package com.example.qzero.CommonFiles.RequestResponse;



import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonParser {

	static InputStream iStream = null;
	static JSONArray jarray = null;
	static String json = "";

	public JsonParser() {
	}

	public String getJSONFromUrl(String url, int timeout) {
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setRequestProperty("appKey",Const.APP_KEY);
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.setConnectTimeout(timeout);
			c.setReadTimeout(timeout);
			c.connect();
			int status = c.getResponseCode();

			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(
						c.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
			}

		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();

		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ex) {

			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.disconnect();
				} catch (Exception ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}
		return null;
	}
	public String getJSONFromUrl(String url, int timeout, String userID) {
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setRequestProperty("appKey",Const.APP_KEY);
			c.setRequestProperty("userId",userID);

			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.setConnectTimeout(timeout);
			c.setReadTimeout(timeout);
			c.connect();
			int status = c.getResponseCode();

			switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(
							c.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					return sb.toString();
			}

		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();

		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ex) {

			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.disconnect();
				} catch (Exception ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							null, ex);
				}
			}
		}
		return null;
	}
	public String executePost(String targetURL, String urlParameters, int timeout) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection

			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setRequestProperty("appKey", Const.APP_KEY);

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();

		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ex) {

			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	public String executePost(String targetURL, String urlParameters,String userID, int timeout) {
		URL url;
		HttpURLConnection connection = null;
		try {
			// Create connection

			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setRequestProperty("appKey", Const.APP_KEY);
			connection.setRequestProperty("userId",userID);

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(timeout);
			connection.setReadTimeout(timeout);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();

		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();

		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ex) {

			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}

}
