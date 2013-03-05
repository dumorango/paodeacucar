package br.com.companhiadesistemas.itim.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import com.ibm.itim.apps.InitialPlatformContext;
import com.ibm.itim.apps.PlatformContext;
import com.ibm.itim.common.properties.PropertiesManager;

public class ItimProperties{
	
	private PropertiesManager pm;
	
	public PropertiesManager getPm() {
		return pm;
	}

	public void setPm(PropertiesManager pm) {
		this.pm = pm;
	}

	public ItimProperties() throws FileNotFoundException, IOException{
		pm = PropertiesManager.gInstance();
	}
	
	public String getLdapPassword(){
		boolean isEncrypted = Boolean.parseBoolean(pm.getProperty("enrole","enrole.password.ldap.encrypted"));
		if(isEncrypted)
			return pm.getEncryptedProperty("enrole.ldap.connection","java.naming.security.credentials");
		return pm.getProperty("enrole.ldap.connection","java.naming.security.credentials");
	}
	
	public String getLdapConnectionProperty(String property){
		return pm.getProperty("enrole.ldap.connection",property);
	}
	
	public Hashtable getLdapConnectionEnvironment(){
		Map connectionEnv = pm.getProperties("enrole.ldap.connection");
		connectionEnv.put("enrole.ldap.connection", getLdapPassword());
		return new Hashtable(connectionEnv);
		
	}
	public String getSystemUserConnectionPassword(){
		boolean isEncrypted = Boolean.parseBoolean(pm.getProperty("enrole","enrole.password.appServer.encrypted"));
		return (isEncrypted)?
				pm.getEncryptedProperty("enrole","java.naming.security.credentials")
				:pm.getProperty("enrole","java.naming.security.credentials");
	
	}
	public String getEbjConnectionPassword(){
		boolean isEncrypted = Boolean.parseBoolean(pm.getProperty("enrole","enrole.password.appServer.encrypted"));
		return (isEncrypted)?
				pm.getEncryptedProperty("enrole","enrole.appServer.ejbuser.credentials")
				:pm.getProperty("enrole","enrole.appServer.ejbuser.credentials");
	
	}
	
	public Hashtable getEbjConnectionEnvironment(){
		Hashtable env = new Hashtable();
		String contextFactory = pm.getProperty("enrole","enrole.platform.contextFactory");
		String appServerUrl = pm.getProperty("enrole","enrole.appServer.url");
		String ejbUser = pm.getProperty("enrole","enrole.appServer.ejbuser.principal");
		String ejbPassword = getEbjConnectionPassword();
		env.put(InitialPlatformContext.CONTEXT_FACTORY, contextFactory);
		env.put(PlatformContext.PLATFORM_URL, appServerUrl);
		env.put(PlatformContext.PLATFORM_PRINCIPAL,ejbUser);
		env.put(PlatformContext.PLATFORM_CREDENTIALS, ejbPassword);
		return env;
	}

}
