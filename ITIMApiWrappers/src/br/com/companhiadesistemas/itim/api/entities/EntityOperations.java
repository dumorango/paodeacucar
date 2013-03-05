package br.com.companhiadesistemas.itim.api.entities;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import br.com.companhiadesistemas.itim.api.connection.ContextManager;

import com.ibm.itim.apps.ApplicationException;
import com.ibm.itim.apps.AuthorizationException;
import com.ibm.itim.apps.Request;
import com.ibm.itim.apps.provisioning.AccountMO;
import com.ibm.itim.apps.provisioning.AccountManager;
import com.ibm.itim.dataservices.model.*;
import com.ibm.itim.dataservices.model.domain.*;

import java.rmi.RemoteException;
import java.util.*;

public class EntityOperations {

	/**
	 * @param args
	 * @throws ObjectNotFoundException 
	 * @throws ModelCommunicationException 
	 */
	
	private ContextManager ctx_manager;
	public EntityOperations(){
	
	}
	public EntityOperations(String itimUser,String itimPassword) throws ApplicationException, FileNotFoundException, IOException{
		this.ctx_manager = new ContextManager(itimUser,itimPassword);
	}
	
	public Request deprovisionAccount(DistinguishedName accountDN,Date date) throws ModelCommunicationException, ObjectNotFoundException, LoginException, AuthorizationException, RemoteException, ApplicationException{
		return getAccountManagedObject(accountDN).remove(date);
		
	}
	public Request modifyAccount(DistinguishedName accountDN,Date date){
		return null;
	}
	public AccountManager getAccountManager() throws LoginException{
		return new AccountManager(ctx_manager.getPlatformContext(),ctx_manager.getSubject());
	}
	
	public AccountMO getAccountManagedObject(DistinguishedName accountDN) throws LoginException{
		return new AccountMO(ctx_manager.getPlatformContext(),ctx_manager.getSubject(),accountDN);
	}
	
	
}

