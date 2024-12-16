package org.example.orm;

import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "person")
@ToString
@NoArgsConstructor
public class Person {

    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private Integer age;

}
