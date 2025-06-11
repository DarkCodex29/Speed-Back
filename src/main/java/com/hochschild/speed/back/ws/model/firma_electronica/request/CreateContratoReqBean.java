package com.hochschild.speed.back.ws.model.firma_electronica.request;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

public @Data
class CreateContratoReqBean implements Serializable {

    private String title;
    private String language;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isUserEmailNotifications() {
        return userEmailNotifications;
    }

    public void setUserEmailNotifications(boolean userEmailNotifications) {
        this.userEmailNotifications = userEmailNotifications;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public HocDocInfoReqBean getHocDocInfo() {
        return hocDocInfo;
    }

    public void setHocDocInfo(HocDocInfoReqBean hocDocInfo) {
        this.hocDocInfo = hocDocInfo;
    }

    public List<DocumentReqBean> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentReqBean> documents) {
        this.documents = documents;
    }

    public List<UserReqBean> getUsers() {
        return users;
    }

    public void setUsers(List<UserReqBean> users) {
        this.users = users;
    }

    public FlagsBean getFlags() {
        return flags;
    }

    public void setFlags(FlagsBean flags) {
        this.flags = flags;
    }
    private boolean userEmailNotifications;
    private String templateId;
    private String reference;
    private HocDocInfoReqBean hocDocInfo;
    private List<DocumentReqBean> documents;
    private List<UserReqBean> users;
    private FlagsBean flags;

}
