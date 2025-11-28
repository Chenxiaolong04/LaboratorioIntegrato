package com.immobiliaris.demo.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        System.out.println("\n!!! CUSTOM ERROR CONTROLLER !!!");
        System.out.println("Status: " + request.getAttribute("javax.servlet.error.status_code"));
        System.out.println("URI: " + request.getAttribute("javax.servlet.error.request_uri"));
        System.out.println("Message: " + request.getAttribute("javax.servlet.error.message"));
        System.out.println("!!! FINE ERROR CONTROLLER !!!\n");
        // Mostra pagina di errore personalizzata
        return "error";
    }
}
