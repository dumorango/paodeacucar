package br.com.companhiadesistemas.itim.gpa.gpaoffice.customizations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.naming.directory.SearchControls;

import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.odm.core.OdmManager;

import org.springframework.ldap.odm.typeconversion.impl.converters.FromStringConverter;
import com.ibm.itim.dataservices.model.domain.Account;

public class HistoricalOrgsManager {
	
	private String baseDN;
	private UUID uuid = new UUID(4242L, 4242L);
	private SearchControls searchControls; 
	private OdmManager manager;

	public void addHistoricalOrgUnit(Account account,String orgUnit){
		HistoricalOrgUnitsEntry hou = getOrCreateHistorialOrgUnit(account.getDistinguishedName().getAsString());
		hou.setHistoricalorgunits(Arrays.asList(orgUnit));
		manager.update(hou);
	}

	public HistoricalOrgUnitsEntry createEmptyHistorialOrgUnitByUid(String dn){
		HistoricalOrgUnitsEntry hou = new HistoricalOrgUnitsEntry();
		hou.setErAccountReference(dn);
		String erglobalid = String.valueOf(Math.abs(uuid.randomUUID().getMostSignificantBits()));
	    hou.setDistinguisedName(new DistinguishedName("erglobalid="+erglobalid+","+baseDN));
	    manager.create(hou);
		return hou;
	}

	public String getBaseDN() {
		return baseDN;
	}
	public HistoricalOrgUnitsEntry getOrCreateHistorialOrgUnit(String dn) {
			HistoricalOrgUnitsEntry hou = getHistoricalOrgUnitEntry(dn);
			if(hou!=null)
				return hou;
		return createEmptyHistorialOrgUnitByUid(dn);
	}
	
	public HistoricalOrgUnitsEntry getHistoricalOrgUnitEntry(String dn){
		SearchControls sr = new SearchControls();
		sr.setCountLimit(0L);
		sr.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		sr.setTimeLimit(60000);
		//List<HistoricalOrgUnitsEntry> list = manager.search(HistoricalOrgUnitsEntry.class,new DistinguishedName(baseDN),"(eruid="+eruid+")", sr);
		List<HistoricalOrgUnitsEntry> list = manager.search(HistoricalOrgUnitsEntry.class,new DistinguishedName(baseDN),"(erAccountReference="+dn+")", sr);
		if(list.isEmpty())
			return null;
		return list.iterator().next();	
}
	
	public String getLastOrgUnit(String dn){
		try{
			HistoricalOrgUnitsEntry hou = getHistoricalOrgUnitEntry(dn);
			if(hou!=null)
				return hou.getHistoricalorgunits().get(0);
		}catch(NamingException ex){
			
		}
		return null;
	}
	public OdmManager getManager() {
		return manager;
	}
	public void save(HistoricalOrgUnitsEntry hou){
		manager.update(hou);
	}
	
	public void setBaseDN(String baseDN) {
		this.baseDN = baseDN;
	}
	
	public void setManager(OdmManager manager) {
		this.manager = manager;
	}

}
