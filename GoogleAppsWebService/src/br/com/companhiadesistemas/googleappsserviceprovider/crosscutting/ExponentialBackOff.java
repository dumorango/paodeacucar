package br.com.companhiadesistemas.googleappsserviceprovider.crosscutting;

import java.util.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;


import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.GoogleWebService;
import br.com.companhiadesistemas.serviceproviders.logging.Logging;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;

@Aspect
public class ExponentialBackOff {
	
	@Around("Pointcuts.webServiceMethods()")
	public Object exponentialBackOffAroundWebServices(ProceedingJoinPoint pjp) throws Throwable { 
		if(GoogleWebService.class.isAssignableFrom(pjp.getSignature().getDeclaringType())){
			  int n_tentativas = 0;
			  int MAX = 10;
			  Logging.LOGGER.debug("Método "+pjp.getSignature().getName()+" irá executar em Exponential BackOff com "+MAX+" tentativas.");
			  Exception ret_ex = null;
			  do{
				  try{
					  Logging.LOGGER.debug("Tentativa de número: "+(n_tentativas+1));
					  return pjp.proceed();
				  }catch(AppsForYourDomainException ex){
					  Logging.LOGGER.error(("AppsForYourDomainException Http "+ex.getHttpErrorCodeOverride()+" na tentativa de número: "+(n_tentativas+1)));
					  ret_ex = ex;
					  if(!Arrays.asList(400, 500, 503).contains(ex.getHttpErrorCodeOverride()))
						 throw ex;
					  long sleeptime = (n_tentativas ^ 2)+(new Random().nextInt(1000));
					  Logging.LOGGER.error("Tempo de espera: "+sleeptime);
					  Thread.sleep(sleeptime);
				  } 
			  }while(++n_tentativas<=MAX);
			  throw ret_ex;
		 }else{
			 return pjp.proceed();
		 }
	}
	
}
