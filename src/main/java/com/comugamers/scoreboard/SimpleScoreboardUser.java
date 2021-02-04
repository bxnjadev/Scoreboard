package com.comugamers.scoreboard;

import com.comugamers.scoreboard.board.Board;
import com.comugamers.scoreboard.data.ScoreboardData;
import com.comugamers.scoreboard.replacer.ScoreboardReplacer;

import java.util.UUID;

public class SimpleScoreboardUser implements ScoreboardUser {

    private final ScoreboardData scoreboardData;
    private final Board board;
    private final ScoreboardReplacer scoreboardReplacer;
    private final UUID id;

    public SimpleScoreboardUser(ScoreboardData scoreboardData, ScoreboardReplacer scoreboardReplacer,Board board, UUID id){
        this.scoreboardData = scoreboardData;
        this.scoreboardReplacer = scoreboardReplacer;
        this.board = board;
        this.id = id;
    }

    @Override
    public ScoreboardData getData() {
        return scoreboardData;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public ScoreboardReplacer getReplacer() {
        return scoreboardReplacer;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
