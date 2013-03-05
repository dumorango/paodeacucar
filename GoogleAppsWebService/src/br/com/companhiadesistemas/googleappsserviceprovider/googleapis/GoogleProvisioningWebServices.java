package br.com.companhiadesistemas.googleappsserviceprovider.googleapis;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import br.com.companhiadesistemas.googleappsserviceprovider.entities.GoogleAccount;
import br.com.companhiadesistemas.googleappsserviceprovider.entities.GoogleOrgUnit;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.ServiceAuthorizator;
import com.google.gdata.client.GoogleService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.util.ServiceException;

public class GoogleProvisioningWebServices {

	private String domain;
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	private ServiceAuthorizator authorizator;
	private GoogleUserWebService userservice;
	private GoogleGroupsService groupsservice;
	private GoogleOrgManagementWebService orgservice;
	private GoogleProfileService profileservice;
	private String orgCustomerId;
	
	public ServiceAuthorizator getAuthorizator() {
		return authorizator;
	}

	public void setAuthorizator(ServiceAuthorizator authorizator) {
		this.authorizator = authorizator;
	}
	
	public void authorizeServices() throws Exception{
		userservice = new GoogleUserWebService(domain);
		authorizator.authorize(userservice.getGoogleService());
		orgservice = new GoogleOrgManagementWebService(domain);
		authorizator.authorize(orgservice.getGoogleService());
		groupsservice = new GoogleGroupsService(domain);
		authorizator.authorize(groupsservice.getGoogleService());
		profileservice = new GoogleProfileService(domain);
		authorizator.authorize(profileservice.getGoogleService());
		orgCustomerId = orgservice.retrieveCustomerId(domain).getProperty("customerId");
	}
	
	private String getUserEmail(GoogleAccount googleaccount){
		return googleaccount.getUserEntry().getProperty("userEmail");
	}
	public GoogleAccount getAccount(String username,ArrayList<String> attributes) throws Exception{
		GoogleAccount googleaccount = new GoogleAccount();
		googleaccount.setUserEntry(userservice.retrieveUser(username));
		setAccountProperties(googleaccount,attributes);
		return googleaccount;
	}
	
	private void setAccountProperties(GoogleAccount googleaccount,ArrayList<String> attributes) throws Exception{
		String userEmail = getUserEmail(googleaccount);
		//googleaccount.setUserEntry(userservice.retrieveUser(userEmail));
		googleaccount.setNicknames(userservice.retrieveAllUserAliases(userEmail));
		googleaccount.setOrgUnit(orgservice.retrieveOrganizaionUser(
				orgCustomerId
				 ,googleaccount.getUserEntry().getProperty("userEmail")
				 ));
		googleaccount.setGroups(groupsservice.retrieveGroups(userEmail).getEntries());
		//googleaccount.setProfile(profileservice.retrieveProfile(userEmail));
		//System.out.println("Profile: "+googleaccount.getProfile().getEmailAddresses());
	}
	
	public GoogleAccount createAccount(GoogleAccount googleaccount) throws MalformedURLException, IOException, ServiceException{
		userservice.createUser(googleaccount.getUserEntry());
		String userEmail = getUserEmail(googleaccount);
		for(GenericEntry alias:googleaccount.getNicknames())
			userservice.createAlias(alias.getProperty("aliasEmail"),userEmail);
		
		for(GenericEntry group:googleaccount.getGroups())
			groupsservice.addMemberToGroup(group.getProperty("groupId"),userEmail);
		return googleaccount;
	}
	
	public GoogleAccount updateUser(GoogleAccount googleaccount) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		userservice.updateUser(googleaccount.getUserEntry());
		String userEmail = getUserEmail(googleaccount);
		orgservice.updateOrganizationUser(orgCustomerId, userEmail,null,googleaccount.getOrgUnit());
		return googleaccount;
	}
	
	public GoogleAccount removeAccount(GoogleAccount googleAccount) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		userservice.deleteUser(getUserEmail(googleAccount));
		return googleAccount;
	}
	
	public GoogleAccount suspendUser(GoogleAccount googleAccount) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		GenericEntry user = googleAccount.getUserEntry();
		user.removeProperty("isSuspended");
		user.addProperty("isSuspended", "true");
		googleAccount.setUserEntry(userservice.updateUser(user));
		return googleAccount;
	}
	
	public GoogleAccount restoreUser(GoogleAccount googleAccount) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		GenericEntry user = googleAccount.getUserEntry();
		user.removeProperty("isSuspended");
		user.addProperty("isSuspended", "false");
		googleAccount.setUserEntry(userservice.updateUser(user));
		return googleAccount;
	}
	
	public Collection<GoogleAccount> getAllAccounts(ArrayList<String> attributes) throws Exception{
		ArrayList<GoogleAccount> accounts = new ArrayList<GoogleAccount>();
		HashMap<String,GoogleAccount> accounts_map = new HashMap<String,GoogleAccount>();
		for(GenericEntry user:userservice.retrieveAllUsers()){
			GoogleAccount googleaccount = new GoogleAccount();
			googleaccount.setUserEntry(user);
			accounts_map.put(user.getProperty("userEmail"),googleaccount);
		}
		for(GenericEntry alias:getAllAlias()){
			String userEmail = alias.getProperty("userEmail");
			if(userEmail!=null){
				GoogleAccount user = accounts_map.get(userEmail);
				if(user!=null)
					user.getNicknames().add(alias);
			}
			
		}
		for(GenericEntry orgUser:getAllOrganizationUsers()){
			String orgUserEmail = orgUser.getProperty("orgUserEmail");
			if(orgUserEmail!=null){
				GoogleAccount user = accounts_map.get(orgUserEmail);
				if(user!=null)
					user.setOrgUnit(orgUser);
			}
		}
		return accounts_map.values();
	}
	
	private List<GenericEntry> getAllAlias() throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		return userservice.retrieveAllAliases();
	}
	
	private Collection<GenericEntry> getAllOrganizationUsers() throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		return orgservice.retrieveAllOrganizationUsers(orgCustomerId);
	}
	public ArrayList<GoogleOrgUnit> getAllOrgUnits() throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		ArrayList<GoogleOrgUnit> googleorgunits = new ArrayList<GoogleOrgUnit>();
		for(GenericEntry genericEntry:orgservice.retrieveAllOrganizationUnits(orgCustomerId))
			googleorgunits.add(new GoogleOrgUnit(genericEntry));
		return googleorgunits;
	}
}

