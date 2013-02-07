package br.com.companhiadesistemas.itimapi.process;

import java.util.*;

import com.ibm.itim.workflow.model.*;

public class ProcessUtil {

	public static ArrayList<ActivityEntity> getManualActivities(WorkflowProcessEntity wpe) throws WorkflowException{
		@SuppressWarnings("unchecked")
		Collection<ActivityEntity> atividades = wpe.getActivities();
		ArrayList<ActivityEntity>atividadesManuais = new ArrayList<ActivityEntity>();
		for(ActivityEntity ae:atividades){
			Activity a = (Activity)ae.getValueObject();
			if(a.getActivityType().compareTo("M")==0){
				atividadesManuais.add(ae);
			}
			@SuppressWarnings("unchecked")
			Collection<WorkflowProcessEntity> subProcessos = ae.getChildren();
			for(WorkflowProcessEntity subProcesso:subProcessos)
				atividadesManuais.addAll(getManualActivities(subProcesso));
		}	
		return atividadesManuais;
	}
}
