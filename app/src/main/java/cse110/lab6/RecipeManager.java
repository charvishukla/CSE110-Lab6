package cse110.lab6;
import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;

public class RecipeManager {
    public static void main(String[] args) {
        // connection URI
        String uri = "mongodb+srv://cshukla:dojacat123@cluster0.uw5iowu.mongodb.net/?retryWrites=true&w=majority";

        // JSON settings
        JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();


        // connect to the Database: 
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            // setting the database name to recipe_db
            MongoDatabase database = mongoClient.getDatabase("recipe_db");
            // setting the collection name to recipe
            MongoCollection<Document> collection = database.getCollection("recipe");
                  
        
            // Read Recipe Data from CSV and Insert Recipes
            try (BufferedReader br = new BufferedReader(new FileReader("/Users/charvieshukla/Desktop/CSE110-Lab6/recipes.csv"))) {   
                String line;
                List<Document> recipesList = new ArrayList<>(); // store recipis in an arraylist
            
                // loop through every line in the CSV file
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";", -1); // split values by semicolons
                    if(values.length >= 3){
                        Document recipe = new Document("name", values[0])
                                        .append("description", values[1])
                                        .append("hours", Double.parseDouble(values[2]));
                    recipesList.add(recipe); // add to the arraylist 
                    //System.out.println(recipe.toJson());
                    } else{
                        continue;
                    }
                }
                collection.insertMany(recipesList); // Insert recipes into the collection
            } catch (IOException e) {
                e.printStackTrace();
            }
        // Print Recipe Count
        System.out.println("Total recipes in the collection: " + collection.countDocuments());
        
        
        // Read Operation
        // find Savory Spinach Delight by name
        Document recipeToFind = collection.find(Filters.eq("name", "Savory Spinach Delight")).first();

        if (recipeToFind != null) { // make sure that it is there 
            System.out.println("original number of hours required for Savory Spinach Delight: " + recipeToFind.getDouble("hours")); 
        }

        // Update Operation:
        // find Savory Spinach Delight by name 
        // then update its hours to 4.5 hours 

        // update one document
            Bson filter = eq("name","Savory Spinach Delight" );
            Bson updateOperation = Updates.set("hours", 4.5);
            UpdateResult updateResult = collection.updateOne(filter, updateOperation);
            //System.out.println(updateResult.toString());
        

        //collection.updateOne(Filters.eq("name", "Savory Spinach Delight"), Updates.set("hours", 4.5));
        //collection.updateOne(Filters.eq("name", "Savory Spinach Delight"), Updates.set("hours", 4.5));
        
        
        
        // Print the updated hours
        Document updatedSpinach = collection.find(Filters.eq("name", "Savory Spinach Delight")).first();
        if (updatedSpinach != null) { // make sure that it is there 
            
            System.out.println("updated number of hours required for Savory Spinach Delight: " + updatedSpinach.getDouble("hours")); 
        }

        // Delete Operation
        collection.deleteOne(Filters.eq("name", "Spicy Shrimp Tacos"));

        // Updated number of recipes 
        System.out.println("final number of recipes: " + collection.countDocuments());

        }        
    }
}
