package com.paralex.erp.services;

import com.paralex.erp.repositories.ServiceProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
@Log4j2
public class ServiceProviderProfileService {
    private final ServiceProviderProfileRepository serviceProviderProfileRepository;
}
