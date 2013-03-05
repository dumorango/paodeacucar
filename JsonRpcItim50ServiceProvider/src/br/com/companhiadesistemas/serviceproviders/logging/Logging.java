package br.com.companhiadesistemas.serviceproviders.logging;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.*;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import br.com.companhiadesistemas.serviceproviders.integration.IntegrationPointcuts;
@Aspect
public class Logging {
	
	public static Logger LOGGER = Logger.getRootLogger();
	
	public Logging(){
		LOGGER.setAdditivity(false);
		
	}
	
	public void setLogProperties(Map<String,String> connectionProperties){
		LOGGER.setLevel(Level.toLevel("ALL"));
		Appender appender = new ConsoleAppender(new SimpleLayout());
		String logFilePath = "";
		try {
			HashMap<String,String> loggingConfig = new ObjectMapper().readValue(connectionProperties.get("erloggingconfig"), HashMap.class);
			logFilePath = loggingConfig.get("logFilePath");
			
			try {
				appender = new DailyRollingFileAppender(new SimpleLayout(),logFilePath,"'.'yyyy-MM-dd");
				LOGGER.addAppender(appender);
			} catch (IOException e) {
				LOGGER.warn("Houve um erro ao acessar o arquivo de log: "+logFilePath+", portanto será utilizado o SystemOut.log.");
			}
			LOGGER.setLevel(Level.toLevel(loggingConfig.get("logLevel")));
		} catch (Exception e1) {
			LOGGER.warn("Houve um erro ao recuperar as configurações de log através do serviço "+connectionProperties.get("erservicename")+", portanto será utilizada a configuração padrão.");
			e1.printStackTrace();
			
		} 
		
	}
	
	@Pointcut("execution(* br.com.companhiadesistemas.serviceproviders.*.*(..))")
	public void allMethods(){}
	
	//@Before("allMethods()")
	public void beforeMethod(JoinPoint jp){
		
		LOGGER.info("Método invocado: "+jp.getSignature().getName());
		LOGGER.debug("Argumentos:"+ Arrays.toString(jp.getArgs()));
	}
	
	@Before("IntegrationPointcuts.receivedRequests()")
	public void logOnReceiveRequest(JoinPoint jp){
		//Configure log based on service parameters
		this.setLogProperties((Map)jp.getArgs()[0]);
		LOGGER.info("Requisição recebida: "+jp.getSignature().getName());
		LOGGER.debug("Argumentos: "+ Arrays.toString(jp.getArgs()));
	}
	
	@AfterReturning("IntegrationPointcuts.receivedRequests()")
	public void logAfterRequest(JoinPoint jp){
		//Configure log based on service parameters
		this.setLogProperties((Map)jp.getArgs()[0]);
		LOGGER.info("Requisição finalizada sem exceções: "+jp.getSignature().getName());
		LOGGER.debug("Argumentos:"+ Arrays.toString(jp.getArgs()));
	}
	
}
