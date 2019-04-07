/*
 * Copyright (c) Worldline 2019.
 */

package com.worldline.edoc.neo4j.neo4j;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NodeEntity
public class Person {

    @Id
    @GeneratedValue
    Long id;

    private String name;

    private int born;

    @Relationship(type = "ACTED_IN", direction = "OUTGOING")
    @JsonBackReference
    private List<Movie> movies;

}
