package br.com.companhiadesistemas.itim.gpa.gpaoffice.customizations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.naming.directory.SearchControls;

import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.odm.core.OdmManager;

public class HistoricalOrgsManager {
	
	private String baseDN;
	private UUID uuid = new UUID(4242L, 4242L);
	private SearchControls searchControls; 
	public String getBaseDN() {
		return baseDN;
	}

	public void setBaseDN(String baseDN) {
		this.baseDN = baseDN;
	}

	public OdmManager getManager() {
		return manager;
	}

	public void setManager(OdmManager manager) {
		this.manager = manager;
	}
	private OdmManager manager;
	
	public HistoricalOrgUnitsEntry getHistorialOrgUnitByUid(String eruid) {
			HistoricalOrgUnitsEntry hou = getHistoricalOrgUnitEntry(eruid);
			if(hou!=null)
				return hou;
		return createEmptyHistorialOrgUnitByUid(eruid);
	}
	
	public HistoricalOrgUnitsEntry createEmptyHistorialOrgUnitByUid(String eruid){
		HistoricalOrgUnitsEntry hou = new HistoricalOrgUnitsEntry();
		hou.setEruid(eruid);
		String erglobalid = String.valueOf(Math.abs(uuid.randomUUID().getMostSignificantBits()));
	    hou.setDistinguisedName(new DistinguishedName("erglobalid="+erglobalid+","+baseDN));
	    manager.create(hou);
		return hou;
	}
	public void save(HistoricalOrgUnitsEntry hou){
		manager.update(hou);
	}
	public void addHistoricalOrgUnit(String eruid,String orgUnit){
		HistoricalOrgUnitsEntry hou = getHistorialOrgUnitByUid(eruid);
		hou.setHistoricalorgunits(Arrays.asList(orgUnit));
		manager.update(hou);
	}
	
	public String getLastOrgUnit(String eruid){
		try{
			return getHistoricalOrgUnitEntry(eruid).getHistoricalorgunits().get(0);
		}catch(NamingException ex){
			return null;
		}
	}
	
	public HistoricalOrgUnitsEntry getHistoricalOrgUnitEntry(String eruid){
		SearchControls sr = new SearchControls();
		sr.setCountLimit(1L);
		sr.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		sr.setTimeLimit(60000);
		List<HistoricalOrgUnitsEntry> list = manager.search(HistoricalOrgUnitsEntry.class,new DistinguishedName(baseDN),"(eruid="+eruid+")", sr);
		if(list.isEmpty())
			return null;
		return list.iterator().next();
		
		//return manager.read(HistoricalOrgUnitsEntry.class,new DistinguishedName("eruid="+eruid+","+baseDN));
		
}

}
