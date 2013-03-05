package br.com.companhiadesistemas.itim.api.connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import javax.security.auth.Subject;
import javax.security.auth.login.*;

import br.com.companhiadesistemas.itim.properties.ItimProperties;

import com.ibm.itim.apps.*;
import com.ibm.itim.apps.jaas.callback.PlatformCallbackHandler;
import com.ibm.itim.common.properties.*;

public class ContextManager {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String itimUser;
	private String itimPassword;
	private PlatformContext platform;
	public ContextManager(String itimUser,String itimPassword) throws ApplicationException, FileNotFoundException, IOException{
		this.itimUser = itimUser;
		this.itimPassword = itimPassword;
		createPlatformContext();
	}
	private void createPlatformContext() throws ApplicationException, FileNotFoundException, IOException{
		ItimProperties ip = new ItimProperties();
		platform = new InitialPlatformContext(ip.getEbjConnectionEnvironment());
	}
	
	public PlatformContext getPlatformContext(){
		return platform;
	}
	public Subject getSubject() throws LoginException{
		// Create the ITIM JAAS CallbackHandler
		PlatformCallbackHandler handler = new PlatformCallbackHandler(itimUser,
				itimPassword);
		handler.setPlatformContext(platform);
		LoginContext lc = new LoginContext("ITIM", handler);
		lc.login();
		return lc.getSubject();
	}
}
