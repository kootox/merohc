package org.chorem.merohc.bean;

import org.chorem.merohc.entities.Project;

public class ProjectDTO {
    protected String name;
    protected String id;

    public ProjectDTO(){}

    public ProjectDTO(Project project){
        super();
        this.name = project.getName();
        this.id = project.getTopiaId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
