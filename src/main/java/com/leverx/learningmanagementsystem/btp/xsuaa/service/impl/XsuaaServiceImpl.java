package com.leverx.learningmanagementsystem.btp.xsuaa.service.impl;

import com.leverx.learningmanagementsystem.btp.xsuaa.config.XsuaaConfiguration;
import com.leverx.learningmanagementsystem.btp.xsuaa.model.XsuaaResponseDto;
import com.leverx.learningmanagementsystem.btp.xsuaa.service.XsuaaService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Profile("cloud")
public class XsuaaServiceImpl implements XsuaaService {

    private XsuaaConfiguration xsuaaConfiguration;

    @Override
    public XsuaaResponseDto getXsuaaInfo() {
        return new XsuaaResponseDto(
                xsuaaConfiguration.getUrl(),
                xsuaaConfiguration.getClientId(),
                xsuaaConfiguration.getClientSecret()
        );
    }
}
