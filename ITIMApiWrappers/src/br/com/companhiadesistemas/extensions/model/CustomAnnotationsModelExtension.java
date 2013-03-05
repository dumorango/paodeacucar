package br.com.companhiadesistemas.extensions.model;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import br.com.companhiadesistemas.itimapi.wrappers.CustomAccountSearchWrapper;
import com.ibm.itim.script.ContextItem;
import com.ibm.itim.script.ScriptContextDAO;
import com.ibm.itim.script.ScriptException;
import com.ibm.itim.script.ScriptInterface;

public class CustomAnnotationsModelExtension implements com.ibm.itim.script.ScriptExtension{

	public List getContextItems(){
		System.out.println(this.getClass()+" getContextItems");
		return allBeans;
	}
	
	private List allBeans;
	
	public void initialize(ScriptInterface si,ScriptContextDAO dao) throws ScriptException, IllegalArgumentException{
		System.out.println(this.getClass()+" initialize");
		try{
			for(Class scriptExtensionClass:findScriptExtensionClasses()){
				ScriptExtension scriptExtension = (ScriptExtension) scriptExtensionClass.getAnnotation(ScriptExtension.class);
				allBeans.add(ContextItem.createConstructor(scriptExtension.constructor(),scriptExtensionClass));
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ScriptException(ex.toString());
		}
	}
	
	public Set<Class<?>> findScriptExtensionClasses(){
		   Reflections reflections = new Reflections(new ConfigurationBuilder()
           .setUrls(ClasspathHelper.forPackage(""))
           );
           

		   Set<Class<?>> annotated1 =
	               reflections.getTypesAnnotatedWith(ScriptExtension.class);
		return annotated1;
	}
	
	public static Set<BeanDefinition> findScriptExtensionClasses(String objectClass) throws Exception{
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);

				scanner.addIncludeFilter(new AnnotationTypeFilter(ScriptExtension.class));

				return scanner.findCandidateComponents("");
	}

}
