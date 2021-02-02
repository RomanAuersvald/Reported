package com.example.securingweb.service;

import com.example.securingweb.model.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.HashMap;
import java.util.Map;

@Service
public class InvoiceService {

    public String parseThymeleafTemplate(Invoice invoice, Address clientAddress, Address userAddress) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("invoiceNumber", invoice.getId());
        context.setVariable("created", invoice.getCreated());
        context.setVariable("dueBy", invoice.getDueBy());

        context.setVariable("userName", invoice.getUser().getNiceNameAndLastname());
        context.setVariable("userStreet", userAddress.getStreet());
        context.setVariable("userCity", userAddress.getCity());
        context.setVariable("userPostCode", userAddress.getPostCode());
        context.setVariable("userBankAccount", invoice.getUser().getBankAccount());

        context.setVariable("clientName", invoice.getClient().getCompanyName());
        context.setVariable("clientStreet", clientAddress.getStreet());
        context.setVariable("clientCity",clientAddress.getCity());
        context.setVariable("clientPostCode", clientAddress.getPostCode());

        Map<ProjectTask, Double> taskPrice = new HashMap<>();
        Double totalPrice = 0.0;
        for (ProjectTask task: invoice.getTasks()){
            Long duration = task.getTaskDuration();
            Double hourRate = task.getHourRate();
            Double price = hourRate * (duration / 3600);
            taskPrice.put(task, price);
            totalPrice += price;
        }


        context.setVariable("tasks", taskPrice);
        context.setVariable("totalPrice", totalPrice);

        return templateEngine.process("/templates/invoice/invoicePDF", context);
    }
}
