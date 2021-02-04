package com.comugamers.scoreboard.data;

import java.util.List;
import java.util.Optional;

public class SimpleScoreboardData implements ScoreboardData {

    private final List<String> fields;

    public SimpleScoreboardData(List<String> fields){
        this.fields = fields;
    }

    @Override
    public List<String> getFields() {
        return fields;
    }

    @Override
    public Optional<Integer> getLine(String value) {

        Optional<String> stringOptional =
                fields.stream().filter(line -> line.contains(value)).findFirst();

        return stringOptional.map(fields::indexOf);

    }
}
