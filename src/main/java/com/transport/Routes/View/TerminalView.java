package com.transport.Routes.View;

import com.transport.Routes.Entity.Terminal;
import com.transport.liby.view.BaseView;

public class TerminalView extends BaseView<Terminal> {

    public TerminalView(Terminal entity) {super(entity);}

    public String getName() {
        return entity.getName();
    }
}
