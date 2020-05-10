package com.solution.eventsmanager.representation;

import java.util.List;

public class TemplateRepresentation {
    private String requestor;
    private String name;
    private List<AttributeRepresentation> attributes;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    private String template;

    public List<AttributeRepresentation> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeRepresentation> attributes) {
        this.attributes = attributes;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TemplateRepresentation{" +
                "requestor='" + requestor + '\'' +
                ", name='" + name + '\'' +
                ", attributes=" + attributes +
                ", template='" + template + '\'' +
                '}';
    }
}
