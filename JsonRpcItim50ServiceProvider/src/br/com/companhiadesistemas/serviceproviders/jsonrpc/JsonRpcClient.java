package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import br.com.companhiadesistemas.serviceproviders.integration.IntegrationInterface;
import br.com.companhiadesistemas.serviceproviders.integration.IntegrationLayersEnum;
import br.com.companhiadesistemas.serviceproviders.logging.Logging;

import com.googlecode.jsonrpc4j.*;

@SuppressWarnings({ "rawtypes"})
public class JsonRpcClient implements IntegrationInterface{
	
	IntegrationInterface remoteService;
	
	public JsonRpcClient(Map connectionProperties) throws JsonParseException, JsonMappingException, IOException{
		HashMap<String,Object> integrationLayerConfig = IntegrationLayersEnum.getIntegrationConfig(connectionProperties);
		JsonRpcHttpClient client = new JsonRpcHttpClient(
			    new URL((String) integrationLayerConfig.get("webservicehost")));
		client.setReadTimeoutMillis(3600000);
		remoteService = ProxyUtil.createProxy(
			    client.getClass().getClassLoader(),
			    IntegrationInterface.class,
			    client);
		
	}
	
	public Object add(Map connectionProperties,Object entity) throws Exception{
		return	remoteService.add(connectionProperties,entity);
	}
	
	public Object changePassword(Map connectionProperties,Object account, byte newPassword[]) throws Exception{
		return remoteService.changePassword(connectionProperties,account, newPassword);
	}
	
	public Object modify(Map connectionProperties,Object oldEntity,Object newEntity) throws Exception{
		return remoteService.modify(connectionProperties,oldEntity,newEntity);
	}
	
	
	public Object restore(Map connectionProperties,Object account) throws Exception{
		return remoteService.restore(connectionProperties,account);
	}
	
	public Object suspend(Map connectionProperties,Object account) throws Exception{
		return 	remoteService.suspend(connectionProperties,account);
	}
	
	public Object delete(Map connectionProperties,Object account) throws Exception{
		return remoteService.delete(connectionProperties,account);
	}
	
	public ArrayList<?> search(Map connectionProperties,Collection<String> objectclasses) throws Exception{
		Object retorno = remoteService.search(connectionProperties,objectclasses);
		System.out.println("Retorno: "+retorno);
		return (ArrayList<?>) retorno;
	}
	
	public boolean test(Map connectionProperties,
		Collection<String> objectClasses) throws Exception {
		return remoteService.test(connectionProperties,objectClasses);
	}
	
}
