package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.HospitalAppointmentServiceTypeHistory;
import com.cogent.cogentappointment.persistence.model.HospitalAppointmentServiceType;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author smriti on 26/05/20
 */
public class HospitalAppointmentServiceTypeEntityListener {

    @PrePersist
    public void prePersist(HospitalAppointmentServiceType target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(HospitalAppointmentServiceType target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(HospitalAppointmentServiceType target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(HospitalAppointmentServiceType target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new HospitalAppointmentServiceTypeHistory(target, action));
    }
}
