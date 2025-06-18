package com.leverx.learningmanagementsystem.btp.appinfo.controller;

import com.leverx.learningmanagementsystem.btp.appinfo.model.ApplicationInfoDto;
import com.leverx.learningmanagementsystem.btp.appinfo.service.ApplicationInfoService;
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

    private final ApplicationInfoService applicationInfoService;

    @GetMapping
    public ApplicationInfoDto getApplicationInfo() {
        return applicationInfoService.getApplicationInfo();
    }
}
