package com.solution.eventsmanager.representation;

import javax.persistence.*;

public class UsersEventsTemplateRepresentation {
    private String id;
    private String userId;
    private String eventId;
    private String templateId;
    private TemplateRepresentation template;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public String toString() {
        return "UsersEventsTemplateRepresentation{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", templateId='" + templateId + '\'' +
                ", template=" + template +
                '}';
    }
}
