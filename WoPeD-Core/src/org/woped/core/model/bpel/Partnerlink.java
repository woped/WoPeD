package org.woped.core.model.bpel;

import javax.xml.namespace.QName;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;

public class Partnerlink {

	private TPartnerLink partnerLink;

	private String wsdlurl;

	public Partnerlink() {
		this.partnerLink = this.genPartnerLink();
	}

	public Partnerlink(String WsdlUrl) {
		this();
		this.wsdlurl = WsdlUrl;
	}

	private TPartnerLink genPartnerLink() {
		return TPartnerLink.Factory.newInstance();
	}

	/**
	 * Insert a partnerlink to a consisting list of partnerlinks
	 * 
	 * @param name
	 * @param namespace
	 * @param partnerLinkType
	 * @param partnerRole
	 */
	public void setPartnerLink(String name, String namespace,
			String partnerLinkType, String partnerRole) {
		this.partnerLink.setName(name);
		this.partnerLink.setPartnerLinkType(new QName(namespace,
				partnerLinkType));
		this.partnerLink.setPartnerRole(partnerRole);
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
	 */
	public void addPartnerLink(String name, String namespace,
			String partnerLinkType, String partnerRole, String myRole) {
		this.partnerLink.setName(name);
		this.partnerLink.setPartnerLinkType(new QName(namespace,
				partnerLinkType));
		this.partnerLink.setPartnerRole(partnerRole);
		this.partnerLink.setMyRole(myRole);
	}

	/**
	 * Insert a partnerlink to a consisting list of partnerlinks Attention:
	 * Parameters: name, namespace, partnerLinkType, myRole (without partnerRole
	 * 
	 * @param name
	 * @param namespace
	 * @param partnerLinkType
	 * @param myRole
	 */
	public void addPartnerLinkWithoutPartnerRole(String name, String namespace,
			String partnerLinkType, String myRole) {
		this.partnerLink.setName(name);
		this.partnerLink.setPartnerLinkType(new QName(namespace,
				partnerLinkType));
		this.partnerLink.setMyRole(myRole);
	}

	/**
	 * Insert a partnerlink to a consisting list of partnerlinks Attention:
	 * Parameters: name, namespace, partnerLinkType, partnerRole
	 * 
	 * @param name
	 * @param namespace
	 * @param partnerLinkType
	 * @param partnerRole
	 */
	public void addPartnerLinkWithoutMyRole(String name, String namespace,
			String partnerLinkType, String partnerRole) {
		this.partnerLink.setName(name);
		this.partnerLink.setPartnerLinkType(new QName(namespace,
				partnerLinkType));
		this.partnerLink.setPartnerRole(partnerRole);
	}
	

	public String Namespace()
	{
		return this.partnerLink.getPartnerLinkType().getNamespaceURI();
	}
	
	public String getPartnerlinkType()
	{
		return this.partnerLink.getPartnerLinkType().getLocalPart();
	}
		
	public String getName() {
		return this.partnerLink.getName();
	}

	public QName getPartnerlinkTypeByQName() {
		return this.partnerLink.getPartnerLinkType();
	}

	public String getPartnerlinkRole() {
		return this.partnerLink.getPartnerRole();
	}

	public String getMyRole() {
		return this.partnerLink.getMyRole();
	}

	/**
	 * 
	 * @param URL
	 */
	public void setWsdlUrl(String URL) {
		this.wsdlurl = URL;
	}

	/**
	 * 
	 * @return
	 */
	public String getWsdlUrl() {
		return this.wsdlurl;
	}

	/**
	 * 
	 * @return
	 */
	public TPartnerLink getTPartnerlink() {
		return this.partnerLink;
	}

	/**
	 * @return String
	 */
	public String toString() {
		return this.partnerLink.getName();
	}

	/**
	 * 
	 * @return
	 */
	public boolean equals(Object obj) {
		if (!Partnerlink.class.isInstance(obj))
			return false;
		if (this.toString().compareToIgnoreCase(((Partnerlink) obj).toString()) != 0)
			return false;
		return true;
	}

}
