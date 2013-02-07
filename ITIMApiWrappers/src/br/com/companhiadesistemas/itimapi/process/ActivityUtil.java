package br.com.companhiadesistemas.itimapi.process;

import java.util.*;

import com.ibm.itim.workflow.model.*;

public class ActivityUtil {

	public static ActivityParticipant getManualActivityActor(ActivityEntity ae) throws WorkflowException{
		@SuppressWarnings("unchecked")
		List<EventAudit> history = ae.getHistory();
		for(EventAudit ea:history){
			if(ea.getEventType().equalsIgnoreCase("CM")){
				return ((CompleteManualActivityEventAudit) ea).getParticipant();
			}
		}
		return null;
	}
	
	public static String getManualActivityActorId(ActivityEntity ae) throws WorkflowException{
		ActivityParticipant p = getManualActivityActor(ae);
		return (p!=null)?p.getId():null;
	}
}
