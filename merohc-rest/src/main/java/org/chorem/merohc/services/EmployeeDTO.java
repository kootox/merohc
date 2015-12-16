package org.chorem.merohc.services;

import org.chorem.merhoc.entities.Employee;

/**
 * Created by couteau on 15/12/15.
 */
public class EmployeeDTO {

    protected String id;
    protected String firstName;
    protected String lastName;

    public EmployeeDTO(Employee employee){
        super();
        this.id=employee.getTopiaId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
