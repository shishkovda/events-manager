package com.solution.eventsmanager.representation;

import javax.persistence.Column;

public class EventRepresentation {
    private String title;
    private String date;
    private String assignee;

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getAssignee() {
        return assignee;
    }

    @Override
    public String toString() {
        return "EventRepresentation{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", assignee='" + assignee + '\'' +
                '}';
    }
}
