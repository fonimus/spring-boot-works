/*
 * Copyright (c) Worldline 2019.
 */

package com.worldline.edoc.neo4j.neo4j;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@RelationshipEntity(type = "ACTED_IN")
public class Role {

    @Id
    @GeneratedValue
    Long id;

    private Collection<String> roles;

    @StartNode
    private Person person;

    @EndNode
    @JsonBackReference
    private Movie movie;

}