package com.comugamers.scoreboard;

import com.comugamers.scoreboard.board.Board;
import com.comugamers.scoreboard.data.ScoreboardData;
import com.comugamers.scoreboard.replacer.ScoreboardReplacer;

import java.util.UUID;

public interface ScoreboardUser {

    ScoreboardData getData();

    Board getBoard();

    ScoreboardReplacer getReplacer();

    UUID getId();

}
