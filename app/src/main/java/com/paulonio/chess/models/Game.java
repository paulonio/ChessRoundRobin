package com.paulonio.chess.models;


import java.io.Serializable;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class Game implements Serializable {

    @Setter
    private long dataBaseId;
    @NonNull
    Player whitePlayer;
    @NonNull
    Player blackPlayer;
    @Setter
    Score gameScore;
}
