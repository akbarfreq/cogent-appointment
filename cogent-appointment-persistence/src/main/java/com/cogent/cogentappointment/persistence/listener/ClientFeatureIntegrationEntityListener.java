package com.cogent.cogentappointment.persistence.listener;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.history.ClientFeatureIntegrationHistory;
import com.cogent.cogentappointment.persistence.model.ClientFeatureIntegration;
import com.cogent.cogentappointment.persistence.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;

import static com.cogent.cogentappointment.persistence.config.Action.*;
import static javax.transaction.Transactional.TxType.MANDATORY;

/**
 * @author rupak on 2020-05-18
 */
public class ClientFeatureIntegrationEntityListener {

    @PrePersist
    public void prePersist(ClientFeatureIntegration target) {
        perform(target, INSERTED);
    }

    @PreUpdate
    public void preUpdate(ClientFeatureIntegration target) {
        perform(target, UPDATED);
    }

    @PreRemove
    public void preRemove(ClientFeatureIntegration target) {
        perform(target, DELETED);
    }

    @Transactional(MANDATORY)
    public void perform(ClientFeatureIntegration target, Action action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new ClientFeatureIntegrationHistory(target, action));
    }
}
