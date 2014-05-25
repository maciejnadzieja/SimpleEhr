package repositories;

import java.util.Collection;
import java.util.Collections;

public class QueryResult<T> {
    private Collection<T> items;
    private int totalCount;

    public QueryResult(Collection<T> items, int totalCount) {
        this.items = Collections.unmodifiableCollection(items);
        this.totalCount = totalCount;
    }

    public Collection<T> getItems() {
        return items;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
