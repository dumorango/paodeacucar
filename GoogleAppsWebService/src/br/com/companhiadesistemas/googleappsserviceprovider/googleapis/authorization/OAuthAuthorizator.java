package br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.authn.oauth.*;

public class OAuthAuthorizator implements ServiceAuthorizator{
	
	
	
	GoogleOAuthParameters oauth = new GoogleOAuthParameters();
	private String OAuthConsumerKey;
	private String OAuthConsumerSecret;
	private String OAuthToken;
	private String OAuthTokenSecret;
	public GoogleService authorize(GoogleService service)
			throws Exception {
		oauth.setOAuthConsumerKey(OAuthConsumerKey);
		oauth.setOAuthConsumerSecret(OAuthConsumerSecret);
		oauth.setOAuthToken(OAuthToken);
		oauth.setOAuthTokenSecret(OAuthTokenSecret);
		service.setOAuthCredentials(oauth, new 
				OAuthHmacSha1Signer());
		return service;
	}
	public String getOAuthConsumerKey() {
		return OAuthConsumerKey;
	}
	public String getOAuthConsumerSecret() {
		return OAuthConsumerSecret;
	}
	public String getOAuthToken() {
		return OAuthToken;
	}
	public String getOAuthTokenSecret() {
		return OAuthTokenSecret;
	}
	public void setOAuthConsumerKey(String oAuthConsumerKey) {
		OAuthConsumerKey = oAuthConsumerKey;
	}
	public void setOAuthConsumerSecret(String oAuthConsumerSecret) {
		OAuthConsumerSecret = oAuthConsumerSecret;
	}
	public void setOAuthToken(String oAuthToken) {
		OAuthToken = oAuthToken;
	}
	public void setOAuthTokenSecret(String oAuthTokenSecret) {
		OAuthTokenSecret = oAuthTokenSecret;
	}
	
}


