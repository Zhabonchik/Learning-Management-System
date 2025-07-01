package com.leverx.learningmanagementsystem.appinfo.controller;

import com.leverx.learningmanagementsystem.appinfo.model.ApplicationInfoDto;
import com.leverx.learningmanagementsystem.appinfo.service.ApplicationInfoService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('Administrate')")
    public ApplicationInfoDto getApplicationInfo() {
        return applicationInfoService.get();
    }
}
