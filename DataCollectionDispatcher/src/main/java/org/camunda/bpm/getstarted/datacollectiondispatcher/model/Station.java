package org.camunda.bpm.getstarted.datacollectiondispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Lombok is used to generate code such as getters, setters, constructors, and toString method.
 */
@Getter
@Setter //Generates Getter and Setter
@NoArgsConstructor //Generates a no-arguments constructor.
@AllArgsConstructor //Generates a constructor with parameters for all fields.
@ToString // Generates a toString method for bugfixing.
public class Station {
    private int id;
    private String db_url;
    private float lat;
    private float lng;
}