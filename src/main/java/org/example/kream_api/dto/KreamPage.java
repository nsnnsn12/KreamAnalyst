package org.example.kream_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class KreamPage <T>{
    List<T> items;
    Integer cursor;
    Long total;
    Integer perPage;
    Integer nextCursor;
    Integer prevCursor;
}
