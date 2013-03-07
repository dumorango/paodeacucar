package br.com.companhiadesistemas.googleappsserviceprovider.googleapis;

import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.ServiceAuthorizator;
import com.google.gdata.client.GoogleService;
public abstract class GoogleWebService {
	
	final String applicationName = "CiaDeSistemas-ITIMAdapter-v1.0";
	String domain = null;
	
	GoogleWebService(String domain){
		this.domain = domain;
		
	}
	public abstract GoogleService getGoogleService();
}
