package br.com.companhiadesistemas.googleappsserviceprovider.googleapis;

import java.io.IOException;
import java.net.MalformedURLException;

import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.authorization.ServiceAuthorizator;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.appsforyourdomain.AppsGroupsService;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleGroupsService extends GoogleWebService {

	GoogleGroupsService(String domain) throws AuthenticationException {
		super(domain);
		googleservice = new AppsGroupsService(domain,applicationName);
	}

	protected AppsGroupsService googleservice;
	
	GenericFeed retrieveGroups(String userId) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		return googleservice.retrieveGroups(userId, true);
	}

	@Override
	public GoogleService getGoogleService() {
		return googleservice;
	}
	
	GenericEntry addMemberToGroup(String groupId, String memberName) throws AppsForYourDomainException, MalformedURLException, IOException, ServiceException{
		return googleservice.addMemberToGroup(groupId, memberName);
	}
}
