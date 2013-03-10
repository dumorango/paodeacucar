package br.com.companhiadesistemas.itim.gpa.gpaoffice.extensions.wkf;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.odm.core.OdmManager;

import br.com.companhiadesistemas.itim.gpa.gpaoffice.customizations.HistoricalOrgsManager;

import com.ibm.itim.common.AttributeChangeOperation;
import com.ibm.itim.common.AttributeValue;
import com.ibm.itim.dataservices.model.ModelCommunicationException;
import com.ibm.itim.dataservices.model.ObjectNotFoundException;
import com.ibm.itim.dataservices.model.domain.*;
import com.ibm.itim.workflow.application.WorkflowApplication;
import com.ibm.itim.workflow.application.WorkflowExecutionContext;
import com.ibm.itim.workflow.engine.ExecutionContext;
import com.ibm.itim.workflow.engine.RetryException;
import com.ibm.itim.workflow.model.ActivityResult;
import com.ibm.itim.workflow.model.type.ProcessResult;
import com.ibm.itim.workflowextensions.AccountExtensions;
import com.ibm.itim.workflowextensions.WorkflowExtensionException;

public class GpaOfficeWorkflowApplication implements WorkflowApplication{

	HistoricalOrgsManager historicalOrgManager;
	public GpaOfficeWorkflowApplication() {
		ApplicationContext context = new ClassPathXmlApplicationContext("springldap.xml");
        historicalOrgManager = (HistoricalOrgsManager) context.getBean("historicalGAppsAccountManager");
	}

	public void setContext(WorkflowExecutionContext ctx) {
		this.ctx = ctx;
	}

	public ActivityResult customModify(Person person, Service service,
			Account account) throws RetryException, WorkflowExtensionException, ModelCommunicationException, ObjectNotFoundException {
		String lastOrg = getOrgUnit(account);
		AccountExtensions ae = new AccountExtensions((ExecutionContext) ctx);
		ProcessResult pr = ae.modifyAccount(person, service, account);	
		if(!pr.getSummary().equalsIgnoreCase(pr.FAILED)){
			saveLastOrg(account,lastOrg);
		}
		return new ActivityResult(pr.getStatus(), pr.getSummary(),
				pr.getDescription(), pr.getDetail());
	}
	
	private String getOrgUnit(Account account) throws ModelCommunicationException, ObjectNotFoundException{
		AccountEntity originalAccount = new AccountSearch().lookup(account.getDistinguishedName());
		AttributeValue av = originalAccount.getDirectoryObject().getAttribute("googleorgunit");
		return av.getString();
	}
	private void saveLastOrg(Account account,String lastOrg) throws ModelCommunicationException, ObjectNotFoundException{
		AttributeChangeOperation aco = account.getChanges().get("googleorgunit");
		if(aco!=null && aco.getAction().equalsIgnoreCase(AttributeChangeOperation.ACTION_CHANGE)){
				historicalOrgManager.addHistoricalOrgUnit(account,lastOrg);
		}
	}
	protected WorkflowExecutionContext ctx;
}
