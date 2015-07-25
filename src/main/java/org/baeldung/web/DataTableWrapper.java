package org.baeldung.web;

import java.util.List;

public class DataTableWrapper {

    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<?> data;

    //

    public DataTableWrapper() {
        super();
    }

    public DataTableWrapper(final int draw, final long recordsTotal, final List<?> data) {
        super();
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.data = data;
        recordsFiltered = recordsTotal;
    }

    //

    public int getDraw() {
        return draw;
    }

    public void setDraw(final int draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(final long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(final long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(final List<?> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DataTableWrapper [draw=").append(draw).append(", recordsTotal=").append(recordsTotal).append(", data=").append(data).append("]");
        return builder.toString();
    }

}
