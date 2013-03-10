package br.com.companhiadesistemas.itim.ldap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import org.springframework.ldap.core.support.LdapContextSource;


import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.*;

import br.com.companhiadesistemas.itim.properties.ItimProperties;
import br.com.companhiadesistemas.itimpropertieswrapper.ItimPropertiesWrapper;

public class ItimLdapContext extends LdapContextSource{
	  
	public ItimLdapContext() {
		Hashtable<String, String> env;
		try {
			env = new ItimProperties().getLdapConnectionEnvironment();
			this.setUrl(env.get("java.naming.provider.url"));
			this.setPassword(env.get("java.naming.security.credentials"));
			this.setUserDn(env.get("java.naming.security.principal"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private DirContext getContext(){
		try {
			Hashtable env = new ItimProperties().getLdapConnectionEnvironment();
			env.put("java.naming.factory.initial", "");
			InitialDirContext context =	new InitialDirContext(env);
			return context;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
