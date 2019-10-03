package io.fonimus.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

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
