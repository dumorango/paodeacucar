package br.com.companhiadesistemas.serviceproviders.integration;

import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.serviceproviders.logging.Logging;

@Aspect
public class AdapterCasting {
	
	@SuppressWarnings("unchecked")
	@Around("IntegrationPointcuts.receivedRequests()")
	public Object callAdapterMethods(ProceedingJoinPoint pjp) throws Throwable {
		Logging log = new Logging();
		log.logOnReceiveRequest(pjp);
		Object adapter = null;
		Class<?> accountclass = null;
		Method method = null;
		Object[] args = pjp.getArgs();
		ArrayList args_list = new ArrayList();
		Collections.addAll(args_list, args);
		String methodName = pjp.getSignature().getName();
		if(methodName.equalsIgnoreCase("search") || methodName.equalsIgnoreCase("test")){
			accountclass = IntegrationClassSearch.getItimEntityClass((Collection<String>)args[1]);
			adapter = getAdapterClassByAccountClass(accountclass).newInstance();
			method = getMethod(adapter.getClass(),methodName);
			args_list.remove(1);
			args = args_list.toArray();
			
		}else{
			args_list.remove(0);
			ObjectMapper mapper = new ObjectMapper();
			int c=1;
			for(Object arg:args_list)
				if(arg.getClass().getName().equalsIgnoreCase("java.util.LinkedHashmap")){
					if(accountclass==null)
						accountclass = getAccountClassByArg(arg);
					if(adapter==null)
						adapter = getAdapterClassByAccountClass(accountclass).newInstance();
					if(method==null)
						method = getMethod(adapter.getClass(),methodName);
					args[c++] = mapper.convertValue(arg, accountclass);
					
				}
		}
		try{
			Object retorno = method.invoke(adapter,args);
			log.logAfterRequest(pjp);
			return retorno;
		}catch(Exception ex){
			throw ExceptionUtils.getRootCause(ex);
		}
		
	}
	
	private Class<?> getAccountClassByArg(Object arg) throws Exception{
		 return IntegrationClassSearch.getItimEntityClass(Arrays.asList((((String) ((LinkedHashMap<?, ?>) arg).get("objectclass")).split(","))));
	}
	
	private Class<?> getAdapterClassByAccountClass(Class<?> accountClass) throws Exception{
		return IntegrationClassSearch.getAdapterClass(accountClass);
	}
	
	private Method getMethod(Class<?> adapterClass,String methodname) throws Exception{
		for(Method m: adapterClass.getDeclaredMethods())
			if(m.getName().equalsIgnoreCase(methodname))
				return m;
		throw new Exception("A classe "+adapterClass+" não possui o método: "+methodname);
	}
}
