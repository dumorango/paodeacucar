package br.com.companhiadesistemas.googleappsserviceprovider.crosscutting;

import java.util.Arrays;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import br.com.companhiadesistemas.googleappsserviceprovider.googleapis.GoogleWebService;

import br.com.companhiadesistemas.serviceproviders.integration.IntegrationPointcuts;
import br.com.companhiadesistemas.serviceproviders.logging.Logging;

@Aspect
public class DebugLogger {
	
	@Before("(Pointcuts.webServiceMethods() ) ")
	public void logBefore(JoinPoint joinPoint){
		if(GoogleWebService.class.isAssignableFrom(joinPoint.getSignature().getDeclaringType())){
			Logging.LOGGER.info("Iniciando o método de webservice "+joinPoint.getSignature().getName()+" da Classe "+joinPoint.getSignature().getDeclaringTypeName());
		}
	}
	
	@Around("Pointcuts.webServiceMethods()")
	public Object logWebServiceTime(ProceedingJoinPoint pjp) throws Throwable { 
		if(GoogleWebService.class.isAssignableFrom(pjp.getSignature().getDeclaringType())){
			long stime = System.currentTimeMillis();
			Object retObj = pjp.proceed();
			long etime = System.currentTimeMillis();
			Logging.LOGGER.debug("Tempo de Execução: " + (etime-stime)+" milisegundos.");
			return retObj;
		}else{
			return pjp.proceed();
		}
	}
	@AfterReturning(pointcut="(Pointcuts.webServiceMethods() ) ",returning="retVal")
	public void logAfterWebServices(JoinPoint joinPoint,Object retVal){
		if(GoogleWebService.class.isAssignableFrom(joinPoint.getSignature().getDeclaringType())){
			Logging.LOGGER.info("O método: "+joinPoint.getSignature().getName()+" retornou com sucesso.");
			Logging.LOGGER.debug("Retorno:"+ retVal);
		}
	}
	
	@Around("IntegrationPointcuts.adapterMethods()")
	public Object logAdapterMethods(ProceedingJoinPoint pjp) throws Throwable {
		long stime = System.currentTimeMillis();
		Object retObj = pjp.proceed();
		Logging.LOGGER.debug("Tempo de execução: "+(System.currentTimeMillis()-stime)+" milisegundos do método "+pjp.getSignature().getName()+" da classe de Adapter "+pjp.getSignature().getDeclaringTypeName()+".");
		return retObj;
	}
}
