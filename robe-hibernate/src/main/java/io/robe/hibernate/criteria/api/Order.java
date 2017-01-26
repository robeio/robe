package io.robe.hibernate.criteria.api;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class Order {
    private String criteriaAlias;
    private final String name;
    private final boolean isAlias;
    private final Type type;

    public Order(String name, Type type, boolean isAlias) {
        this.name = name;
        this.type = type;
        this.isAlias = isAlias;
    }

    public static Order asc(String name) {
        return new Order(name, Type.ASC, false);
    }

    public static Order ascByAlias(String alias) {
        return new Order(alias, Type.ASC, true);
    }

    public static Order desc(String name) {
        return new Order(name, Type.DESC, false);
    }

    public static Order descByAlias(String alias) {
        return new Order(alias, Type.DESC, true);
    }

    public boolean isAlias() {
        return isAlias;
    }

    public String getCriteriaAlias() {
        return criteriaAlias;
    }

    void setCriteriaAlias(String criteriaAlias) {
        this.criteriaAlias = criteriaAlias;
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
