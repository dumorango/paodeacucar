package br.com.companhiadesistemas.googleappsserviceprovider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.googleappsserviceprovider.entities.GoogleAccount;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.GoogleProvisioningWebServices;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.ClientLoginAuthorizator;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.OAuthAuthorizator;
import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.ServiceAuthorizator;
import br.com.companhiadesistemas.serviceproviders.integration.IntegrationAdapter;

@SuppressWarnings({"unchecked","rawtypes"})
public class GoogleAppsAdapter extends IntegrationAdapter<GoogleAccount>{

	GoogleProvisioningWebServices webservices;
	
	public boolean connect(Map connectionProperties) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
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
		
		webservices = new GoogleProvisioningWebServices();
		webservices.setAuthorizator(authorizator);
		webservices.setDomain(props.get("maildomain"));
		webservices.authorizeServices();
		mapper = new ObjectMapper();
		mapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return true;
	}

	
	public GoogleAccount add(Map connectionProperties,GoogleAccount account) throws Exception {
		connect(connectionProperties);
		webservices.createAccount(account);
		return null;
	}

	public GoogleAccount modify(Map connectionProperties,GoogleAccount oldAccount, GoogleAccount newAccount) throws Exception {
		connect(connectionProperties);
		webservices.updateUser(newAccount);
		return null;
		
	}

	public GoogleAccount changePassword(Map ConnectionProperties,GoogleAccount account, byte[] newPassword)
			throws Exception {
		webservices.updateUser(account);
		return null;
	}

	public GoogleAccount suspend(Map connectionProperties,GoogleAccount account) throws Exception {
		connect(connectionProperties);
		webservices.suspendUser(account);
		return null;
	}

	public GoogleAccount restore(Map connectionProperties,GoogleAccount account) throws Exception {
		connect(connectionProperties);
		webservices.restoreUser(account);
		return null;
	}

	public GoogleAccount delete( Map connectionProperties,GoogleAccount account) throws Exception {
		webservices.removeAccount(account);
		return null;
	}

	public Collection<?> search(Map connectionProperties) throws Exception {
		connect(connectionProperties);
		ArrayList entities = new ArrayList(webservices.getAllAccounts(null));
		entities.addAll(webservices.getAllOrgUnits());
		return entities;
	}
	
	public boolean test(Map connectionProperties) throws Exception{
		return connect(connectionProperties);
	}

}
