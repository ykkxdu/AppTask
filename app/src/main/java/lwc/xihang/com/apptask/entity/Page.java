package lwc.xihang.com.apptask.entity;

/**
 * Created on 2017/12/24.
 * 页面的定义
 */

public class Page {
    // 每页元素的个数
    private Integer number;
    // 每页的大小
    private Integer size;
    // 总共的页数
    private Integer totalPages;
    // 总共的元素
    private Integer totalElements;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }
}
