package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import br.com.companhiadesistemas.serviceproviders.integration.IntegrationClassSearch;
import br.com.companhiadesistemas.serviceproviders.integration.ItimEntity;
import br.com.companhiadesistemas.serviceproviders.logging.Logging;


public class JsonRpcEntityDeserializer extends JsonDeserializer<Object>{
{
}

@SuppressWarnings({ "rawtypes", "unchecked" })
@Override
public Object deserialize(JsonParser jp, DeserializationContext dc)
		throws IOException, JsonProcessingException {
	try{
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String,Object> accountmap = mapper.readValue(jp,HashMap.class);
		Class<?> accountClass = IntegrationClassSearch.getItimEntityClass(
				Arrays.asList((((String) accountmap.get("objectclass")).split(",")))
				);
		Object entity = accountClass.newInstance();
		ItimEntity jsonentity = accountClass.getAnnotation(ItimEntity.class);
		Class<Enum> deserializeEnumClass = (Class<Enum>) jsonentity.deserializerEnum();
		Method method = null;
		for(Method m: deserializeEnumClass.getDeclaredMethods())
			if(m.getName().equalsIgnoreCase("deserialize"))
				method = m;
		for(String attributename:accountmap.keySet()){
			try{
				Enum instance = Enum.valueOf(deserializeEnumClass,attributename.toUpperCase());
				Object attributevalue = accountmap.get(attributename);
				entity = method.invoke(instance,entity,
					(ArrayList.class.isInstance(attributevalue))?(ArrayList<String>)attributevalue:(String)attributevalue);
			}catch(java.lang.IllegalArgumentException ex){
				Logging.LOGGER.error(attributename+" - Atributo não encontrado na classe "+jsonentity.deserializerEnum()+".");
			};
		}
		return entity;
	}catch(Exception ex){
		throw new IOException(ex);
	}
	}
}
