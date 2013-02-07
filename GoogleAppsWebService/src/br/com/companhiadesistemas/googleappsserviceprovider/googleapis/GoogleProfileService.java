package br.com.companhiadesistemas.googleappsserviceprovider.googleapis;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gdata.client.GoogleService;
import com.google.gdata.client.appsforyourdomain.AppsPropertyService;
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.*;

import com.google.gdata.util.ServiceException;
import com.google.gdata.util.common.xml.XmlWriter;

public class GoogleProfileService extends GoogleWebService{

	
	GoogleService googleservice;
	
	GoogleProfileService(String domain) {
		 super(domain);
		 googleservice = new ContactsService(applicationName);
		

	}

	@Override
	public GoogleService getGoogleService() {
		// TODO Auto-generated method stub
		return googleservice;
	}
	
	public ContactEntry retrieveProfile(String userName) throws MalformedURLException, IOException, ServiceException {
		  ContactEntry profile = googleservice.getEntry(
		      new URL("https://www.google.com/m8/feeds/profiles/domain/"+domain+"/full/"+userName.split("@")[0]),
		      ContactEntry.class);
		  // Do something with the profile.
		  return profile;
	}
	
	public void printAllProfiles()
		    throws ServiceException, IOException {
		  // Request the feed
		ContactsService myService = (ContactsService) googleservice;
		  URL feedUrl = new URL("https://www.google.com/m8/feeds/profiles/domain/"+domain+"/full");
		  ContactFeed resultFeed = myService.getFeed(feedUrl, ContactFeed.class);
		  // Print the results
		  System.out.println(resultFeed.getTitle().getPlainText());
		  for (ContactEntry entry : resultFeed.getEntries()) {
		    if (entry.hasName()) {
		      Name name = entry.getName();
		      if (name.hasFullName()) {
		        String fullNameToDisplay = name.getFullName().getValue();
		        if (name.getFullName().hasYomi()) {
		          fullNameToDisplay += " (" + name.getFullName().getYomi() + ")";
		        }
		        System.out.println("\t\t" + fullNameToDisplay);
		      } else {
		        System.out.println("\t\t (no full name found)");
		      }
		      if (name.hasNamePrefix()) {
		        System.out.println("\t\t" + name.getNamePrefix().getValue());
		      } else {
		        System.out.println("\t\t (no name prefix found)");
		      }
		      if (name.hasGivenName()) {
		        String givenNameToDisplay = name.getGivenName().getValue();
		        if (name.getGivenName().hasYomi()) {
		          givenNameToDisplay += " (" + name.getGivenName().getYomi() + ")";
		        }
		        System.out.println("\t\t" + givenNameToDisplay);
		      } else {
		        System.out.println("\t\t (no given name found)");
		      }
		      if (name.hasAdditionalName()) {
		        String additionalNameToDisplay = name.getAdditionalName().getValue();
		        if (name.getAdditionalName().hasYomi()) {
		          additionalNameToDisplay += " (" + name.getAdditionalName().getYomi() + ")";
		        }
		        System.out.println("\t\t" + additionalNameToDisplay);
		      } else {
		        System.out.println("\t\t (no additional name found)");
		      }
		      if (name.hasFamilyName()) {
		        String familyNameToDisplay = name.getFamilyName().getValue();
		        if (name.getFamilyName().hasYomi()) {
		          familyNameToDisplay += " (" + name.getFamilyName().getYomi() + ")";
		        }
		        System.out.println("\t\t" + familyNameToDisplay);
		      } else {
		        System.out.println("\t\t (no family name found)");
		      }
		      if (name.hasNameSuffix()) {
		        System.out.println("\t\t" + name.getNameSuffix().getValue());
		      } else {
		        System.out.println("\t\t (no name suffix found)");
		      }
		    } else {
		      System.out.println("\t (no name found)");
		    }

		    System.out.println("Email addresses:");
		    for (Email email : entry.getEmailAddresses()) {
		      System.out.print(" " + email.getAddress());
		      if (email.getRel() != null) {
		        System.out.print(" rel:" + email.getRel());
		      }
		      if (email.getLabel() != null) {
		        System.out.print(" label:" + email.getLabel());
		      }
		      if (email.getPrimary()) {
		        System.out.print(" (primary) ");
		      }
		      System.out.print("\n");
		    }

		    System.out.println("IM addresses:");
		    for (Im im : entry.getImAddresses()) {
		      System.out.print(" " + im.getAddress());
		      if (im.getLabel() != null) {
		        System.out.print(" label:" + im.getLabel());
		      }
		      if (im.getRel() != null) {
		        System.out.print(" rel:" + im.getRel());
		      }
		      if (im.getProtocol() != null) {
		        System.out.print(" protocol:" + im.getProtocol());
		      }
		      if (im.getPrimary()) {
		        System.out.print(" (primary) ");
		      }
		      System.out.print("\n");
		    }

		    System.out.println("Extended Properties:");
		    for (ExtendedProperty property : entry.getExtendedProperties()) {
		      if (property.getValue() != null) {
		        System.out.println("  " + property.getName() + "(value) = " +
		            property.getValue());
		      } else if (property.getXmlBlob() != null) {
		        System.out.println("  " + property.getName() + "(xmlBlob)= " +
		            property.getXmlBlob().getBlob());
		      }
		    }

		    Link photoLink = entry.getContactPhotoLink();
		    String photoLinkHref = photoLink.getHref();
		    System.out.println("Photo Link: " + photoLinkHref);

		    if (photoLink.getEtag() != null) {
		      System.out.println("Profile Photo's ETag: " + photoLink.getEtag());
		    }

		    System.out.println("Profile's ETag: " + entry.getEtag());
		  }
		}

}
