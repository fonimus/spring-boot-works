/*
 * Copyright (c) Worldline 2019.
 */

package com.worldline.edoc.neo4j.neo4j;

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
public class Movie {

    @Id
    @GeneratedValue
    Long id;

    private String title;

    private int released;

    @Relationship(type = "DIRECTED_BY", direction = Relationship.OUTGOING)
    private List<Person> directors;

    @Relationship(type = "ACTED_IN", direction = Relationship.INCOMING)
    private List<Role> roles;
}
