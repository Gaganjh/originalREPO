package com.manulife.pension.ps.web.taglib.search;



import java.io.Serializable;
import java.util.Properties;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>@version 1.0</p>
 * <p> </p>
 * @author unascribed
 * @version 1.0
 */

public class CategoryUrlSpecification implements Serializable, Comparable {
    private String category;
    private String url;
    private Condition condition;

    public static interface Condition extends Serializable {
        boolean match(Properties properties);
    }

    public static class Equals implements Condition {
        private String property;
        private String value;

        public Equals(String property, String value) {
            this.property= property;
            this.value= value;
        }

        public boolean match(Properties properties) {
            if (properties == null) {
                return false;
            }
            String valueToMatch= properties.getProperty(property);

            return value.equals(valueToMatch);
        }
    }

    public static class NotEquals implements Condition {
        private String property;
        private String value;

        public NotEquals(String property, String value) {
            this.property= property;
            this.value= value;
        }

        public boolean match(Properties properties) {
            if (properties == null) {
                return true;
            }
            String valueToMatch= properties.getProperty(property);

            return !value.equals(valueToMatch);
        }
    }

    public CategoryUrlSpecification(String category, String url) {
        this.category= category;
        this.url= url;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public boolean equals(Object o) {
        return false;
    }

    public boolean equals(CategoryUrlSpecification other) {
        return this.category.equals(other.category);
    }

    public int compareTo(Object o) {
        CategoryUrlSpecification other= (CategoryUrlSpecification)o;
        return this.category.compareTo(other.category);
    }

    public int hashCode() {
        return category.hashCode();
    }

    public boolean match(Properties properties) {
        return condition == null ? true : condition.match(properties);
    }

    public void setCondition(String operator, String property, String value) {
        if ("equals".equals(operator)) {
            this.condition= new Equals(property, value);
        } else if ("notEquals".equals(operator)) {
            this.condition= new NotEquals(property, value);
        } else {
            throw new IllegalArgumentException("Operator \""+operator+"\" is not supported");
        }
    }
}

