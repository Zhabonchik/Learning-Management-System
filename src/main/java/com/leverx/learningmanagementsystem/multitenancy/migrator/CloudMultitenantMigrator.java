package com.leverx.learningmanagementsystem.multitenancy.migrator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Profile("cloud")
@Slf4j
public class CloudMultitenantMigrator {
    // TODO
}
