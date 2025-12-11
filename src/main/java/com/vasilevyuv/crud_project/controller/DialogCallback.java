package com.vasilevyuv.crud_project.controller;

import com.vasilevyuv.crud_project.model.Entity;

public interface DialogCallback {
    void onEntityCreated(Entity entity);
    void onEntityUpdated(Entity entity);
    void onDialogClosed();
}