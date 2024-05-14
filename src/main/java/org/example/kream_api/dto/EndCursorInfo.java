package org.example.kream_api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EndCursorInfo {
    String date;
    Long id;
    int endCursor;
    int endRow;
}
