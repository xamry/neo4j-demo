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

import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.index.RelationshipIndex;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * <Prove description of functionality provided by this Type> 
 * @author amresh.singh
 */
public class BollywoodExample
{
    static GraphDatabaseService graphDb = new EmbeddedGraphDatabase("target/neo4j-imdb-db");;
    static IndexManager index = graphDb.index();
    
    public static void main(String[] args)
    {        
        BollywoodExample example = new BollywoodExample();
        
        example.insert();
        example.search();
        
        
        
        
    }

    private void query()
    {
        //Get Indexes            
        Index<Node> actors = index.forNodes( "actors" );
        Index<Node> movies = index.forNodes( "movies" );
        RelationshipIndex roles = index.forRelationships( "roles" );  //extends Index<Relationship>
       
        for (Node actor : actors.query( "name", "*K*" ) )
        {
            // This will return SRK and Katrina
        }
        
        //In the following example the query uses multiple keys:
        for (Node movie : movies.query("title:*T* AND year:2012" ) )
        {
            //This will return JTHJ and ETT
        }       
        
        //Sorted results
        for ( Node movie : movies.query( "title", new QueryContext( "*" ).sort( "title" ) ) )
        {
            // all movies with a title in the index, ordered by title
        }        
        
        for ( Node movie : movies.query(new QueryContext( "title:*" ).sort( "year", "title" )))
        {
            // all movies with a title in the index, ordered by year, then title
        }
        
        //Querying with Lucene Query objects        
        for ( Node movie : movies.query( new WildcardQuery( new Term( "title", "*T*" ) ) ) )
        {
            //This is how to perform wildcard searches using Lucene Query Objects:
        }
    }
    
    private void search() {
        //Get Indexes            
        Index<Node> actors = index.forNodes( "actors" );
        Index<Node> movies = index.forNodes( "movies" );
        RelationshipIndex roles = index.forRelationships( "roles" );  //extends Index<Relationship>
        
        //Only one Node matching
        IndexHits<Node> skrNodes = actors.get( "name", "Shahrukh Khan" );
        Node srk = skrNodes.getSingle();
        
        //Multiple Nodes matching 
        IndexHits<Node> moviesIn2012 = movies.get( "year", "2012" );
        for(Node node : moviesIn2012) {
            System.out.println(node.getProperty("title"));
        }
        
        //Search over relationships
        Relationship asShekhar = roles.get( "name", "Shekhar" ).getSingle();
        Node actor = asShekhar.getStartNode();
        Node movie = asShekhar.getEndNode();
    }
    private void insert()
    {
        Transaction tx = null;

        try
        {            
            tx = graphDb.beginTx();
            
            //Create or Get Indexes            
            Index<Node> actors = index.forNodes( "actors" );
            Index<Node> movies = index.forNodes( "movies" );
            RelationshipIndex roles = index.forRelationships( "roles" );  //extends Index<Relationship>

            //Create Nodes
            Node srk = graphDb.createNode();
            srk.setProperty( "name", "Shahrukh Khan" );
            
            Node katrina = graphDb.createNode();
            katrina.setProperty( "name", "Katrina Kaif" );

            Node ra1 = graphDb.createNode();
            ra1.setProperty( "title", "Ra-One" );
            ra1.setProperty( "year", "2011" );

            Node jthj = graphDb.createNode();
            jthj.setProperty( "title", "Jab Tak Hai Jaan" );
            jthj.setProperty( "year", "2012" );

            Node ett = graphDb.createNode();
            ett.setProperty( "title", "Ek Tha Tiger" );
            ett.setProperty( "year", "2012" );

            //Create Indexes on Nodes
            actors.add(srk, "name", srk.getProperty( "name" ));
            actors.add(katrina, "name", katrina.getProperty( "name" ));

            movies.add(ra1, "title", ra1.getProperty( "title" ));
            movies.add(ra1, "year", ra1.getProperty( "year" ));

            movies.add(jthj, "title", jthj.getProperty( "title" ));
            movies.add(jthj, "year", jthj.getProperty( "year" ));

            movies.add(ett, "title", ett.getProperty( "title" ));
            movies.add(ett, "year", ett.getProperty( "year" ));

            //Create Relationships
            DynamicRelationshipType ACTS_IN = DynamicRelationshipType.withName( "ACTS_IN" );
            
            Relationship role1 = srk.createRelationshipTo(ra1, ACTS_IN );
            role1.setProperty( "as", "Shekhar" );
            
            Relationship role2 = srk.createRelationshipTo(jthj, ACTS_IN );
            role1.setProperty( "as", "Samar" );
            
            Relationship role3 = katrina.createRelationshipTo(jthj, ACTS_IN );
            role1.setProperty( "as", "Meera" );
            
            Relationship role4 = katrina.createRelationshipTo(ett, ACTS_IN );
            role1.setProperty( "as", "Zoya" );
            
            //Create Indexes on relationships
            roles.add(role1, "as", role1.getProperty( "as" ));
            roles.add(role2, "as", role2.getProperty( "as" ));
            roles.add(role3, "as", role3.getProperty( "as" ));
            roles.add(role4, "as", role4.getProperty( "as" ));
            

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
