package br.com.companhiadesistemas.serviceproviders.jsonrpc;

import java.io.IOException;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import br.com.companhiadesistemas.serviceproviders.logging.Logging;

import com.googlecode.jsonrpc4j.JsonRpcClientException;
import com.ibm.itim.remoteservices.provider.RequestStatus;

@Aspect
public class JsonRpcRequestStatusCasting {
	
	@Around("execution(public * JsonRpcClient.*(..))" +
			" && ! execution(public * JsonRpcClient.test(..))" +
			" && ! execution(public * JsonRpcClient.search(..))")
	public Object returnRequestStatus(ProceedingJoinPoint pjp){
		Object retValue = null;
		try{
			try{
			retValue = pjp.proceed();
			return new RequestStatus(RequestStatus.SUCCESSFUL,"Sucesso");
			}catch(JsonRpcClientException ex){
				ex.printStackTrace();
				ObjectMapper mapper = new ObjectMapper();
				Map exceptionMap = mapper.readValue(ex.getData(), Map.class);
				return new RequestStatus(RequestStatus.UNSUCCESSFUL,"Erro:: "+exceptionMap.get("exceptionTypeName")
						+ "\nMensagem: "+exceptionMap.get("message")); 
			}
		}catch(Exception ex){	
			ex.printStackTrace();
			return new RequestStatus(RequestStatus.UNSUCCESSFUL,"Falha: "+ex);
		} catch (Throwable e) {
			e.printStackTrace();
			return new RequestStatus(RequestStatus.UNSUCCESSFUL,"Falha: "+e);
		}
	}
}
