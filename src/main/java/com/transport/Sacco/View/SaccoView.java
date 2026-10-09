package com.transport.Sacco.View;

import com.transport.Sacco.Entity.Sacco;
import com.transport.liby.view.BaseView;

public class SaccoView extends BaseView<Sacco> {
    public SaccoView(Sacco entity) {super(entity);}

    public String getName(){
        return entity.getName();
    }
}
