package com.transport.sacco.SaccoAdmin.Service;

import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.stereotype.Service;

@Service
public class SaccoAdminEditService extends BaseJpaRepoEditService<SaccoAdmin, SaccoAdminRepository> {
}
