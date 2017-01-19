package io.robe.hibernate.query.api.criteria;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class Order {
    private String alias;
    private final String name;
    private final Type type;

    public Order(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public static Order asc(String name) {
        return new Order(name, Type.ASC);
    }
    public static Order desc(String name) {
        return new Order(name, Type.DESC);
    }

    public String getAlias() {
        return alias;
    }

    void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ASC("+") , DESC("-");
        /**
         * Holds operator as {@link String}
         */
        private String value;

        /**
         *
         * @param value
         */
        Type(String value) {
            this.value = value;
        }

        /**
         * gets Operator as {@link String}
         * @return
         */
        public String value() {
            return value;
        }


        /**
         * gets Operator as {@link Type} enum type
         * @param op
         * @return
         */
        public static Type value(String op){
            for(Type type: Type.values()) {
                if(type.value().equals(op)) {
                    return type;
                }
            }
            throw new RuntimeException("Value not found in " + Type.class.getName()) ;
        }
    }
}
