package be.thebeehive.tdd.todoapp.model;

import com.github.javafaker.Faker;

public class TodoEntityFixtures {
    public static final Faker FAKER = Faker.instance();

    public static TodoEntity todoEntity() {
        return new TodoEntity(Math.toIntExact(FAKER.number().randomNumber(4, false)));
    }
}