package org.woped.core.model.bpel;

import java.util.HashSet;
import java.util.Iterator;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLinks;

/**
 *
 * @author Frank Schüler
 *
 */
public class PartnerlinkList {

	private HashSet<Partnerlink> _list;

	public PartnerlinkList() {
		this._list = new HashSet<Partnerlink>();
	}

	/**
	 * Insert a partnerlink to a consisting list of partnerlinks
	 *
	 * @param name
	 * @param namespace
	 * @param partnerLinkType
	 * @param partnerRole
	 * @param WsdlUrl
	 */
	public void setPartnerLink(String name, String namespace,
			String partnerLinkType, String partnerRole, String WsdlUrl) {
		Partnerlink p = new Partnerlink(WsdlUrl);
		p.setPartnerLink(name, namespace, partnerLinkType, partnerRole);
		this._list.add(p);
	}

	/**
	 * Insert a partnerlink to a consisting list of partnerlinks Attention:
	 * Parameters: name, namespace, partnerLinkType, partnerRole, myRole
	 *
	 * @param name
	 * @param namespace
	 * @param partnerLinkType
	 * @param partnerRole
	 * @param myRole
	 * @param WsdlUrl
	 */
	public void addPartnerLink(String name, String namespace,
			String partnerLinkType, String partnerRole, String myRole,
			String WsdlUrl) {
		Partnerlink p = new Partnerlink(WsdlUrl);
		p.addPartnerLink(name, namespace, partnerLinkType, partnerRole, myRole);
		this._list.add(p);
	}


	/**
	 * Insert a partnerlink to a consisting list of partnerlinks Attention:
	 * Parameters: name, namespace, partnerLinkType, myRole (without partnerRole)
	 *
	 * @param name
	 * @param namespace
	 * @param partnerLinkType
	 * @param myRole
	 * @param WsdlUrl
	 */
	public void addPartnerLinkWithoutPartnerRole(String name, String namespace,
			String partnerLinkType, String myRole, String WsdlUrl) {
		Partnerlink p = new Partnerlink(WsdlUrl);
		p.addPartnerLinkWithoutPartnerRole(name, namespace, partnerLinkType,
				myRole);
		this._list.add(p);
	}

	public void addPartnerLinkWithoutMyRole(String name, String namespace,
			String partnerLinktType, String partnerRole, String WsdlUrl) {
		Partnerlink p = new Partnerlink(WsdlUrl);
		p.addPartnerLinkWithoutMyRole(name, namespace, partnerLinktType, partnerRole);
		this._list.add(p);
	}

	/**
	 *
	 * @return
	 */
	public String[] getPartnerlinkNameArray()
	{
		String[] list = new String[this._list.size()];
		Iterator<Partnerlink> iter = this._list.iterator();
		int i = 0;
		while(iter.hasNext())
		{
			list[i] = iter.next().toString();
			i++;
		}
		return list;
	}

	/**
	 *
	 * @return
	 */
	private TPartnerLinks genPartnerLinks() {
		return TPartnerLinks.Factory.newInstance();
	}

	/**
	 *
	 * @return
	 */
	public TPartnerLinks getBpelCode() {
		TPartnerLinks list = this.genPartnerLinks();
		Iterator<Partnerlink> iter = this._list.iterator();
		while (iter.hasNext()) {
			list.addNewPartnerLink().set(iter.next().getTPartnerlink());
		}
		return list;
	}

	
	public String[] getWsdlUrls(){
		String[] urls = new String[this._list.size()];
		Iterator<Partnerlink> iter = this._list.iterator();
		for(int i=0;iter.hasNext();i++){
			urls[i]=iter.next().getWsdlUrl();
		}
		return urls;
	}
	
	/**
	 *
	 * @param Name
	 */
	public void removePartnerlinkByName(String Name)
	{
		//TODO fertigschreiben
	}

	public HashSet<Partnerlink> getPartnerlinkList() {
		return this._list;
	}
}
