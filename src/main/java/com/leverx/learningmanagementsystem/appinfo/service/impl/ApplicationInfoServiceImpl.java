package com.leverx.learningmanagementsystem.appinfo.service.impl;

import com.leverx.learningmanagementsystem.btp.xsuaa.model.XsuaaProperties;
import com.leverx.learningmanagementsystem.appinfo.model.ApplicationInfoDto;
import com.leverx.learningmanagementsystem.appinfo.service.ApplicationInfoService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile("cloud")
public class ApplicationInfoServiceImpl implements ApplicationInfoService {

    private XsuaaProperties xsuaaProperties;

    @Override
    public ApplicationInfoDto get() {
        return new ApplicationInfoDto(
                xsuaaProperties.getUrl(),
                xsuaaProperties.getClientId(),
                xsuaaProperties.getClientSecret()
        );
    }
}
