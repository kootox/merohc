package org.chorem.merohc;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Class containing utilities methods for DAO request.
 *
 * @author Eric Chatellier
 * @author Arnaud Thimel (Code Lutin)
 * @author ymartel (codelutin)
 */
public class DaoUtils {

    protected static final String LIKE =
        "TRANSLATE(LOWER( %s ),"
            + "'áàâãäåāăąèééêëēĕėęěìíîïìĩīĭḩóôõöōŏőùúûüũūŭůäàáâãåæçćĉčöòóôõøüùúûßéèêëýñîìíïş',"
            + "'aaaaaaaaaeeeeeeeeeeiiiiiiiihooooooouuuuuuuuaaaaaaeccccoooooouuuuseeeeyniiiis')"
            + "like LOWER( %s )";

    /**
     * Generate sql like operator case and accent insensitive.
     *
     * @param field1 entity field to search into
     * @param field2 value field (must be accent escaped)
     * @return sql string
     */
    public static String getFieldLikeInsensitive(String field1, String field2) {
        String query = String.format(LIKE, field1, field2);
        return query;
    }

    public static String addQueryAttribute(Map<String, Object> args, String entityAttributeName, Object value) {
        String baseAttributeName = entityAttributeName.replaceAll("[.]", "_");

        int index = 0;
        String queryAttributeName;
        do {
            queryAttributeName = baseAttributeName + index;
            index++;
        } while (args.containsKey(queryAttributeName));

        args.put(queryAttributeName, value);

        return queryAttributeName;
    }

    protected static String getQueryForAttributeLike(String entityAlias, String entityAttributeName, Map<String, Object> args, String likeValue, String operator) {
        // TODO AThimel 12/07/13 Refactor : peut-être qu'il n'est pas nécessaire d'utiliser la méthode "getFieldLikeInsensitive"
        String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
        String queryAttributeName = addQueryAttribute(args, entityAttributeName, StringUtils.stripAccents(likeValue));
        String result = " " + operator + " " + DaoUtils.getFieldLikeInsensitive(alias + entityAttributeName, ":" + queryAttributeName);

        return result;
    }

    public static String getQueryForAttributeEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value, String operator) {
        String result = "";

        if (value != null) {
            String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
            String queryAttributeName = addQueryAttribute(args, entityAttributeName, value);
            result += String.format(" %s %s = :%s", operator, alias + entityAttributeName, queryAttributeName);
        }

        return result;
    }

    public static String getQueryForAttributeNotEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value, String operator) {
        String result = "";

        if (value != null) {
            String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
            String queryAttributeName = addQueryAttribute(args, entityAttributeName, value);
            result += String.format(" %s %s != :%s", operator, alias + entityAttributeName, queryAttributeName);
        }

        return result;
    }

    public static String getQueryForAttributeGreaterOrEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value, String operator) {
        String result = "";

        if (value != null) {
            String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
            String queryAttributeName = addQueryAttribute(args, entityAttributeName, value);
            result += String.format(" %s %s >= :%s", operator, alias + entityAttributeName, queryAttributeName);
        }

        return result;
    }

    public static String getQueryForAttributeLesserOrEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value, String operator) {
        String result = "";

        if (value != null) {
            String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
            String queryAttributeName = addQueryAttribute(args, entityAttributeName, value);
            result += String.format(" %s %s <= :%s", operator, alias + entityAttributeName, queryAttributeName);
        }

        return result;
    }

    public static String andAttributeEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeEquals(entityAlias, entityAttributeName, args, value, "AND");
        return result;
    }

    public static String andAttributeNotEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeNotEquals(entityAlias, entityAttributeName, args, value, "AND");
        return result;
    }

    public static String andAttributeGreaterOrEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeGreaterOrEquals(entityAlias, entityAttributeName, args, value, "AND");
        return result;
    }

    public static String andAttributeLesserOrEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeLesserOrEquals(entityAlias, entityAttributeName, args, value, "AND");
        return result;
    }

    public static String orAttributeEquals(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeEquals(entityAlias, entityAttributeName, args, value, "OR");
        return result;
    }

    public static String andAttributeLike(String entityAlias, String entityAttributeName, Map<String, Object> args, String value) {
        String result = "";
        if (StringUtils.isNotBlank(value)) {
            result = getQueryForAttributeLike(entityAlias, entityAttributeName, args, "%" + value + "%", "AND");
        }
        return result;
    }

    public static String orAttributeLike(String entityAlias, String entityAttributeName, Map<String, Object> args, String value) {
        String result = "";
        if (StringUtils.isNotBlank(value)) {
            result = getQueryForAttributeLike(entityAlias, entityAttributeName, args, "%" + value + "%", "OR");
        }

        return result;
    }

    protected static String getQueryForAttributeContains(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value, String operator) {
        String result = "";

        String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
        String queryAttributeName = addQueryAttribute(args, entityAttributeName, value);
        result += String.format(" %s :%s in elements( %s )", operator, queryAttributeName, alias + entityAttributeName);

        return result;
    }

    protected static String getQueryForAttributeIn(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value, String operator) {
        String result = "";

        String alias = StringUtils.isBlank(entityAlias) ? "" : entityAlias + ".";
        String queryAttributeName = addQueryAttribute(args, entityAttributeName, value);
        result += String.format(" %s %s in ( :%s )", operator, alias + entityAttributeName, queryAttributeName);

        return result;
    }

    public static String andAttributeContains(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeContains(entityAlias, entityAttributeName, args, value, "AND");
        return result;
    }

    public static String andAttributeIn(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeIn(entityAlias, entityAttributeName, args, value, "AND");
        return result;
    }

    public static String orAttributeIn(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeIn(entityAlias, entityAttributeName, args, value, "OR");
        return result;
    }

    public static String orAttributeContains(String entityAlias, String entityAttributeName, Map<String, Object> args, Object value) {
        String result = getQueryForAttributeContains(entityAlias, entityAttributeName, args, value, "OR");
        return result;
    }

}

