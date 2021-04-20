package com.example.securingweb.controller;

import com.example.securingweb.model.ARESClient;
import com.example.securingweb.model.Address;
import com.example.securingweb.model.Client;
import com.example.securingweb.model.RestResponse;
import com.example.securingweb.service.XMLLoaderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RESTClientARESController {

    private XMLLoaderService xmlLoader = new XMLLoaderService();

    @RequestMapping("client/getClientFromICO/{clientICO}")
    public RestResponse clientInfo(@PathVariable String clientICO) {
        RestResponse response = new RestResponse();
        String aresURL = "https://wwwinfo.mfcr.cz/cgi-bin/ares/darv_std.cgi?ico=" + clientICO;
        Client client = null;
        Optional<ARESClient> returnedClient = xmlLoader.loadDataFor(aresURL);
        if (returnedClient != null){
            client = new Client(returnedClient.get());
            response.setResponseStatus(RestResponse.OK);
            response.setResponse(client);
        }else{
            response.setResponseStatus(RestResponse.NOT_FOUND);
            response.setResponse("");
        }

        return response;
    }

    @RequestMapping("address/ad/getClientAddressFromICO/{clientICO}")
    public RestResponse clientAddress(@PathVariable String clientICO) {
        RestResponse response = new RestResponse();
        String aresURL = "https://wwwinfo.mfcr.cz/cgi-bin/ares/darv_std.cgi?ico=" + clientICO;
        Address address = null;
        Optional<ARESClient> returnedClient = xmlLoader.loadDataFor(aresURL);
        if (returnedClient != null){
            address = new Address(returnedClient.get());
            response.setResponseStatus(RestResponse.OK);
            response.setResponse(address);
        }else{
            response.setResponseStatus(RestResponse.NOT_FOUND);
            response.setResponse("");
        }

        return response;
    }
}
