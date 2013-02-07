package br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization;

import java.util.HashMap;

public interface OAuthTokenServiceInterface {
	public HashMap<String,String>  getAuthorizingURLAndUnauthorizedSecret(String consumerKey,String consumerSecret,String callbackURL)throws Exception;
	public HashMap<String,String> getAccessTokenAndSecret(String consumerKey,String consumerSecret,String token,String verifyCode,String unauthorizedSecret) throws Exception;
}
