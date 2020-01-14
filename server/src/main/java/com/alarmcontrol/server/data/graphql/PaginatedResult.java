package com.alarmcontrol.server.data.graphql;

import java.util.List;

public abstract class PaginatedResult<T> {

  private long totalCount;
  private List<T> items;

  public PaginatedResult(long totalCount, List<T> items) {
    this.totalCount = totalCount;
    this.items = items;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public List<T> getItems() {
    return items;
  }
}
