/*
 * Copyright (c) Worldline 2019.
 */

package com.worldline.edoc.neo4j.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository
        extends Neo4jRepository<Role, Long> {

}
