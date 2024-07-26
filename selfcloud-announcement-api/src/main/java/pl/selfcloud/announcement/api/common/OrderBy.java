package pl.selfcloud.announcement.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderBy {

  ID("id"), CATEGORY("category"), SUBCATEGORY("subcategory"), PRICE("price"), CREATED_AT("created_at"), TITLE("title"), EMAIL("email");

  private final String value;
}
