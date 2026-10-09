package com.transport.Routes.Service;

import com.transport.Routes.Entity.Terminal;
import com.transport.Routes.Form.EditTerminalForm;
import com.transport.Routes.Form.RegisterTerminalForm;
import com.transport.Routes.Repository.TerminalRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TerminalEditService extends BaseJpaRepoEditService<Terminal, TerminalRepository> {

    private RouteReadService routeReadService;

    @Transactional
    public Terminal registerTerminal(RegisterTerminalForm form) {
        var terminal = new Terminal();
        terminal.setName(form.getName().trim());
        terminal.setCreatedByEntityId(form.getSessionUserId());

        terminal = save(terminal, form.getSessionUserId());
        return terminal;
    }

    @Transactional
    public Terminal editTerminal(String terminalId, EditTerminalForm form) {
        var terminal = findByEntityId(terminalId);
        terminal.setName(form.getName().trim());

        terminal = save(terminal, form.getSessionUserId());

        return terminal;
    }

    @Transactional
    public void deleteTerminal(String terminalId, String sessionUserId) {
        var terminal = findByEntityId(terminalId);
        boolean inUse = routeReadService.isTerminalInUse(terminal.getEntityId());
        if (inUse) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.terminal.in.use");
        }
        delete(terminal, sessionUserId);
    }

    @Autowired
    public void setRouteReadService(RouteReadService routeReadService) {
        this.routeReadService = routeReadService;
    }
}
