package org.apromore.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class ApromoreConfig{
	@Bean
	public HttpComponentsMessageSender myHttpId() {
		HttpComponentsMessageSender h = new HttpComponentsMessageSender();
		h.setConnectionTimeout(120000);
		return h;
	}

	@Bean
	public Jaxb2Marshaller myMarshallerId() {
		Jaxb2Marshaller j = new Jaxb2Marshaller();
		j.setContextPath("org.apromore.model");
		return j;
	}

	@Bean
	public SaajSoapMessageFactory myMessageFactoryId() {
		SaajSoapMessageFactory s = new SaajSoapMessageFactory();
		s.setSoapVersion(org.springframework.ws.soap.SoapVersion.SOAP_11);
		return s;
	}
}