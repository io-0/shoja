package net.io_0.shoja.model;

import java.time.OffsetDateTime;

public class Item {
  private final String name;
  private OffsetDateTime processedAt;

  public Item(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public OffsetDateTime getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(OffsetDateTime processedAt) {
    this.processedAt = processedAt;
  }
}
