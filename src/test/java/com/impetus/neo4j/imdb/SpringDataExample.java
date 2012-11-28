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
package com.impetus.neo4j.imdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.impetus.imdb.model.Movie;

/**
 * <Prove description of functionality provided by this Type> 
 * @author amresh.singh
 */
public class SpringDataExample
{
    @Autowired Neo4jTemplate template;


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        new SpringDataExample().persistedMovieShouldBeRetrievableFromGraphDb();

    }
    
    
    @Transactional public void persistedMovieShouldBeRetrievableFromGraphDb() {
        Movie mi = new Movie();
        mi.setTitle("Mission Impossible");
        mi.setYear(1996);
        
        Movie missionImpossibleMovie = template.save(mi);
        //Movie retrievedMovie = template.findOne(missionImpossibleMovie.getNodeId(), Movie.class);
        System.out.println(missionImpossibleMovie);
        
    }


}
