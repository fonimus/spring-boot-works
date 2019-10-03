package io.fonimus.neo4j;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
