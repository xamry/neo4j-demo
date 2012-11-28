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

import java.util.Set;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * <Prove description of functionality provided by this Type> 
 * @author amresh.singh
 */

@NodeEntity
public class Actor
{
    @Indexed
    private String name;

    @RelatedTo(type = "ACTS_IN", elementClass = Movie.class)
    private Set<Movie> movies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return movies;
    }

    /*public Role getRole(final Movie inMovie) {
        return getRelationshipTo(inMovie, Role.class, RelTypes.ACTS_IN.name());
    }*/

    @Override
    public String toString() {
        return "Actor '" + this.getName() + "'";
    }

    /*public void actsIn(Movie movie, final String role) {
        relateTo(movie, Role.class, RelTypes.ACTS_IN.name()).play(role);
    }*/

    public int getMovieCount() {
        return getMovies().size();
    }
    

}
