package br.com.companhiadesistemas.googleappsserviceprovider;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyManager {
	
	public static void setParameters(String proxyHost,String proxyPort,String proxyUser,String proxyPassword){
		if(proxyHost!=null){
			System.setProperty("http.proxyHost", proxyHost);
			System.setProperty("https.proxyHost", proxyHost);
		}
		if(proxyPort!=null){
			System.setProperty("http.proxyPort", proxyPort);
			System.setProperty("https.proxyPort", proxyPort);
		}
		Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPassword));
		
	}
}

class ProxyAuthenticator extends Authenticator {  
	  
    private String user, password;  
  
    public ProxyAuthenticator(String user, String password) {  
        this.user = user;  
        this.password = password;  
    }  
  
    protected PasswordAuthentication getPasswordAuthentication() {  
        return new PasswordAuthentication(user, password.toCharArray());  
    }  
}