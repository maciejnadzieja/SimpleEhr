package repositories;

public class Query {
    private final int limit;
    private final int start;
    private final String query;

    public Query(int limit, int start, String query) {
        this.limit = limit;
        this.start = start;
        this.query = query;
    }

    public int getLimit() {
        return limit;
    }

    public int getStart() {
        return start;
    }

    public String getQuery() {
        return query;
    }

    public int getStop() {
        return getStart()+getLimit();
    }
}
