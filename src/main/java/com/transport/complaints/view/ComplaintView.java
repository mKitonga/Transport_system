package com.transport.complaints.view;

import com.transport.complaints.entity.ComplaintService;
import com.transport.complaints.entity.ComplaintStatus;
import com.transport.complaints.entity.Complaints;
import com.transport.liby.view.BaseView;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ComplaintView extends BaseView<Complaints> {

    public ComplaintView(Complaints entity) { super(entity); }

    public ComplaintService getService() {return entity.getService();}

    public String getComplaintMessage() {return entity.getComplaintMessage();}

    public String getResponseMessage() {return entity.getResponseMessage();}

    public String getPassengerPhoneNumber() {return entity.getPassengerPhoneNumber();}

    public String getPassengerEmail() {return entity.getPassengerEmail();}

    public String getRouteId() {return entity.getRoute() != null ? entity.getRoute().getEntityId() : null;}

    public String getRouteName() {return entity.getRoute() != null ? entity.getRoute().getName() : null;}

    public ComplaintStatus getStatus() {return entity.getStatus();}

    public String getSaccoId() {return entity.getSacco() != null ? entity.getSacco().getEntityId() : null;}

    public String getSaccoName() {return entity.getSacco() != null ? entity.getSacco().getName() : null;}

    public String getSchoolId() {return entity.getSchool() != null ? entity.getSchool().getEntityId() : null;}

    public String getSchoolName() {return entity.getSchool() != null ? entity.getSchool().getName() : null;}

    public String getLocation() {return entity.getSchool() != null ? entity.getSchool().getLocation() : null;}

    public String getRespondedByEntityId() {return entity.getRespondedByEntityId();}

    public LocalDateTime getRespondedAt() {return entity.getRespondedAt();}
}
