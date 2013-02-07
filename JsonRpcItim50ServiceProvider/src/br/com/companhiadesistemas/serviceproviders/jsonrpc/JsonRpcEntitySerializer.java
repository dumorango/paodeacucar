package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;

import br.com.companhiadesistemas.serviceproviders.integration.ItimEntity;

public class JsonRpcEntitySerializer extends JsonSerializer<Object>{

	@SuppressWarnings({ "unchecked" })
	@Override
	public void serialize(Object entity, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException{
		try{
		jsonGenerator.writeStartObject();
		String mappingMethod = "map";
		Class<?> accountClass = entity.getClass();
		ItimEntity jsonentity = accountClass.getAnnotation(ItimEntity.class);
		Class<Enum<?>> serializeEnumClass = (Class<Enum<?>>) jsonentity.serializerEnum();
		Method method = null;
		for(Method m: serializeEnumClass.getDeclaredMethods())
			if(m.getName().equalsIgnoreCase(mappingMethod))
				method = m;
		for(Enum<?> e:serializeEnumClass.getEnumConstants()){
			Object att = method.invoke(e, entity);
			if(Iterable.class.isInstance(att)){
				jsonGenerator.writeArrayFieldStart(e.name());
				for(String value:(Iterable<String>) att){
					jsonGenerator.writeString(value);
				}
				jsonGenerator.writeEndArray();
			}else if(Map.class.isInstance(att)){
				jsonGenerator.writeStringField(e.name(),mapToJsonString((Map) att));
			}else{
				jsonGenerator.writeStringField(e.name(),(String) att);
			}
		}
		}catch(Exception ex){
			throw new IOException(ex);
		}
	}
	
	String mapToJsonString(Map map) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(map);
	}
}
