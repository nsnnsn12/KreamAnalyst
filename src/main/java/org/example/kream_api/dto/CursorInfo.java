package org.example.kream_api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CursorInfo {
    private int cursor;
    private int next_cursor;
    private int prev_cursor;
    private int total;
}
