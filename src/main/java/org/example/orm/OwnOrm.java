package org.example.orm;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class OwnOrm {

    private final DataSource dataSource;


    public <T> T find(Class<T> type, Long id) throws InstantiationException, IllegalAccessException, SQLException,
            NoSuchMethodException, InvocationTargetException {
        if (nonNull(type.getAnnotation(Entity.class))) {
            String tableName = type.getAnnotation(Table.class).name();
            T instance = type.getConstructor().newInstance();
            try (Connection connection = dataSource.getConnection()) {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?");
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    for (Field field : type.getDeclaredFields()) {
                        Optional<Annotation> fieldColumnAnnotation = Arrays.stream(field.getDeclaredAnnotations())
                                .filter(annotation -> annotation.annotationType().equals(Column.class))
                                .findAny();
                        if (fieldColumnAnnotation.isPresent()) {
                            Column column = field.getAnnotation(Column.class);
                            String columnName = column.name();
                            Class<?> fieldType = field.getType();
                            Object value = resultSet.getObject(columnName, fieldType);
                            field.setAccessible(true);
                            field.set(instance, value);
                        }
                    }
                }
            }
            return instance;
        }
        throw new RuntimeException("The specified class is not an entity");
    }
}
