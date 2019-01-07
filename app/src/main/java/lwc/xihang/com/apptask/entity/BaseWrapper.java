package lwc.xihang.com.apptask.entity;

import java.util.List;
import java.util.Map;

/**
 * Created on 2017/12/24.
 */

public class BaseWrapper<T> {

    private Links _links;
    private Page page;
    private Map<String, List<T>> _embedded;

    public Links get_links() {
        return _links;
    }

    public void set_links(Links _links) {
        this._links = _links;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Map<String, List<T>> get_embedded() {
        return _embedded;
    }

    public void set_embedded(Map<String, List<T>> _embedded) {
        this._embedded = _embedded;
    }
}
