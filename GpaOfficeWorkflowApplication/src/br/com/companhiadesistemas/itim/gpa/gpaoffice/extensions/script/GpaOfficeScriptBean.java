package br.com.companhiadesistemas.itim.gpa.gpaoffice.extensions.script;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ibm.itim.script.ExtensionBean;

import br.com.companhiadesistemas.itim.gpa.gpaoffice.customizations.HistoricalOrgsManager;

public class GpaOfficeScriptBean implements ExtensionBean{

	HistoricalOrgsManager historicalOrgManager;

	public String getLastHistoricalOrgUnit(String accountDN){
		ApplicationContext context = new ClassPathXmlApplicationContext("springldap.xml");
	    historicalOrgManager = (HistoricalOrgsManager) context.getBean("historicalGAppsAccountManager");
		return historicalOrgManager.getLastOrgUnit(accountDN);
	}

}
