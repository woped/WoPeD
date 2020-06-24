package org.woped.editor.controller.bpel;

import inspireit.uddi.base.Name;
import inspireit.uddi.base.UDDIException;
import inspireit.uddi.collections.BusinessInfos;
import inspireit.uddi.collections.FindQualifiers;
import inspireit.uddi.collections.Names;
import inspireit.uddi.collections.ServiceInfos;
import inspireit.uddi.request.UDDIProfile;
import inspireit.uddi.request.UDDIQuerier;
import inspireit.uddi.request.base.FindQualifier;
import inspireit.uddi.response.BusinessList;
import inspireit.uddi.response.base.BusinessInfo;
import inspireit.uddi.response.base.ServiceInfo;

/**
 * @author Alexander Roßwog
 *
 * This is the basic utility-class to connect a UDDI-server.
 * It contains methods to find businesses and their according offered services.
 *
 * Created on 10.02.2008
 */

public class UDDI 
{
	public static String[] find_business(String uddiUrl, String businessName)
	{
		String[] buslist = null;
		//profile.setQueryURL(new URL(queryURL));

		//To configure a profile, enable the appropriate set of properties like in the following examples:
		//define the appropriate UDDI profile
		UDDIProfile profile = new UDDIProfile();

		//Sets the inquiry URL to Microsoft's UBR one, http://uddi.microsoft.com/inquire .
		profile.setProperty("inspireit.uddi.inquiry.url",uddiUrl);

		//Sets the UDDI version in use to UDDI 3.0.
		profile.setProperty("inspireit.uddi.version","2.0");
		
		UDDIQuerier querier = UDDIQuerier.getQuerier(profile);

		try {
	         // find max. 40 businesses starting with "BusinessName", sorted by name descending
	         int max = 40;
	         Name name = new Name(businessName);
	         FindQualifiers findQualifiers = new FindQualifiers();
	         //findQualifiers.add(FindQualifier.sortByNameDesc);
	         findQualifiers.add(FindQualifier.sortByNameAsc);
	         
	         // the following is required for UDDI V3 and will be ignored if another UDDI version is used
	         findQualifiers.add(FindQualifier.approximateMatch);

	         BusinessList businessList   = querier.findBusiness(name, findQualifiers, max);
	         BusinessInfos businessInfos = businessList.getBusinessInfos();
	         BusinessInfo businessInfo[] = businessInfos.toArray();
	         
	         if(businessInfo.length > 0)
	         {
	        	 buslist = new String[businessInfo.length];
	         }

             for(int i=0; i< businessInfo.length; i++) {
            	 BusinessInfo bi = businessInfo[i];
            	 Names names  = bi.getNames();
            	 Name _name[] = names.toArray();
            	 buslist[i] = _name[0] + "";
             }
                          
		} 
		catch(UDDIException e) 
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return buslist;
	}
	public static BusinessInfo getBusinessInfoFromName(String uddiUrl, String businessName)
	{
		BusinessInfo bi = null;
		//profile.setQueryURL(new URL(queryURL));

		//To configure a profile, enable the appropriate set of properties like in the following examples:
		//define the appropriate UDDI profile
		UDDIProfile profile = new UDDIProfile();

		//Sets the inquiry URL to Microsoft's UBR one, http://uddi.microsoft.com/inquire .
		profile.setProperty("inspireit.uddi.inquiry.url",uddiUrl);

		//Sets the UDDI version in use to UDDI 3.0.
		profile.setProperty("inspireit.uddi.version","2.0");
		
		UDDIQuerier querier = UDDIQuerier.getQuerier(profile);

		try {
	         // find max. 1 businesses
	         int max = 1;
	         Name name = new Name(businessName);
	         FindQualifiers findQualifiers = new FindQualifiers();
	         findQualifiers.add(FindQualifier.sortByNameDesc);

	         // the following is required for UDDI V3 and will be ignored if another UDDI version is used
	         findQualifiers.add(FindQualifier.approximateMatch);

	         BusinessList businessList   = querier.findBusiness(name, findQualifiers, max);
	         BusinessInfos businessInfos = businessList.getBusinessInfos();
	         BusinessInfo businessInfo[] = businessInfos.toArray();
	         
	         if(businessInfo.length > 0)
	         {
	        	 bi = businessInfo[0];
	         }
	         return bi;
		} 
		catch(UDDIException e) 
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return bi;
	}
	
	public static String[] find_services(BusinessInfo bi)
	{
		String[] servlist = null;
		
		if(bi != null)
		{
			ServiceInfos serviceInfos = bi.getServiceInfos();
			ServiceInfo serviceInfo[] = serviceInfos.toArray();
			
			if(serviceInfo != null)
			{
				servlist = new String[serviceInfo.length];
				for(int i=0; i<servlist.length; i++)
				{
					ServiceInfo si = serviceInfo[i];
					Names names = si.getNames();
					Name _name[] = names.toArray();
					servlist[i] = _name[0] + "";
				}
			}
		}
		
		return servlist;
	}
	
	public static String getWsdlFromService(String uddiUrl, ServiceInfo serviceInfo)
	{
		String wsdl = null;
		
		//profile.setQueryURL(new URL(queryURL));

		//To configure a profile, enable the appropriate set of properties like in the following examples:
		//define the appropriate UDDI profile
		UDDIProfile profile = new UDDIProfile();

		//Sets the inquiry URL to Microsoft's UBR one, http://uddi.microsoft.com/inquire .
		profile.setProperty("inspireit.uddi.inquiry.url",uddiUrl);

		//Sets the UDDI version in use to UDDI 3.0.
		profile.setProperty("inspireit.uddi.version","2.0");
		
		//UDDIQuerier querier = UDDIQuerier.getQuerier(profile);
		
		try
		{
	         FindQualifiers findQualifiers = new FindQualifiers();
	         findQualifiers.add(FindQualifier.sortByNameDesc);

	         // the following is required for UDDI V3 and will be ignored if another UDDI version is used
	         findQualifiers.add(FindQualifier.approximateMatch);
	        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return wsdl;
	}
}
