/**
 *
 * ListAsSQLArrayUserType.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.model;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * TODO Add class description here.
 */
public abstract class ListAsSQLArrayUserType<T> implements UserType {

    private static final int SQL_TYPE = Types.ARRAY;

    private static final int[] SQL_TYPES = { SQL_TYPE };

    abstract protected Array getDataAsArray(Object value);

    abstract protected List<T> getDataFromArray(Object primitivesArray);

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$BOOLEAN"
     * hibernate.column name="fieldName" sql-type="bool[]"
     */
    public static class BOOLEAN extends ListAsSQLArrayUserType<Boolean> {
        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SqlArray.BOOLEAN((List<Boolean>) value);
        }

        @Override
        protected List<Boolean> getDataFromArray(Object array) {
            boolean[] booleans = (boolean[]) array;
            ArrayList<Boolean> result = new ArrayList<Boolean>(booleans.length);
            for (boolean b : booleans)
                result.add(b);

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$INTEGER"
     * hibernate.column name="fieldName" sql-type="int[]"
     */
    public static class INTEGER extends ListAsSQLArrayUserType<Integer> {
        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SqlArray.INTEGER((List<Integer>) value);
        }

        @Override
        protected List<Integer> getDataFromArray(Object array) {
            int[] ints = (int[]) array;
            ArrayList<Integer> result = new ArrayList<Integer>(ints.length);
            for (int i : ints)
                result.add(i);

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$FLOAT"
     * hibernate.column name="fieldName" sql-type="real[]"
     */
    public static class FLOAT extends ListAsSQLArrayUserType<Float> {
        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SqlArray.FLOAT((List<Float>) value);
        }

        @Override
        protected List<Float> getDataFromArray(Object array) {
            float[] floats = (float[]) array;
            ArrayList<Float> result = new ArrayList<Float>(floats.length);
            for (float f : floats)
                result.add(f);

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$DOUBLE"
     * hibernate.column name="fieldName" sql-type="float8[]"
     */
    public static class DOUBLE extends ListAsSQLArrayUserType<Double> {
        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SqlArray.DOUBLE((List<Double>) value);
        }

        @Override
        protected List<Double> getDataFromArray(Object array) {
            double[] doubles = (double[]) array;
            ArrayList<Double> result = new ArrayList<Double>(doubles.length);
            for (double d : doubles)
                result.add(d);

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$STRING"
     * hibernate.column name="fieldName" sql-type="text[]"
     */
    public static class STRING extends ListAsSQLArrayUserType<String> {
        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SqlArray.STRING((List<String>) value);
        }

        @Override
        protected List<String> getDataFromArray(Object array) {
            String[] strings = (String[]) array;
            ArrayList<String> result = new ArrayList<String>(strings.length);
            for (String s : strings)
                result.add(s);

            return result;
        }
    }

    /**
     * To use, define : hibernate.property
     * type="com.seanergie.persistence.ListAsSQLArrayUserType$DATE"
     * hibernate.column name="fieldName" sql-type="timestamp[]"
     */
    public static class DATE extends ListAsSQLArrayUserType<Date> {
        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            return new SqlArray.DATE((List<Date>) value);
        }

        @Override
        protected List<Date> getDataFromArray(Object array) {
            Date[] dates = (Date[]) array;
            ArrayList<Date> result = new ArrayList<Date>(dates.length);
            for (Date d : dates)
                result.add(d);

            return result;
        }
    }

    /**
     * Warning, this one is special. You have to define a class that extends
     * ENUM_LIST&lt;E&gt; and that has a no arguments constructor. For example :
     * class MyEnumsList extends ENUM_LIST&&ltMyEnumType&gt; { public
     * MyEnumList(){ super( MyEnum.values() ); } } Then, define :
     * hibernate.property type="com.myPackage.MyEnumsList" hibernate.column
     * name="fieldName" sql-type="int[]"
     */
    public static class ENUM<E extends Enum<E>> extends
            ListAsSQLArrayUserType<E> {
        private E[] theEnumValues;

        /**
         * @param clazz
         *            the class of the enum.
         * @param theEnumValues
         *            The values of enum (by invoking .values()).
         */
        protected ENUM(E[] theEnumValues) {
            this.theEnumValues = theEnumValues;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected Array getDataAsArray(Object value) {
            List<E> enums = (List<E>) value;
            List<Integer> integers = new ArrayList<Integer>(enums.size());
            for (E theEnum : enums)
                integers.add(theEnum.ordinal());

            return new SqlArray.INTEGER(integers);
        }

        @Override
        protected List<E> getDataFromArray(Object array) {
            int[] ints = (int[]) array;
            ArrayList<E> result = new ArrayList<E>(ints.length);
            for (int val : ints) {
                for (int i = 0; i < theEnumValues.length; i++) {
                    if (theEnumValues[i].ordinal() == val) {
                        result.add(theEnumValues[i]);
                        break;
                    }
                }
            }

            if (result.size() != ints.length)
                throw new RuntimeException("Error attempting to convert "
                        + array + " into an array of enums (" + theEnumValues
                        + ").");

            return result;
        }
    }

    public Class returnedClass() {
        return List.class;
    }

    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    public Object deepCopy(Object value) {
        return value;
    }

    public boolean isMutable() {
        return true;
    }

    @SuppressWarnings("unused")
    public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
            throws HibernateException, SQLException {

        Array sqlArray = resultSet.getArray(names[0]);
        if (resultSet.wasNull())
            return null;

        return getDataFromArray(sqlArray.getArray());
    }

    public void nullSafeSet(PreparedStatement preparedStatement, Object value,
            int index) throws HibernateException, SQLException {
        if (null == value)
            preparedStatement.setNull(index, SQL_TYPE);
        else
            preparedStatement.setArray(index, getDataAsArray(value));
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y)
            return true;
        if (null == x || null == y)
            return false;
        Class javaClass = returnedClass();
        if (!javaClass.equals(x.getClass()) || !javaClass.equals(y.getClass()))
            return false;

        return x.equals(y);
    }

    @SuppressWarnings("unused")
    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return cached;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @SuppressWarnings("unused")
    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return original;
    }
}
