package com.paulonio.chess.dao;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class FidePlayer implements Serializable {

    private String fideId;
    private String name;
    private String surname;
    private String rating;

}
