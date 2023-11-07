package cse110.lab6;

import com.mongodb.client.*;
import org.bson.Document;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;


public class Read {


    public static void main(String[] args) {
        String uri = "mongodb+srv://cshukla:dojacat123@cluster0.uw5iowu.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");


            // find one document with new Document
            Document student1 = gradesCollection.find(new Document("student_id", 10000)).first();
            System.out.println("Student 1: " + student1.toJson());


            // find one document with Filters.eq()
            Document student2 = gradesCollection.find(eq("student_id", 10000)).first();
            System.out.println("Student 2: " + student2.toJson());


            // find a list of documents and iterate through it using an iterator.
            FindIterable<Document> iterable = gradesCollection.find(gte("student_id", 10000));
            MongoCursor<Document> cursor = iterable.iterator();
            System.out.println("Student list with a cursor: ");
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }


            // find a list of documents and use a List object instead of an iterator
            List<Document> studentList = gradesCollection.find(gte("student_id", 10000)).into(new ArrayList<>());
            System.out.println("Student list with an ArrayList:");
            for (Document student : studentList) {
                System.out.println(student.toJson());
            }


            // find a list of documents with sort, skip, limit and projection
            List<Document> docs = gradesCollection.find(and(eq("student_id", 10001), lte("class_id", 5)))
                                                  .projection(fields(excludeId(), include("class_id", "student_id")))
                                                  .sort(descending("class_id"))
                                                  .skip(2)
                                                  .limit(2)
                                                  .into(new ArrayList<>());


            System.out.println("Student sorted, skipped, limited and projected: ");
            for (Document student : docs) {
                System.out.println(student.toJson());
            }
        }
    }
}

