/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.db;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.time.LocalDate;

/** Persists {@link LocalDate} values as ISO-8601 strings. */
public final class LocalDatePersister extends BaseDataType {

    /** Singleton persister instance. */
    private static final LocalDatePersister SINGLETON = new LocalDatePersister();

    /** Private constructor to enforce singleton pattern. */
    private LocalDatePersister() {
        super(SqlType.STRING, new Class<?>[]{LocalDate.class});
    }

    /**
     * Returns the singleton persister instance.
     *
     * @return the singleton instance
     */
    public static LocalDatePersister getSingleton() {
        return SINGLETON;
    }

    /**
     * Parses the default string value.
     *
     * @param fieldType field type definition
     * @param defaultStr default string representation
     * @return parsed default string
     */
    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return defaultStr;
    }

    /**
     * Converts a database result column to a SQL argument.
     *
     * @param fieldType field type definition
     * @param results database query results
     * @param columnPos column index
     * @return SQL argument string
     * @throws SQLException if database access fails
     */
    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    /**
     * Converts a SQL argument string back into a {@link LocalDate}.
     *
     * @param fieldType field type definition
     * @param sqlArg SQL argument object
     * @param columnPos column index
     * @return converted LocalDate
     */
    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return LocalDate.parse((String) sqlArg);
    }

    /**
     * Converts a {@link LocalDate} to a SQL argument string.
     *
     * @param fieldType field type definition
     * @param javaObject LocalDate object
     * @return ISO-8601 string representation
     */
    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        return javaObject.toString();
    }
}