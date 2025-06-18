package com.leverx.learningmanagementsystem.btp.appinfo.service.impl;

import com.leverx.learningmanagementsystem.btp.appinfo.config.ApplicationInfoConfiguration;
import com.leverx.learningmanagementsystem.btp.appinfo.model.ApplicationInfoDto;
import com.leverx.learningmanagementsystem.btp.appinfo.service.ApplicationInfoService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile("cloud")
public class ApplicationInfoServiceImpl implements ApplicationInfoService {

    private ApplicationInfoConfiguration applicationInfoConfiguration;

    @Override
    public ApplicationInfoDto getApplicationInfo() {
        return new ApplicationInfoDto(
                applicationInfoConfiguration.getUrl(),
                applicationInfoConfiguration.getClientId(),
                applicationInfoConfiguration.getClientSecret()
        );
    }
}
