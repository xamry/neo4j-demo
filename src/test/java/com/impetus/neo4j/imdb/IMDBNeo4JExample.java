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

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * <Prove description of functionality provided by this Type>
 * 
 * @author amresh.singh
 */
public class IMDBNeo4JExample
{
    enum RelationshipTypes implements RelationshipType
    {
        ACTS_IN
    };

    public static void main(String[] args)
    {
        Transaction tx = null;

        try
        {
            GraphDatabaseService gds = new EmbeddedGraphDatabase("target/neo4j-imdb-db");

            tx = gds.beginTx();
            
            //Mission Impossible
            Node mi = gds.createNode();
            mi.setProperty("title", "Mission Impossible");
            mi.setProperty("year", 1996);
            gds.index().forNodes("movies").add(mi, "id", 1);

            
            Node tom = gds.createNode();
            tom.setProperty("name", "Tom Cruise");
            Relationship actorRel = tom.createRelationshipTo(mi, RelationshipTypes.ACTS_IN);
            actorRel.setProperty("roleName", "Lead Actor");
            
            Node ema = gds.createNode();
            ema.setProperty("name", "Emmanuelle Béart");
            Relationship actressRel = ema.createRelationshipTo(mi, RelationshipTypes.ACTS_IN);
            actressRel.setProperty("roleName", "Lead Actress");
            
            
            
            //Print Movie and Actors
            Node movie = gds.index().forNodes("movies").get("id", 1).getSingle();
            
            System.out.println("Actors in Movie - " + movie.getProperty("title") + ":"); 
            System.out.println("--------------------------------------");

            for (Relationship rel : movie.getRelationships(RelationshipTypes.ACTS_IN, Direction.INCOMING))
            {
                Node actor = rel.getOtherNode(movie);
                System.out.println(actor.getProperty("name") + " as " + rel.getProperty("roleName"));
            }

            tx.success();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            tx.failure();
        }
        finally
        {
            tx.finish();
        }

    }

}
