package br.com.companhiadesistemas.googleappsserviceprovider.entities;

import java.io.IOException;
import java.net.URLDecoder;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;


import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;


@Deprecated
public class GoogleAccountSerializer extends JsonSerializer<GoogleAccount>{

	@Override
	public void serialize(GoogleAccount googleaccount, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException{
		jsonGenerator.writeStartObject();
		GenericEntry user = googleaccount.getUserEntry();
		jsonGenerator.writeStringField("eruid", user.getProperty("userEmail"));
		jsonGenerator.writeStringField("erpassword", user.getProperty("password"));
		jsonGenerator.writeStringField("eraccountstatus",(Boolean.valueOf(user.getProperty("isSuspended")))?"1":"0");
		jsonGenerator.writeStringField("googlechangepasswordnextlogin", String.valueOf(user.getProperty("isChangePasswordAtNextLogin")));
		jsonGenerator.writeStringField("firstname",user.getProperty("firstName"));
		jsonGenerator.writeStringField("familyname",user.getProperty("lastName"));
		jsonGenerator.writeStringField("quotalimit",user.getProperty("quotaInGb"));
		jsonGenerator.writeStringField("dn","eruid="+user.getProperty("userEmail"));
		jsonGenerator.writeArrayFieldStart("ergroup");
		
		for(GenericEntry actualgroup:googleaccount.getGroups())
			jsonGenerator.writeString(actualgroup.getProperty("groupId"));
		jsonGenerator.writeEndArray();
		jsonGenerator.writeArrayFieldStart("aliases");
		for(GenericEntry nickname:googleaccount.getNicknames())
			jsonGenerator.writeString(nickname.getProperty("aliasEmail"));
		jsonGenerator.writeEndArray();
		String org = googleaccount.getOrgUnit().getProperty("orgUnitPath");
		if(org!=null)
			jsonGenerator.writeStringField("googleorgunit",URLDecoder.decode(org));
		
	}
	
	
	
	
}
