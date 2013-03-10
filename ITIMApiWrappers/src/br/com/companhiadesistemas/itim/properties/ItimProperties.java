package br.com.companhiadesistemas.itim.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import br.com.companhiadesistemas.itimpropertieswrapper.ItimPropertiesWrapper;

import com.ibm.itim.apps.InitialPlatformContext;
import com.ibm.itim.apps.PlatformContext;
import com.ibm.itim.common.properties.PropertiesManager;

public class ItimProperties{
	
	//private PropertiesManager pm;
	ItimPropertiesWrapper ipw;;

	public ItimProperties() throws FileNotFoundException, IOException{
		//pm = PropertiesManager.gInstance();
	}
	
	public String getLdapPassword() throws FileNotFoundException, IOException, Exception{
		/*
		ipw = new ItimPropertiesWrapper();
		boolean isEncrypted = Boolean.parseBoolean(pm.getProperty("enrole","enrole.password.ldap.encrypted"));
		if(isEncrypted)
			return ipw.getEncryptedProperty("java.naming.security.credentials", "enRoleLDAPConnection.properties");
		return pm.getProperty("enrole.ldap.connection","java.naming.security.credentials");
		*/
		//TODO
		return null;
	}
	
	public String getLdapConnectionProperty(String property){
		//TODO
		return null;
	}
	
	public Hashtable getLdapConnectionEnvironment() throws FileNotFoundException, IOException, Exception{
		/*Map connectionEnv = pm.getProperties("enrole.ldap.connection");
		connectionEnv.put("java.naming.security.credentials", getLdapPassword());
		System.out.println("Ldap Env: "+connectionEnv);*/
		ipw = new ItimPropertiesWrapper();
		return new Hashtable(ipw.getLdapConnectionProperties());
		
	}
	public String getSystemUserConnectionPassword(){
		/*boolean isEncrypted = Boolean.parseBoolean(pm.getProperty("enrole","enrole.password.appServer.encrypted"));
		return (isEncrypted)?
				pm.getEncryptedProperty("enrole","java.naming.security.credentials")
				:pm.getProperty("enrole","java.naming.security.credentials");
		*/
		//TODO
		return null;
	
	}
	public String getEbjConnectionPassword(){
		/*
		boolean isEncrypted = Boolean.parseBoolean(pm.getProperty("enrole","enrole.password.appServer.encrypted"));
		return (isEncrypted)?
				pm.getEncryptedProperty("enrole","enrole.appServer.ejbuser.credentials")
				:pm.getProperty("enrole","enrole.appServer.ejbuser.credentials");
		*/
				//TODO
				return null;
	
	}
	
	public Hashtable getEbjConnectionEnvironment(){
		/*
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
		*/
		//TODO
		return null;
	}

}
