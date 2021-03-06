package br.com.companhiadesistemas.googleappsserviceprovider.googleapis;

/* Copyright (c) 2008 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.ServiceAuthorizator;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.gdata.client.AuthTokenFactory.*;
import com.google.gdata.client.appsforyourdomain.AppsForYourDomainQuery;
import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.client.appsforyourdomain.EmailListRecipientService;
import com.google.gdata.client.appsforyourdomain.EmailListService;
import com.google.gdata.client.appsforyourdomain.NicknameService;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.calendar.CalendarService;
//import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainErrorCode;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.EmailList;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Name;
import com.google.gdata.data.appsforyourdomain.Nickname;
import com.google.gdata.data.appsforyourdomain.Quota;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListRecipientEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.EmailListRecipientFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.NicknameFeed;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.extensions.Who;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import com.google.gdata.client.contacts.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
* This is the client library for the Google Apps for Your Domain GData API.
* It shows how the AppsForYourDomainService can be used to manage users on a 
* domain.  This class contains a number of methods which can be used to
* create, update, retrieve, and delete entities such as users, email lists
* and nicknames.  Also included is sample usage of the client library.   
* 
*/
public class GoogleAppsWebServices {

 private static final String APPS_FEEDS_URL_BASE =
     "https://apps-apis.google.com/a/feeds/";

 private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

 private static final JsonFactory JSON_FACTORY = new JacksonFactory();

 private static final Logger LOGGER = Logger.getLogger(
		 GoogleAppsWebServices.class.getName());
 
 private ServiceAuthorizator authorizator;
 
  public ServiceAuthorizator getAuthorizator() {
	return authorizator;
}

public void setAuthorizator(ServiceAuthorizator authorizator) {
	this.authorizator = authorizator;
}

protected static final String SERVICE_VERSION = "2.0";
 /**
  * Main entry point.  Parses arguments and creates and invokes the
  * AppsForYourDomainClient.
  *
  * Usage: java AppsForYourDomainClient --admin_email [email]
  *                                     --admin_password [pass]
  *                                     --domain [domain]
  */
 /*
 public static void main(String[] arg)
     throws Exception {
   SimpleCommandLineParser parser = new SimpleCommandLineParser(arg);
   String adminEmail = parser.getValue("admin_email", "email", "e");
   String adminPassword = parser.getValue("admin_password", "pass", "p");
   String domain = parser.getValue("domain", "domain", "d");

   boolean help = parser.containsKey("help", "h");
   if (help || (adminEmail == null) || (adminPassword == null) || (domain == null)) {
     usage();
     System.exit(1);
   }

   AppsForYourDomainClient client =
       new AppsForYourDomainClient(adminEmail, adminPassword, domain);
   client.run();
 }
	*/
 /*
  * Prints the command line usage of this sample application.
  */
 private static void usage() {
   System.out.println("Usage: java AppsForYourDomainClient" +
       " --admin_email [email] --admin_password [pass] --domain [domain]");
   System.out.println(
       "\nA simple application that performs user, email list,\n" +
       "and nickname related operations on the given domain using.\n" +
       "the provided admin username and password.\n");
 }
 ContactsService contactService;
 protected final String domain;
 protected String domainUrlBase;
 protected EmailListRecipientService emailListRecipientService;
 protected EmailListService emailListService;
 protected AppsGroupsService groupService;
 
 protected NicknameService nicknameService;
 String orgCustomerId;

 GoogleOrgManagementWebService orgservice;

 public UserService userService;

 public GoogleAppsWebServices(String domain) throws AuthenticationException {
	 String applicationName = "grupopaodeacucar-ITIMAdapter-v1.0";
	   this.domain = domain;
	   this.domainUrlBase = APPS_FEEDS_URL_BASE + domain + "/";
	   userService = new UserService(applicationName);
 }
 
 /**
  * Constructs an AppsForYourDomainClient for the given domain using the
  * given admin credentials.
  *
  * @param adminEmail An admin user's email address such as admin@domain.com
  * @param adminPassword The admin's password
  * @param domain The domain to administer
 * @throws Exception 
  */
 

 public GoogleAppsWebServices(String domain,ServiceAuthorizator auth) throws Exception{
   this(domain);
   this.authorizator=auth;
   
   String applicationName = "grupopaodeacucar-ITIMAdapter-v1.0";
  
   userService = new UserService(applicationName);
   userService.getExtensionProfile();
   
   authorizator.authorize(userService);
   
   orgservice = new GoogleOrgManagementWebService(domain);
   orgCustomerId = orgservice.retrieveCustomerId("testegapps.grupopaodeacucar.com.br").getProperty("customerId");
  
   nicknameService = new NicknameService(
       "gdata-sample-AppsForYourDomain-NicknameService");
   authorizator.authorize(nicknameService);

   emailListService = new EmailListService(
       "gdata-sample-AppsForYourDomain-EmailListService");
   authorizator.authorize(emailListService);

   emailListRecipientService = new EmailListRecipientService(
       "gdata-sample-AppsForYourDomain-EmailListRecipientService");
   authorizator.authorize(emailListRecipientService);

   groupService = new AppsGroupsService(domain, 
       "gdata-sample-AppsForYourDomain-AppsGroupService");
   authorizator.authorize(groupService);
   
   contactService = new ContactsService(applicationName);
   authorizator.authorize(contactService);
   
 }

 /**
  * Set admin privilege for user. Note that executing this method for a user
  * who is already an admin has no effect.
  * 
  * @param username The user you wish to make an admin.
  * @throws AppsForYourDomainException If a Provisioning API specific error
  *         occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public UserEntry addAdminPrivilege(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Setting admin privileges for user '" + username + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   UserEntry userEntry = userService.getEntry(retrieveUrl, UserEntry.class);
   userEntry.getLogin().setAdmin(true);

   URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   return userService.update(updateUrl, userEntry);
 }
 
 
 /**
  * Adds an email address to an email list.
  *
  * @param recipientAddress The email address you wish to add.
  * @param emailList The email list you wish to modify.
  * @return The EmailListRecipientEntry of the newly created email list
  *         recipient.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#addMemberToGroup(String,String)}
  *             with Groups instead.
  */
 @Deprecated
 public EmailListRecipientEntry addRecipientToEmailList(
     String recipientAddress, String emailList)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Adding '" + recipientAddress + "' to emailList '" + emailList + "'.");

   EmailListRecipientEntry emailListRecipientEntry = new EmailListRecipientEntry();
   Who who = new Who();
   who.setEmail(recipientAddress);
   emailListRecipientEntry.addExtension(who);

   URL insertUrl =
       new URL(domainUrlBase + "emailList/" + SERVICE_VERSION + "/" + emailList + "/recipient");
   return emailListRecipientService.insert(insertUrl, emailListRecipientEntry);
 }
/**
  * Creates an empty email list.
  *
  * @param emailList The name of the email list you wish to create.
  * @return An EmailListEntry object of the newly created email list.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#createGroup(String,String,String,String)} 
  *             with Groups instead.
  */
 @Deprecated
 public EmailListEntry createEmailList(String emailList) 
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Creating email list '" + emailList + "'.");

   EmailListEntry entry = new EmailListEntry();
   EmailList emailListExtension = new EmailList();
   emailListExtension.setName(emailList);
   entry.addExtension(emailListExtension);

   URL insertUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION);
   return emailListService.insert(insertUrl, entry);
 }

 /**
  * Creates a nickname for the username.
  *
  * @param username The user for which we want to create a nickname.
  * @param nickname The nickname you wish to create.
  * @return A NicknameEntry object of the newly created nickname. 
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public NicknameEntry createNickname(String username, String nickname) 
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Creating nickname '" + nickname +
       "' for user '" + username + "'.");

   NicknameEntry entry = new NicknameEntry();
   Nickname nicknameExtension = new Nickname();
   nicknameExtension.setName(nickname);
   entry.addExtension(nicknameExtension);

   Login login = new Login();
   login.setUserName(username);
   entry.addExtension(login);

   URL insertUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION);
   return nicknameService.insert(insertUrl, entry);
 }

 /**
  * Creates a new user with an email account.
  *
  * @param username The username of the new user.
  * @param givenName The given name for the new user.
  * @param familyName the family name for the new user.
  * @param password The password for the new user.
  * @return A UserEntry object of the newly created user.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserEntry createUser(String username, String givenName,
     String familyName, String password) throws AppsForYourDomainException, 
     ServiceException, IOException {

   return createUser(username, givenName, familyName, password, null, null);
 }

 /**
  * Creates a new user with an email account.
  *
  * @param username The username of the new user.
  * @param givenName The given name for the new user.
  * @param familyName the family name for the new user.
  * @param password The password for the new user.
  * @param quotaLimitInMb User's quota limit in megabytes.  This field is only
  * used for domains with custom quota limits.
  * @return A UserEntry object of the newly created user.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserEntry createUser(String username, String givenName,
     String familyName, String password, Integer quotaLimitInMb)
     throws AppsForYourDomainException, ServiceException, IOException {

   return createUser(username, givenName, familyName, password, null, 
       quotaLimitInMb);
 }

 /**
  * Creates a new user with an email account.
  *
  * @param username The username of the new user.
  * @param givenName The given name for the new user.
  * @param familyName the family name for the new user.
  * @param password The password for the new user.
  * @param passwordHashFunction The name of the hash function to hash the 
  * password
  * @return A UserEntry object of the newly created user.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserEntry createUser(String username, String givenName,
     String familyName, String password, String passwordHashFunction)
     throws AppsForYourDomainException, ServiceException, IOException {

   return createUser(username, givenName, familyName, password,
       passwordHashFunction, null);
 }

 /**
  * Creates a new user with an email account.
  *
  * @param username The username of the new user.
  * @param givenName The given name for the new user.
  * @param familyName the family name for the new user.
  * @param password The password for the new user.
  * @param passwordHashFunction Specifies the hash format of the password
  * parameter
  * @param quotaLimitInMb User's quota limit in megabytes.  This field is only
  * used for domains with custom quota limits.
  * @return A UserEntry object of the newly created user.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserEntry createUser(String username, String givenName,
     String familyName, String password, String passwordHashFunction,
     Integer quotaLimitInMb)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Creating user '" + username + "'. Given Name: '" + givenName +
       "' Family Name: '" + familyName +
       (passwordHashFunction != null 
           ? "' Hash Function: '" + passwordHashFunction : "") + 
       (quotaLimitInMb != null 
           ? "' Quota Limit: '" + quotaLimitInMb + "'." : "'.")
       );

   UserEntry entry = new UserEntry();
   Login login = new Login();
   login.setUserName(username);
   login.setPassword(password);
   if (passwordHashFunction != null) {
     login.setHashFunctionName(passwordHashFunction);
   }
   entry.addExtension(login);

   Name name = new Name();
   name.setGivenName(givenName);
   name.setFamilyName(familyName);
   entry.addExtension(name);

   if (quotaLimitInMb != null) {
     Quota quota = new Quota();
     quota.setLimit(quotaLimitInMb);
     entry.addExtension(quota);
   }

   URL insertUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION );
   return userService.insert(insertUrl, entry);
 }
 public UserEntry createUser(UserEntry entry)
	 throws AppsForYourDomainException, ServiceException, IOException{
	 URL insertUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION );
	 return userService.insert(insertUrl, entry);
 }

 /**
  * Deletes an email list.
  *
  * @param emailList The email list you with to delete.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#deleteGroup(String)}
  *             with Groups instead.
  */
 @Deprecated
 public void deleteEmailList(String emailList)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Attempting to delete emailList '" + emailList + "'.");

   URL deleteUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION + "/" + emailList);
   emailListService.delete(deleteUrl);
 }

 /**
  * Deletes a nickname.
  *
  * @param nickname The nickname you wish to delete.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public void deleteNickname(String nickname)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Deleting nickname '" + nickname + "'.");

   URL deleteUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION + "/" + nickname);
   nicknameService.delete(deleteUrl);
 }

 /**
  * Deletes a user.
  * 
  * @param username The user you wish to delete.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public void deleteUser(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Deleting user '" + username + "'.");

   URL deleteUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   userService.delete(deleteUrl);
 }
 
 /**
  * Require a user to change password at next login. Note that executing this
  * method for a user who is already required to change password at next login
  * as no effect.
  * 
  * @param username The user who must change his or her password.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public UserEntry forceUserToChangePassword(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Requiring " + username + " to change password at " +
       "next login.");

   URL retrieveUrl = new URL(domainUrlBase + "user/"
       + SERVICE_VERSION + "/" + username);
   UserEntry userEntry = userService.getEntry(retrieveUrl, UserEntry.class);
   userEntry.getLogin().setChangePasswordAtNextLogin(true);

   URL updateUrl = new URL(domainUrlBase + "user/"
       + SERVICE_VERSION + "/" + username);
   return userService.update(updateUrl, userEntry);
 }

 public List<GenericEntry> getAllOrgs() throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
	return orgservice.retrieveAllOrganizationUnits(orgCustomerId);
}

 /**
  * Public getter for AppsGroupsService
  * @return the groupService
  */
 public AppsGroupsService getGroupService() {
   return groupService;
 }

 public GenericEntry getOrg(String userName) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		return orgservice.retrieveOrganizaionUser(
				orgCustomerId
				 ,userName+"@"+domain
				 );
 }

 public ContactEntry getUserProfile(String userName) throws MalformedURLException, IOException, ServiceException{
	  return contactService.getEntry(
		      new URL("https://www.google.com/m8/feeds/profiles/domain/"+domain+"/full/"+userName),
		      ContactEntry.class);
 }

 /**
  * Remove admin privilege for user. Note that executing this method for a user
  * who is not an admin has no effect.
  * 
  * @param username The user you wish to remove admin privileges.
  * @throws AppsForYourDomainException If a Provisioning API specific error
  *         occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public UserEntry removeAdminPrivilege(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Removing admin privileges for user '" + username + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   UserEntry userEntry = userService.getEntry(retrieveUrl, UserEntry.class);
   userEntry.getLogin().setAdmin(false);

   URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   return userService.update(updateUrl, userEntry);
 }

 /**
  * Removes an email address from an email list.
  *
  * @param recipientAddress The email address you wish to remove.
  * @param emailList The email list you wish to modify.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#deleteMemberFromGroup(String,String)}
  *             with Groups instead.
  */
 @Deprecated
 public void removeRecipientFromEmailList(String recipientAddress,
     String emailList) throws AppsForYourDomainException, ServiceException,
     IOException {

   LOGGER.log(Level.INFO, "Removing '" + recipientAddress + "' from emailList '" + emailList
       + "'.");

   URL deleteUrl =
       new URL(domainUrlBase + "emailList/" + SERVICE_VERSION + "/" + emailList + "/recipient/"
           + recipientAddress);
   emailListRecipientService.delete(deleteUrl);
 }

 /**
  * Restores a user. Note that executing this method for a user who is not
  * suspended has no effect.
  * 
  * @param username The user you wish to restore.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public UserEntry restoreUser(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Restoring user '" + username + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   UserEntry userEntry = userService.getEntry(retrieveUrl, UserEntry.class);
   userEntry.getLogin().setSuspended(false);

   URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   return userService.update(updateUrl, userEntry);
 }

 /**
  * Retrieves all email lists in domain. This method may be very slow for
  * domains with a large number of email lists. Any changes to email lists,
  * including creations and deletions, which are made after this method is
  * called may or may not be included in the Feed which is returned.
  *
  * @return A EmailListFeed object of the retrieved email lists.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#retrieveAllGroups()}
  *             with Groups instead.
  */
 @Deprecated
 public EmailListFeed retrieveAllEmailLists()
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Retrieving all email lists.");

   URL retrieveUrl = new URL(domainUrlBase + "emailList/"
       + SERVICE_VERSION + "/");
   EmailListFeed allEmailLists = new EmailListFeed();
   EmailListFeed currentPage;
   Link nextLink;

   do {
     currentPage = emailListService.getFeed(retrieveUrl, EmailListFeed.class);
     allEmailLists.getEntries().addAll(currentPage.getEntries());
     nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
     if (nextLink != null) {
       retrieveUrl = new URL(nextLink.getHref());
     }
   } while (nextLink != null);

   return allEmailLists;
 }

 /**
  * Retrieves all nicknames in domain.  This method may be very slow for
  * domains with a large number of nicknames.  Any changes to nicknames,
  * including creations and deletions, which are made after this method is
  * called may or may not be included in the Feed which is returned.
  *
  * @return A NicknameFeed object of the retrieved nicknames.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public NicknameFeed retrieveAllNicknames()
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Retrieving all nicknames.");

   URL retrieveUrl = new URL(domainUrlBase + "nickname/"
       + SERVICE_VERSION + "/");
   NicknameFeed allNicknames = new NicknameFeed();
   NicknameFeed currentPage;
   Link nextLink;

   do {
     currentPage = nicknameService.getFeed(retrieveUrl, NicknameFeed.class);
     allNicknames.getEntries().addAll(currentPage.getEntries());
     nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
     if (nextLink != null) {
       retrieveUrl = new URL(nextLink.getHref());
     }
   } while (nextLink != null);

   return allNicknames;
 }

 /**
  * Retrieves all recipients in an email list. This method may be very slow for
  * email lists with a large number of recipients. Any changes to the email
  * list contents, including adding or deleting recipients which are made after
  * this method is called may or may not be included in the Feed which is
  * returned.
  * 
  * @return An EmailListRecipientFeed object of the retrieved recipients.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#retrieveAllMembers(String)}
  *             with Groups instead.
  */
 @Deprecated
 public EmailListRecipientFeed retrieveAllRecipients(String emailList)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Retrieving all recipients in emailList '" + emailList + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "emailList/"
       + SERVICE_VERSION + "/" + emailList + "/recipient/");

   EmailListRecipientFeed allRecipients = new EmailListRecipientFeed();
   EmailListRecipientFeed currentPage;
   Link nextLink;

   do {
     currentPage = emailListRecipientService.getFeed(retrieveUrl, EmailListRecipientFeed.class);
     allRecipients.getEntries().addAll(currentPage.getEntries());
     nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
     if (nextLink != null) {
       retrieveUrl = new URL(nextLink.getHref());
     }
   } while (nextLink != null);

   return allRecipients;
 }

 /**
  * Retrieves all users in domain.  This method may be very slow for domains
  * with a large number of users.  Any changes to users, including creations
  * and deletions, which are made after this method is called may or may not be
  * included in the Feed which is returned.
  *
  * @return A UserFeed object of the retrieved users.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserFeed retrieveAllUsers()
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Retrieving all users.");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/");
   UserFeed allUsers = new UserFeed();
   UserFeed currentPage;
   Link nextLink;

   do {
     currentPage = userService.getFeed(retrieveUrl, UserFeed.class);
     allUsers.getEntries().addAll(currentPage.getEntries());
     nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
     if (nextLink != null) {
       retrieveUrl = new URL(nextLink.getHref());
     }
   } while (nextLink != null);

   return allUsers;
 }

 /**
  * Retrieves an email list.
  *
  * @param emailList The name of the email list you want to retrieve.
  * @return An EmailListEntry object of the retrieved email list.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#retrieveGroup(String)}
  *             with Groups instead.
  */
 @Deprecated
 public EmailListEntry retrieveEmailList(String emailList)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Retrieving email list '" + emailList + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "emailList/" + SERVICE_VERSION + "/" + emailList);
   return emailListService.getEntry(retrieveUrl, EmailListEntry.class);
 }

 /**
  * Retrieves all email lists in which the recipient is subscribed. Recipient
  * can be given as a username or an email address of a hosted user.
  *
  * @param recipient The email address or username of the recipient.
  * @return An EmailListFeed object containing the email lists.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#retrieveGroups(String,boolean)} 
  *             with Groups.instead
  */
 @Deprecated
 public EmailListFeed retrieveEmailLists(String recipient)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Retrieving email lists for '" + recipient + "'.");

   URL feedUrl = new URL(domainUrlBase + "emailList/"
       + SERVICE_VERSION);
   AppsForYourDomainQuery query = new AppsForYourDomainQuery(feedUrl);
   query.setRecipient(recipient);

   return emailListService.query(query, EmailListFeed.class);
 }

 /**
  * Retrieves a nickname.
  *
  * @param nickname The nickname you wish to retrieve.
  * @return A NicknameEntry object of the newly created nickname. 
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public NicknameEntry retrieveNickname(String nickname) throws AppsForYourDomainException,
     ServiceException, IOException {
   LOGGER.log(Level.INFO, "Retrieving nickname '" + nickname + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION + "/" + nickname);
   return nicknameService.getEntry(retrieveUrl, NicknameEntry.class);
 }

 /**
  * Retrieves all nicknames for the given username.
  * 
  * @param username The user for which you want all nicknames.
  * @return A NicknameFeed object with all the nicknames for the user.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public NicknameFeed retrieveNicknames(String username)
     throws AppsForYourDomainException, ServiceException, IOException {
   LOGGER.log(Level.INFO,
       "Retrieving nicknames for user '" + username + "'.");

   URL feedUrl = new URL(domainUrlBase + "nickname/" + SERVICE_VERSION);
   AppsForYourDomainQuery query = new AppsForYourDomainQuery(feedUrl);
   query.setUsername(username);
   return nicknameService.query(query, NicknameFeed.class);
 }

 /**
  * Retrieves one page (100) of email lists in domain. Any changes to email
  * lists, including creations and deletions, which are made after this method
  * is called may or may not be included in the Feed which is returned. If the
  * optional startEmailListName parameter is specified, one page of email lists
  * is returned which have names at or after startEmailListName as per ASCII
  * value ordering with case-insensitivity. A value of null or empty string
  * indicates you want results from the beginning of the list.
  *
  * @param startEmailListName The starting point of the page (optional).
  * @return A EmailListFeed object of the retrieved email lists.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#retrievePageOfGroups(Link)}
  *             with Groups instead.
  */
 @Deprecated
 public EmailListFeed retrievePageOfEmailLists(String startEmailListName)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Retrieving one page of email lists"
       + (startEmailListName != null ? " starting at " + startEmailListName : "") + ".");

   URL retrieveUrl = new URL(
         domainUrlBase + "emailList/" + SERVICE_VERSION + "/");
   AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
   query.setStartEmailListName(startEmailListName);
   return emailListService.query(query, EmailListFeed.class);
 }

 /**
  * Retrieves one page (100) of nicknames in domain.  Any changes to
  * nicknames, including creations and deletions, which are made after
  * this method is called may or may not be included in the Feed which is
  * returned.  If the optional startNickname parameter is specified, one page
  * of nicknames is returned which have names at or after startNickname as per
  * ASCII value ordering with case-insensitivity.  A value of null or empty
  * string indicates you want results from the beginning of the list.
  *
  * @param startNickname The starting point of the page (optional).
  * @return A NicknameFeed object of the retrieved nicknames.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 
 /*
 public NicknameFeed retrievePageOfNicknames(String startNickname)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Retrieving one page of nicknames"
       + (startNickname != null ? " starting at " + startNickname : "") + ".");

   URL retrieveUrl = new URL(
       domainUrlBase + "nickname/" + SERVICE_VERSION + "/");
   AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
   query.setStartNickname(startNickname);
   return nicknameService.query(query, NicknameFeed.class);
 }
*/
 /**
  * Retrieves one page (100) of recipients in an email list. Changes to the
  * email list recipients including creations and deletions, which are made
  * after this method is called may or may not be included in the Feed which is
  * returned. If the optional startRecipient parameter is specified, one page
  * of recipients is returned which have email addresses at or after
  * startRecipient as per ASCII value ordering with case-insensitivity. A value
  * of null or empty string indicates you want results from the beginning of
  * the list.
  *
  * @param emailList The name of the email list for which we are retrieving
  *        recipients.
  * @param startRecipient The starting point of the page (optional).
  * @return A EmailListRecipientFeed object of the retrieved recipients.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  * @deprecated Email lists have been replaced by Groups. Use
  *             {@link AppsGroupsService#retrievePageOfMembers(Link)}
  *             with Groups instead.
  */
 /*
 @Deprecated
 public EmailListRecipientFeed retrievePageOfRecipients(String emailList,
     String startRecipient) throws AppsForYourDomainException,
     ServiceException, IOException {

   LOGGER.log(Level.INFO, "Retrieving one page of recipients"
       + (startRecipient != null ? " starting at " + startRecipient : "") + ".");

   URL retrieveUrl =
       new URL(domainUrlBase + "emailList/" + SERVICE_VERSION + "/" + emailList + "/recipient/");
   AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
   query.setStartRecipient(startRecipient);
   return emailListRecipientService.query(query, EmailListRecipientFeed.class);
 }
*/
 /**
  * Retrieves one page (100) of users in domain.  Any changes to users,
  * including creations and deletions, which are made after this method is
  * called may or may not be included in the Feed which is returned.  If the
  * optional startUsername parameter is specified, one page of users is
  * returned which have usernames at or after the startUsername as per ASCII
  * value ordering with case-insensitivity.  A value of null or empty string
  * indicates you want results from the beginning of the list.
  *
  * @param startUsername The starting point of the page (optional).
  * @return A UserFeed object of the retrieved users.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 /*
 public UserFeed retrievePageOfUsers(String startUsername)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Retrieving one page of users"
       + (startUsername != null ? " starting at " + startUsername : "") + ".");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/");
   AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
   query.setStartUsername(startUsername);
   return userService.query(query, UserFeed.class);
 }
*/
 /**
  * Retrieves a user.
  * 
  * @param username The user you wish to retrieve.
  * @return A UserEntry object of the retrieved user. 
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserEntry retrieveUser(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO,
       "Retrieving user '" + username + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   System.out.println("URL: "+retrieveUrl);
   return userService.getEntry(retrieveUrl, UserEntry.class);
 }

 public void teste() throws Throwable{
	 LOGGER.log(Level.INFO,
		       "Teste");
 }
 
 public void setOrg(String eruid, String oldorgunit,String googleorgunit) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
	/*
	orgservice.updateOrganizationUser(
			orgCustomerId
			, eruid+"@"+domain
			, (oldorgunit==null)?null:URLEncoder.encode(oldorgunit,"UTF-8")
			,  URLEncoder.encode(googleorgunit,"UTF-8"));
			*/
}


 /**
  * Suspends a user. Note that executing this method for a user who is already
  * suspended has no effect.
  * 
  * @param username The user you wish to suspend.
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  *         service.
  */
 public UserEntry suspendUser(String username)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Suspending user '" + username + "'.");

   URL retrieveUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   UserEntry userEntry = userService.getEntry(retrieveUrl, UserEntry.class);
   userEntry.getLogin().setSuspended(true);

   URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   return userService.update(updateUrl, userEntry);
 }

public GenericEntry updateUser(String username, Map<String, String> updatedAttributes)
	      throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
	    GenericEntry entry = new GenericEntry();
	    entry.addProperties(updatedAttributes);
	    URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
	    return userService.update(updateUrl, entry);
	  }



/**
  * Updates a user.
  *
  * @param username The user to update.
  * @param userEntry The updated UserEntry for the user.
  * @return A UserEntry object of the newly updated user. 
  * @throws AppsForYourDomainException If a Provisioning API specific occurs.
  * @throws ServiceException If a generic GData framework error occurs.
  * @throws IOException If an error occurs communicating with the GData
  * service.
  */
 public UserEntry updateUser(String username, UserEntry userEntry)
     throws AppsForYourDomainException, ServiceException, IOException {

   LOGGER.log(Level.INFO, "Updating user '" + username + "'.");

   URL updateUrl = new URL(domainUrlBase + "user/" + SERVICE_VERSION + "/" + username);
   return userService.update(updateUrl, userEntry);
 }
 
 @Override
 public String toString(){
	 return "DomainUrlBase: "+this.domainUrlBase
	 +"\nSERVICE_VERSION: "+SERVICE_VERSION
	 +"\nDomain: "+domain
	 +"\norgCustomerId: "+orgCustomerId;
 }
}

