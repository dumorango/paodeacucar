package br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization;

import com.google.gdata.client.GoogleService;

public interface ServiceAuthorizator {
	public GoogleService authorize(GoogleService service) throws Exception;
}
