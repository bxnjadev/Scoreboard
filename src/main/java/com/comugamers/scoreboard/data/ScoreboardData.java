package com.comugamers.scoreboard.data;

import java.util.List;
import java.util.Optional;

public interface ScoreboardData {

    List<String> getFields();

    Optional<Integer> getLine(String value);

    static ScoreboardData create(List<String> fields){
        return new SimpleScoreboardData(fields);
    }

}
