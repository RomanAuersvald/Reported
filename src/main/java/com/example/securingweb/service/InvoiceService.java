package com.example.securingweb.service;

import com.example.securingweb.model.Address;
import com.example.securingweb.model.Invoice;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
public class InvoiceService {

    public String parseThymeleafTemplate(Invoice invoice, Address clientAddress) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("created", invoice.getCreated());
        context.setVariable("dueBy", invoice.getDueBy());

        context.setVariable("userStreet", invoice.getUser().getUsername());
        context.setVariable("userCity", invoice.getDueBy());
        context.setVariable("userPostCode", invoice.getDueBy());

        context.setVariable("clientStreet", clientAddress.getStreet());
        context.setVariable("clientCity",clientAddress.getCity());
        context.setVariable("clientPostCode", clientAddress.getPostCode());

        context.setVariable("tasks", invoice.getTasks());

        return templateEngine.process("/templates/invoice/invoicePDF", context);
    }
}
