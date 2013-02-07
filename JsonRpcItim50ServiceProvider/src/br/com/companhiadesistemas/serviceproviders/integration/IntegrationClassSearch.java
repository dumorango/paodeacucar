package br.com.companhiadesistemas.serviceproviders.integration;

import java.util.*;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

public class IntegrationClassSearch {
	public static Class<?> getItimEntityClass(String objectClass) throws Exception{
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);

				scanner.addIncludeFilter(new AnnotationTypeFilter(ItimEntity.class));

				for (BeanDefinition bd : scanner.findCandidateComponents("")){
					Class<?> itimEntityClass = Class.forName(bd.getBeanClassName());
					ItimEntity jsonentity = (ItimEntity) itimEntityClass.getAnnotation(ItimEntity.class);
					if(jsonentity!=null && jsonentity.objectClass().equalsIgnoreCase(objectClass))
						return itimEntityClass; 
				}
				throw new Exception("Não foi encontrada nenuma classe anotada como "+ItimEntity.class.getName()+" ,com o valor objectclass="+objectClass);
	}
	
	public static Class<?> getAdapterClass(Class<?> itimEntityClass) throws Exception{
		ItimEntity jsonentity = (ItimEntity) itimEntityClass.getAnnotation(ItimEntity.class);
		if(jsonentity==null)
			throw new Exception("A classe "+itimEntityClass+" não foi anotada como "+ItimEntity.class.getName());
		return jsonentity.adapterClass();
	}
	public static Class<?> getItimEntityClass(Collection<String> objectClasses) throws Exception{
		for(String objectclass:objectClasses){
			try{
				return getItimEntityClass(objectclass);
			}catch(Exception e){}
		}
		throw new Exception("Não foi encontrada nenuma classe anotada como "+ItimEntity.class.getName()+" ,com um dos valores objectclass="+objectClasses);
	}
	
}
