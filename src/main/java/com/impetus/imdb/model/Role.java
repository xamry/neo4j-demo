/**
 * Copyright 2012 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.impetus.imdb.model;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * <Prove description of functionality provided by this Type> 
 * @author amresh.singh
 */

@RelationshipEntity
public class Role
{
    String roleName;
    @StartNode
    Actor actor;
    @EndNode
    Movie movie;

    @Override
    public String toString() {
        return String.format("%s-[%s]->%s", this.getActor(), roleName, this.getMovie());
    }

    public Actor getActor() {
        return actor;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getName() {
        return roleName;
    }

    public Role play(String name) {
        this.roleName = name;
        return this;
    }
    
}
