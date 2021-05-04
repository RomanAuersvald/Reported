package com.example.securingweb.service;

import com.example.securingweb.model.ARESClient;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Optional;

@Service
public class XMLLoaderService {

    private Unmarshaller jaxbUnmarshaller;
    private JAXBContext jaxbContext;
    private RestTemplate restTemplate;
    private XMLInputFactory factory;

    public XMLLoaderService(){
        {
            try {
                jaxbContext = JAXBContext.newInstance(ARESClient.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }

        RestTemplateBuilder builder = new RestTemplateBuilder();
        restTemplate = builder.build();

        factory = XMLInputFactory.newInstance(); // Or newFactory()
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false); // this is the magic line

    }

    public Optional<ARESClient> loadDataFor( String fromURL){
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8"))); // správné načítání diakritiky
        String xmlData = restTemplate.getForObject(fromURL, String.class);
        InputStream targetStream = new ByteArrayInputStream(xmlData.getBytes());
        Reader reader = null;
        try {
            reader = new InputStreamReader(targetStream, "UTF-8"); // nakonec k ničemu
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XMLStreamReader xmlReader = null;

        try {
            xmlReader = factory.createXMLStreamReader(reader);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return null;
        }

        ARESClient xmlResponse = null;
        try {
            xmlResponse = (ARESClient) jaxbUnmarshaller.unmarshal(xmlReader);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
        if (xmlResponse.getPocetZaznamu() == 0){
            // nenašla se shoda
            return null;
        }else{
            return Optional.ofNullable(xmlResponse);
        }

    }
}
