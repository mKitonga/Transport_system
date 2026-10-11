package com.transport.Sacco.SaccoDriver.Service;

import com.transport.Sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.Sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.stereotype.Service;

@Service
public class SaccoDriverReadService extends BaseJpaRepoReadService<SaccoDriver, SaccoDriverRepository> {

    public SaccoDriver findActiveByEntityIdForUpdate(String driverId) {
        return repository.findActiveByEntityIdForUpdate(driverId)
                .orElseThrow(() -> new CommonRuntimeException(
                        ExceptionType.NOT_FOUND,
                        "error.entity.not.found"
                ));
    }
}
