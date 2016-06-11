package sj.bodystats.Database;

/**
 * Created by Corsair on 10-6-2016.
 */
public enum Queries {
    GET_ALL_WEIGHTS("SELECT * FROM Weight", QueryTypes.READ),
    GET_LAST_INSERTED_WEIGHT("SELECT * FROM Weight WHERE id = (SELECT MAX(id) FROM Weight)", QueryTypes.READ);

    private String query;
    private QueryTypes queryType;

    Queries(String query, QueryTypes queryType) {
        this.query = query;
        this.queryType = queryType;
    }

    public String getQuery() {
        return query;
    }

    public QueryTypes getQueryType() {
        return queryType;
    }
}

