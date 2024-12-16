package org.example;

import org.example.orm.OwnOrm;
import org.example.orm.Person;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/postgres");
        dataSource.setUser("postgres");
        dataSource.setPassword("1111");
        OwnOrm ownOrm = new OwnOrm(dataSource);
       try {
           System.out.println(ownOrm.find(Person.class, 3L));
       } catch (RuntimeException e) {
           e.printStackTrace();
       } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
           throw new RuntimeException(e);
       }
    }
}