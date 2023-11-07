package cse110.lab6;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;


import java.util.Random;


import static java.util.Arrays.asList;


public class Create {


    public static void main(String[] args) {
        String uri = "mongodb+srv://cshukla:dojacat123@cluster0.uw5iowu.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {


            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");


            Random rand = new Random();
            Document student = new Document("_id", new ObjectId());
            student.append("student_id", 10000d)
                   .append("class_id", 1d)
                   .append("scores", asList(new Document("type", "exam").append("score", rand.nextDouble() * 100),
                                            new Document("type", "quiz").append("score", rand.nextDouble() * 100),
                                            new Document("type", "homework").append("score", rand.nextDouble() * 100),
                                            new Document("type", "homework").append("score", rand.nextDouble() * 100)));


            gradesCollection.insertOne(student);
        }
    }
}
