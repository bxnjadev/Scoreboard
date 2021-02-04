package com.comugamers.scoreboard.handler;

import com.comugamers.scoreboard.ScoreboardUser;
import com.comugamers.scoreboard.SimpleScoreboardUser;
import com.comugamers.scoreboard.board.Board;
import com.comugamers.scoreboard.board.SimpleBoard;
import com.comugamers.scoreboard.data.ScoreboardData;
import com.comugamers.scoreboard.replacer.ScoreboardReplacer;
import com.comugamers.scoreboard.util.UtilColor;
import org.bukkit.entity.Player;

import java.util.*;

public class SimpleScoreboardHandler implements ScoreboardHandler {

    private final Map<UUID,ScoreboardUser> scoreboards;

    public SimpleScoreboardHandler(){
        scoreboards = new HashMap<>();
    }

    @Override
    public void setScoreboard(Player player, String title, ScoreboardData scoreboardData, ScoreboardReplacer scoreboardReplacer) {

        UUID id = player.getUniqueId();

        if(scoreboards.containsKey(id)){
            breakScoreboard(player);
        }

        Board board = new SimpleBoard(player, UtilColor.colorize(title));
        applyBoard(player,board,scoreboardData.getFields(),scoreboardReplacer);

        ScoreboardUser scoreboardUser = new SimpleScoreboardUser(scoreboardData,scoreboardReplacer,board,id);
        scoreboards.put(id,scoreboardUser);
    }

    @Override
    public void breakScoreboard(Player player) {
        getScoreboard(player).ifPresent(scoreboard -> {
            scoreboard.getBoard().destroy();
            scoreboards.remove(player.getUniqueId());
        });
    }

    @Override
    public Optional<ScoreboardUser> getScoreboard(Player player) {
        return Optional.ofNullable(scoreboards.get(player.getUniqueId()));
    }

    @Override
    public void updateField(Player player, String field) {
        getScoreboard(player).ifPresent(scoreboard -> scoreboard.getData().getLine(field).ifPresent(line -> scoreboard.getBoard().setLine(line,scoreboard.getReplacer().replace(player,scoreboard.getData().getFields().get(line)))));
    }

    @Override
    public void updateTitle(Player player, String title) {
        getScoreboard(player).ifPresent(scoreboard -> scoreboard.getBoard().setObjectiveName(UtilColor.colorize(title)));
    }

    private void applyBoard(Player player, Board board, List<String> lines, ScoreboardReplacer scoreboardReplacer){

        board.create();

        final int[] i = {0};
        lines.forEach(line -> {
            System.out.println(i[0]);
            System.out.println("linea " + line);
            board.setLine(i[0],scoreboardReplacer.replace(player,line));
            i[0]++;
        });
    }

}
