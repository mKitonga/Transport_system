package com.transport.routes.View;

import com.transport.routes.Entity.Terminal;
import com.transport.liby.view.BaseView;

public class TerminalView extends BaseView<Terminal> {

    public TerminalView(Terminal entity) {super(entity);}

    public String getName() {
        return entity.getName();
    }
}
