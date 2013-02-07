package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.util.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.serviceproviders.logging.Logging;

@Aspect
public class Serialize {
	
	@SuppressWarnings("unchecked")
	@Around("execution( * JsonRpcService.search(..))")
	public ArrayList<LinkedHashMap<String, Object>> serializeSearch(ProceedingJoinPoint pjp) throws Throwable { 
		ObjectMapper mapper = new ObjectMapper();
		ArrayList<?> arrayList = (ArrayList<?>) pjp.proceed();
		ArrayList<LinkedHashMap<String,Object>> ret = new ArrayList<LinkedHashMap<String,Object>>();
		for(Object obj:arrayList)
			ret.add(mapper.convertValue(obj, LinkedHashMap.class));
		return ret;
	}
}
