package com.leverx.learningmanagementsystem.btp.xsuaa.controller;

import com.leverx.learningmanagementsystem.btp.xsuaa.model.XsuaaResponseDto;
import com.leverx.learningmanagementsystem.btp.xsuaa.service.XsuaaService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/application-info")
@AllArgsConstructor
@Profile("cloud")
public class ApplicationInfoController {

    private final XsuaaService xsuaaService;

    @GetMapping
    public XsuaaResponseDto getXsuaaConfiguration() {
        return xsuaaService.getXsuaaInfo();
    }
}
