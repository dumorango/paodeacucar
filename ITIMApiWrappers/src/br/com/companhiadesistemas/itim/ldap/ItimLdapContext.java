package br.com.companhiadesistemas.itim.ldap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;


import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.*;

import br.com.companhiadesistemas.itim.properties.ItimProperties;

public class ItimLdapContext implements ContextSource{
	  
	public ItimLdapContext() throws FileNotFoundException, IOException{
		}

	public DirContext getReadOnlyContext(){
		return getContext();
}

	public DirContext getReadWriteContext(){
			return getContext();
	}

	public DirContext getContext(String principal, String credentials){
		return getContext();
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
		}
		return null;
	}
}
