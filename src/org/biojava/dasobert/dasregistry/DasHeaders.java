package org.biojava.dasobert.dasregistry;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DasHeaders {
	
	private int httpStatus = 0;
	
	String dasVersion = "";
	private String headerVersion;
	private String cors;
	private String accessControl;
	private boolean hasCors = false;
	public String getDasVersion(){
		return dasVersion;
	}

	public boolean hasCors() {
		return hasCors;
	}

	public boolean hasAccessControl() {
		return hasAccessControl;
	}

	public String getAccessControl() {
		return accessControl;
	}

	private boolean hasAccessControl;
	private boolean validResponse=false;

	public DasHeaders(String urlString) {
		URL url = null;
		
		
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException ex) {
			System.out.println("Cannot open connection to URL: " + url);
		}

		if (connection instanceof HttpURLConnection) {
			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			try {
				httpStatus = httpConnection.getResponseCode();
				System.out.println("status code=" + httpStatus);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// do something with code .....
		} else {
			System.err.println("error - not a http request!");
		}

		if (httpStatus == 200) {
			validResponse=true;
			headerVersion = connection.getHeaderField("X-DAS-Version");
			cors = connection.getHeaderField("Access-Control-Allow-Origin");
			accessControl = connection
					.getHeaderField("Access-Control-Expose-Headers");
			if (headerVersion != null) {
				dasVersion = headerVersion;
			}
			if (cors != null) {
				if (cors.equals("*"))
					hasCors = true;
			}
			if (accessControl != null) {
				hasAccessControl = true;
			}
			System.out.println("cors=" + cors);
			System.out.println("accessControl=" + accessControl);
			System.out.println("das version="+dasVersion);
		}

	}
	
	public boolean validHttpStatus(){
		return validResponse;
	}

}
