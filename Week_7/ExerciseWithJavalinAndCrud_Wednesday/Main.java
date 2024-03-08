package ExerciseWithJavalinAndCrud_Wednesday;

import io.javalin.Javalin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
/*          CONTEXT OBJECT IN JAVALIN: UNDERSTAND THE JAVALIN CONTEXT OBJECT AND HOW IT PROVIDES ACCESS TO INFORMATION
            ABOUT THE CURRENT HTTP REQUEST AND RESPONSE, INCLUDING PARAMETERS, HEADERS, AND THE REQUEST BODY.. */

    /*I Javalin, repræsentere Context objektet den nuværende HTTP request og response man arbejder med og er som udgangspunkt
    /den primære måde at interagere med disee objekter på.

    Context objektet kommer fra klassen Context som har en masse metoder til at interagere med request og response objekterne.
    Den kommer fra Handler klassen som er en interface der har en enkelt metode handle som tager et Context objekt som parameter den ser sådan ud:

    public interface Handler {
        void handle(Context ctx) throws Exception;
    }
    Handler klassen er en interface der har en enkelt metode handle som tager et Context objekt som parameter.
    Det betyder at når vi laver en endpoint i Javalin, så er det en metode der tager et Context objekt som parameter.
    Det er derfor vi skriver ctx -> i vores endpoints.

    Headers er en måde at sende metadata med en HTTP request eller response. Det kan være information om hvilken type data der sendes, hvilken type encoding der bruges, hvilken type authentication der bruges osv.
    I Javalin kan vi bruge Context objektet til at læse og skrive headers. Vi kan bruge metoderne header og headers til at læse og skrive headers.

    Body er den del af en HTTP request eller response der indeholder data. Det kan være data i form af tekst, billeder, video, lyd osv.
    I Javalin kan vi bruge Context objektet til at læse og skrive body. Vi kan bruge metoderne body og formParam til at læse body og formParam, json og result til at skrive body.


    Path parameters er en måde at sende data med en HTTP request
    Eks. hvis vi har en endpoint /dogs/:id, så kan vi sende et id med requesten og det vil blive læst af Javalin og vi kan bruge det i vores endpoint. Eks på kode:
    */
    public static void main(String[] args) {

        Map<Integer, Person> persons = new HashMap<>();
        AtomicInteger nextId = new AtomicInteger(1);

        persons.put(nextId.getAndIncrement(), new Person(nextId.get(), "Orhan", 25));
        persons.put(nextId.getAndIncrement(), new Person(nextId.get(), "Michael", 30));
        persons.put(nextId.getAndIncrement(), new Person(nextId.get(), "John", 35));
        persons.put(nextId.getAndIncrement(), new Person(nextId.get(), "Jane", 40));
        persons.put(nextId.getAndIncrement(), new Person(nextId.get(), "Mads", 20));

        Javalin app = Javalin.create().start(7006);

        app.get("/", ctx -> ctx.result("Welcome! This is the frontpage of the person API - I hope you find what you are looking for! Enjoy your visit!!!"));

        //Now we want to create an endpoint that returns all the persons in the map persons - we use the .json method to convert the object to a JSON string
        app.get("/persons", ctx -> {
            ctx.json(persons.values());
        });

        //Now we want to create an endpoint that returns a specific person
        app.get("/persons/:id", ctx -> {
            //We get the id from the path parameter and convert it to an int
            int id = Integer.parseInt(ctx.pathParam("id"));
            //We get the person from the map
            Person person = persons.get(id);
            //We return the person
            ctx.json(person);
        });

        //Now we want to create an endpoint that creates a new person
        app.post("/create", ctx -> {
            //We get the name and age from the form parameters
            String name = ctx.formParam("name");
            int age = Integer.parseInt(ctx.formParam("age"));
            //We create a new person and add the parameters to it and then to the map
            Person newPerson = new Person(nextId.getAndIncrement(), name, age);
            persons.put(newPerson.getId(), newPerson);
            //We return the person
            ctx.json(newPerson);
        });

        //Now we want to create an endpoint that updates a person
        app.put("/update/:id", ctx -> {
            //We get the id from the path parameter and convert it to an int
            int id = Integer.parseInt(ctx.pathParam("id"));
            //We get the person from the map
            Person person = persons.get(id);
            //We get the name and age from the form parameters
            String name = ctx.formParam("name");
            int age = Integer.parseInt(ctx.formParam("age"));
            //We update the person
            person.setName(name);
            person.setAge(age);
            //We return the person
            ctx.json(person);
        });

        //Now we want to create an endpoint that deletes a person
        app.delete("/delete/:id", ctx -> {
            //We get the id from the path parameter and convert it to an int
            int id = Integer.parseInt(ctx.pathParam("id"));
            //We remove the person from the map
            if(persons.containsKey(id)){
                Person removedPerson = persons.remove(id);
                //We return the person that was removed
                System.out.println("Following person :\n" + removedPerson + " \nhas now been removed");
                ctx.json(removedPerson);
            } else {
                ctx.result("Person not found");
            }
        });


    }


}
