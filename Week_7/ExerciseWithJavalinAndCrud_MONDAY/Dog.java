package ExerciseWithJavalinAndCrud_MONDAY;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString


public class Dog {
    private int id;
    private String name;
    private int age;

    public Dog(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }


}
