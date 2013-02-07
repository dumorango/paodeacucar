package br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization;

import java.util.HashMap;

import com.google.gdata.client.authn.oauth.*;

public class OAuthTokenService implements OAuthTokenServiceInterface{
	
	GoogleOAuthParameters oauth;
	GoogleOAuthHelper oauthHelper;
	
	public OAuthTokenService(){
		 oauthHelper = new GoogleOAuthHelper(new OAuthHmacSha1Signer());
	}
	private GoogleOAuthParameters getParameters(String consumerKey,String consumerSecret){
		GoogleOAuthParameters oauth = new GoogleOAuthParameters();
		oauth.setOAuthConsumerKey(consumerKey);
		oauth.setOAuthConsumerSecret(consumerSecret);
		oauth.setScope("https://apps-apis.google.com/a/feeds/");
		return oauth;
	}
	
	public HashMap<String,String> getAuthorizingURLAndUnauthorizedSecret(String consumerKey,String consumerSecret,String callbackURL) throws Exception{
			HashMap<String,String> authorizingURLAndUnauthorizedSecret = new HashMap<String,String>();
			oauth = getParameters(consumerKey, consumerSecret);
			oauth.setOAuthCallback(callbackURL);
			oauthHelper.getUnauthorizedRequestToken(oauth);
			authorizingURLAndUnauthorizedSecret.put("authorizingURL", oauthHelper.createUserAuthorizationUrl(oauth));
			authorizingURLAndUnauthorizedSecret.put("unauthorizedSecret", oauth.getOAuthTokenSecret());
			System.out.println("authorizingURLAndUnauthorizedSecret: "+authorizingURLAndUnauthorizedSecret);
			return authorizingURLAndUnauthorizedSecret;
		
	}
	
	public HashMap<String,String> getAccessTokenAndSecret(String consumerKey,String consumerSecret,String token,String verifyCode,String unauthorizedSecret) throws Exception{
			oauth = getParameters(consumerSecret, consumerSecret);
			System.out.println("Código: "+verifyCode);
			System.out.println("unauthorizedSecret: "+unauthorizedSecret);
			oauth.setOAuthToken(token);
			oauth.setOAuthTokenSecret(unauthorizedSecret);
			oauth.setOAuthVerifier(verifyCode);
			HashMap<String,String> accessTokenAndSecret = new HashMap<String,String>();
			accessTokenAndSecret.put("accessToken",oauthHelper.getAccessToken(oauth));
			accessTokenAndSecret.put("tokenSecret",oauth.getOAuthTokenSecret());
			return accessTokenAndSecret;
	}
}
