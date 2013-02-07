package br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization;

import com.google.gdata.client.GoogleService;
import com.google.gdata.util.AuthenticationException;


public class ClientLoginAuthorizator implements ServiceAuthorizator{

	public ClientLoginAuthorizator(){
		
	}
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GoogleService authorize(GoogleService service) throws AuthenticationException {
		service.setUserCredentials(username, password);
		return service;
	}
	
}
