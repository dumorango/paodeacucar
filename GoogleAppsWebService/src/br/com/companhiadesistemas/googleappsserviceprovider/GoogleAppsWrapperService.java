package br.com.companhiadesistemas.googleappsserviceprovider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.*;
import java.net.*;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.googleappsserviceprovider.entities.*;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.*;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.*;
import br.com.companhiadesistemas.serviceproviders.integration.IntegrationInterface;

import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.data.appsforyourdomain.*;
import com.google.gdata.data.appsforyourdomain.generic.*;
import com.google.gdata.data.appsforyourdomain.provisioning.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.util.ServiceException;

@Deprecated
@SuppressWarnings({"rawtypes","unused","serial","unchecked"})
public class GoogleAppsWrapperService{
	GoogleAppsWebServices dc;
	AppsGroupsService groupsService;
	ObjectMapper mapper = new ObjectMapper();
	private static final Map<String, String> orgAttributeMap = new HashMap<String, String>(){
			{
	            put("description", "googleorgunitdescription");
	            put("name", "googleorgunitname");
	            put("orgUnitPath", "googleorgunitpath");
	            put("blockInheritance", "googleorgunitblockinheritance");
	        }
	    };

	public Object add(Object account) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(account);
		dc.createUser(getUserEntry(googleaccount));
		setNickNames(googleaccount.getEruid(),googleaccount.getAliases());
		setGroups(googleaccount.getEruid(),googleaccount.getErgroup());
		dc.setOrg(googleaccount.getEruid(),null,googleaccount.getGoogleorgunit());
		return true;
	}

	private boolean setNickNames(String username,ArrayList<String> nicknames) throws AppsForYourDomainException, ServiceException, IOException{
		if(nicknames==null)return true;
		List<String> actualnicknamesid = getNickNames(username);
		for(String actualnickname:actualnicknamesid){		
			if(!nicknames.contains(actualnickname))
				dc.deleteNickname(actualnickname);
		}
		for(String nickname:nicknames){		
			if(!actualnicknamesid.contains(nickname))
				dc.createNickname(username, nickname);
		}
		return true;
	}
	
	private ArrayList<String> getNickNames(String username) throws AppsForYourDomainException, ServiceException, IOException{
		NicknameFeed feed = dc.retrieveNicknames(username);
		ArrayList<String> nicknamelist = new ArrayList<String>();
		for(NicknameEntry actualnickname:feed.getEntries()){
			nicknamelist.add(actualnickname.getNickname().getName());
		}
		return nicknamelist;
	}
	private boolean setGroups(String username,ArrayList<String> groups) throws AppsForYourDomainException, ServiceException, IOException{
		if(groups==null)return true;
		List<String> actualgroupsid = getGroupsId(username);
		for(String actualgroupid:actualgroupsid){		
			if(!groups.contains(actualgroupid))
				groupsService.deleteMemberFromGroup(actualgroupid, username);
		}
		for(String group:groups){		
			if(!actualgroupsid.contains(group))
				groupsService.addMemberToGroup(group, username);
		}
		return true;
	}
	
	private ArrayList<String> getGroupsId(String username) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		GenericFeed feed = groupsService.retrieveGroups(username, true);
		ArrayList<String> groupsIdList = new ArrayList<String>();
		for(GenericEntry actualgroup:feed.getEntries()){
			groupsIdList.add(new GoogleAppsGroup(actualgroup).getErgroupid());
		}
		return groupsIdList;
	}
	
	public Object changePassword(Object account,byte newPassword[]) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(account);
		//googleaccount.setErpassword(String.valueOf(newPassword));
		return modify(account,googleaccount);
	}
	
	public Object modify(Object oldAccount,Object newAccount) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(newAccount);
		GoogleAppsAccount oldGoogleAccount = objectToAccount(oldAccount);
		dc.updateUser(googleaccount.getEruid(), getUserEntry(googleaccount));
		setNickNames(googleaccount.getEruid(),googleaccount.getAliases());
		setGroups(googleaccount.getEruid(),googleaccount.getErgroup());
		dc.setOrg(googleaccount.getEruid(),oldGoogleAccount.getGoogleorgunit(),googleaccount.getGoogleorgunit());
		return true;
	}
	
	public Object suspend(Object account) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(account);
		dc.suspendUser(googleaccount.getEruid());
		return true;
	}
	public Object restore(Object account) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(account);
		dc.restoreUser(googleaccount.getEruid());
		return true;
	}
	public Object delete(Object account) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(account);
		dc.deleteUser(googleaccount.getEruid());
		return true;
	}
	
	public Object retrieve(Object object) throws Exception{
		GoogleAppsAccount googleaccount = objectToAccount(object);
		googleaccount =  getGoogleAppsAccount(dc.retrieveUser(googleaccount.getEruid()));
		googleaccount.setAliases(getNickNamesList(googleaccount.getEruid()));
		googleaccount.setErgroup(getGroupsId(googleaccount.getEruid()));
		googleaccount.setGoogleorgunit(getOrgUnitId(googleaccount.getEruid()));
		return googleaccount;
	}
	
	private UserEntry getUserEntry(GoogleAppsAccount account) {
		UserEntry entry = new UserEntry();
		Login login = new Login();
		login.setUserName(account.getEruid());
		login.setPassword(account.getErpassword());
		String status = account.getEraccountstatus();
		login.setSuspended(status==null||"0".equalsIgnoreCase(status)?false:true);
		login.setChangePasswordAtNextLogin(Boolean.valueOf(account.getGooglechangepasswordnextlogin()));
		entry.addExtension(login);
		Name name = new Name();
		name.setGivenName(account.getFirstname());
		name.setFamilyName(account.getFamilyname());
		entry.addExtension(name);
		Quota quotaObj = new Quota();
		quotaObj.setLimit(Integer.parseInt(account.getQuotalimit()));
		entry.addExtension(quotaObj);
		return entry;
	}
	
	private GoogleAppsAccount getGoogleAppsAccount(UserEntry user) throws AppsForYourDomainException, ServiceException, IOException {
		GoogleAppsAccount account = new GoogleAppsAccount();
		Login login = user.getLogin();
		account.setEruid(login.getUserName());
		account.setErpassword(login.getPassword());
		account.setEraccountstatus((login.getSuspended())?"1":"0");
		account.setGooglechangepasswordnextlogin(String.valueOf(login.getChangePasswordAtNextLogin()));
		Name names = user.getName();
		account.setFirstname(names.getGivenName());
		account.setFamilyname(names.getFamilyName());
		account.setQuotalimit(String.valueOf(user.getQuota().getLimit()));
		account.setDn(GoogleAppsAccount.USERNAME_ATTRIBUTE + "=" + account.getEruid());
		return account;
	}
	
	private ArrayList<String> getNickNamesList(String userName) throws AppsForYourDomainException, ServiceException, IOException {
		NicknameFeed retrieveNicknames = dc.retrieveNicknames(userName);
		List<NicknameEntry> nicknames = retrieveNicknames.getEntries();
		ArrayList<String> aliases = new ArrayList<String>();
		for (NicknameEntry nickname : nicknames) {
			aliases.add(nickname.getNickname().getName());
		}
		return aliases;
	}
	
	private Object getUserProfile(String userName) throws MalformedURLException, IOException, ServiceException{
		ContactEntry profile = dc.getUserProfile(userName);
		return profile;
	}
	
	public ArrayList<?> search() throws Exception{
		ArrayList<Object> entities = retrieveAllUsers();
		entities.addAll(retrieveAllGroups());
		return entities;
	}
	
	private ArrayList<Object> retrieveAllUsers() throws Exception{
		UserFeed uf;
		uf = dc.retrieveAllUsers();
		ArrayList<Object> retorno = new ArrayList<Object>();
		Iterator<UserEntry> entries = uf.getEntries().iterator();
		while(entries.hasNext()){
			GoogleAppsAccount account = getGoogleAppsAccount(entries.next());
			account.setAliases(getNickNamesList(account.getEruid()));
			account.setErgroup(getGroupsId(account.getEruid()));
			account.setGoogleorgunit(getOrgUnitId(account.getEruid()));
			retorno.add(account);
		}
		return retorno;
		
	}
	
	private String getOrgUnitId(String userName) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException {
		return getOrg(dc.getOrg(userName)).getGoogleorgunitpath();
	}

	private ArrayList<Object> retrieveAllGroups() throws Exception{
		ArrayList<Object> retorno = new ArrayList<Object>();
		Iterator<GenericEntry> entries = dc.getGroupService().retrieveAllPagesOfGroups().iterator();
		while(entries.hasNext()){
			retorno.add(new GoogleAppsGroup(entries.next()));
		}
		retorno.addAll(getAllOrgs());
		return retorno;
	}
	
	private ArrayList<GoogleAppsOrgUnit> getAllOrgs() throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		ArrayList<GoogleAppsOrgUnit> orgs = new ArrayList<GoogleAppsOrgUnit>();
		for(GenericEntry generic_org:dc.getAllOrgs()){
			orgs.add(getOrg(generic_org));
		}
		return orgs;
	}
	
	private GoogleAppsOrgUnit getOrg(GenericEntry generic_org) throws UnsupportedEncodingException{
		LinkedHashMap object_map = new LinkedHashMap();
		for(String property:orgAttributeMap.keySet()){
			String generic_org_property = generic_org.getProperty(property);
			if(generic_org_property!=null)
				object_map.put(orgAttributeMap.get(property), generic_org_property);
		}
		object_map.put("googleorgunitpath",URLDecoder.decode((String) object_map.get("googleorgunitpath"),"UTF-8"));
		return mapper.convertValue(object_map, GoogleAppsOrgUnit.class);	
	}
	
	public boolean connect(Map connectionProperties) throws Exception {
		
		Map<String,String> props = ((Map<String,String>)connectionProperties);
		ProxyManager.setParameters(
				props.get("erproxyhost")
				,props.get("erproxyport")
				,props.get("erproxyuser")
				,props.get("erproxypassword")
		);
		ServiceAuthorizator authorizator;
		if(connectionProperties.get("googleoauthtoken")!=null){
			OAuthAuthorizator oAuthAuthorizator = new OAuthAuthorizator();
			oAuthAuthorizator.setOAuthConsumerKey(props.get("googleoauthconsumerkey"));
			oAuthAuthorizator.setOAuthConsumerSecret(props.get("googleoauthconsumersecret"));
			HashMap<String,String> oauthtoken = mapper.readValue(props.get("googleoauthtoken"), HashMap.class);
			oAuthAuthorizator.setOAuthToken(oauthtoken.get("accessToken"));
			oAuthAuthorizator.setOAuthTokenSecret(oauthtoken.get("tokenSecret"));
			authorizator = oAuthAuthorizator;
			
		}else{
			ClientLoginAuthorizator clientLoginAuthorizator = new ClientLoginAuthorizator();
			clientLoginAuthorizator.setUsername((String) connectionProperties.get("eruid"));
			clientLoginAuthorizator.setPassword((String) connectionProperties.get("erpassword"));
			authorizator = clientLoginAuthorizator;
		}
		
		dc = new GoogleAppsWebServices(
				(String) connectionProperties.get("maildomain")
				,authorizator
		);
		dc.setAuthorizator(authorizator);
		this.groupsService = dc.getGroupService();
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return true;
	}	
	
	private GoogleAppsAccount objectToAccount(Object obj){
		return mapper.convertValue(obj, GoogleAppsAccount.class);
	}
	
	
}


