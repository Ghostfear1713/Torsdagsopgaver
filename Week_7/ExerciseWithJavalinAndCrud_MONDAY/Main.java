package ExerciseWithJavalinAndCrud_MONDAY;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {

        //We start making a list of dogs with Map
        //We will use the id as the key and the Dog object as the value
        Map<Integer, Dog> dogs = new HashMap<>();
        //We create an AtomicInteger to make sure that we can create a unique id for each dog - this will function as our autoincrement
        AtomicInteger nextId = new AtomicInteger(1);

        //We create a new dog and add it to the map
        //nextId will first return the value of nextId and then increment it by 1 to make sure that IF we create a new dog,
        //it will have a unique id because it will be incremented with 1
        dogs.put(nextId.getAndIncrement(), new Dog(nextId.get(), "Fido", 3));
        dogs.put(nextId.getAndIncrement(), new Dog(nextId.get(), "Rex", 5));
        dogs.put(nextId.getAndIncrement(), new Dog(nextId.get(), "Bobby", 2));
        dogs.put(nextId.getAndIncrement(), new Dog(nextId.get(), "Lassie", 4));


        Javalin app = Javalin.create().start(7007);
        //Ctx is short for context and it is the object that we use to interact with the request and response objects
        app.get("/", ctx -> ctx.result("This is the mainpage of the dog API"));

        //Now we want to create an endpoint that returns all the dogs
        app.get("/dogs", ctx -> {
            //.json is a method that takes an object and converts it to a JSON string
            ctx.json(dogs.values());
        });

        //Now we want to create an endpoint that returns a specific dog
        app.get("/dogs/:id", ctx -> {
            //We get the id from the path parameter and convert it to an int
            int id = Integer.parseInt(ctx.pathParam("id"));
            //We get the dog from the map
            Dog dog = dogs.get(id);
            //We return the dog
            ctx.json(dog);
        });

        //Now we want to create an endpoint that creates a new dog
        app.post("/create", ctx -> {
            //We get the name and age from the form parameters
            String name = ctx.formParam("name");
            int age = Integer.parseInt(ctx.formParam("age"));
        //We create a new dog and add the parameters to it and then to the map
            Dog newDog = new Dog(nextId.getAndIncrement(), name, age);
            dogs.put(newDog.getId(), newDog);

            //We return the dog
            ctx.json(newDog);
        });

        //Now we want to create an endpoint that deletes a dog
        app.delete("/delete/:id", ctx -> {
            //We get the id from the path parameter and convert it to an int
            int id = Integer.parseInt(ctx.pathParam("id"));
            //We remove the dog from the map
            if(dogs.containsKey(id)){
                Dog removedDog = dogs.remove(id);
                //We return the dog that was removed
                System.out.println("Following dog :\n" + removedDog + " \nhas now been removed");
                ctx.json(removedDog);

            } else {
                ctx.result("Dog not found");
            }
        });


        //Now we want to create an endpoint that updates a dog
        app.put("/update/:id", ctx -> {
            //We get the id from the path parameter and convert it to an int
            int id = Integer.parseInt(ctx.pathParam("id"));
            //We get the dog from the map
            Dog dog = dogs.get(id);
            //We get the name and age from the form parameters
            String name = ctx.formParam("name");
            int age = Integer.parseInt(ctx.formParam("age"));
            //We update the dog
            dog.setName(name);
            dog.setAge(age);
            //We return the dog
            ctx.json(dog);
        });


    }
}
