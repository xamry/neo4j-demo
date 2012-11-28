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
            
            //War of the worlds
            Node wotw = gds.createNode();
            wotw.setProperty("title", "War of the Worlds");
            wotw.setProperty("year", 2005);
            gds.index().forNodes("movies").add(wotw, "id", 2);          
            

            //Tom Cruise
            Node tom = gds.createNode();
            tom.setProperty("name", "Tom Cruise");
            Relationship miTomRole = tom.createRelationshipTo(mi, RelationshipTypes.ACTS_IN);
            miTomRole.setProperty("roleName", "Lead Actor");
            Relationship wotwTomRole = tom.createRelationshipTo(wotw, RelationshipTypes.ACTS_IN);            
            wotwTomRole.setProperty("roleName", "Action");            
            gds.index().forNodes("actors").add(tom, "id", "a1");
            
            //Emmanuelle Béart
            Node ema = gds.createNode();
            ema.setProperty("name", "Emmanuelle Béart");
            Relationship actressRel = ema.createRelationshipTo(mi, RelationshipTypes.ACTS_IN);
            actressRel.setProperty("roleName", "Lead Actress");            
            
            
            //Print MI Movie and Actors
            Node miMovie = gds.index().forNodes("movies").get("id", 1).getSingle();
            
            System.out.println("Actors in Movie - " + miMovie.getProperty("title") + ":"); 
            System.out.println("--------------------------------------");
            for (Relationship rel : miMovie.getRelationships(RelationshipTypes.ACTS_IN, Direction.INCOMING))
            {
                Node actor = rel.getOtherNode(miMovie);
                System.out.println(actor.getProperty("name") + " as " + rel.getProperty("roleName"));
            }
            
            //Print all movies for Tom
            Node tomActor = gds.index().forNodes("actors").get("id", "a1").getSingle();
            System.out.println("\nAll Movies " + tomActor.getProperty("name") + " acted in:");
            System.out.println("--------------------------------------");
            for(Relationship rel : tomActor.getRelationships(RelationshipTypes.ACTS_IN, Direction.OUTGOING)) {
                Node movie = rel.getOtherNode(tomActor);
                System.out.println("* " + movie.getProperty("title") + " in " + movie.getProperty("year"));
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
