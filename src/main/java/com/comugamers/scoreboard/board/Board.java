package com.comugamers.scoreboard.board;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;

public interface Board {

    void create();

    void destroy();

    void setObjectiveName(String name);

    void setLine(int line, String value);

    void removeLine(int line);

    String getLine(int line);

    VirtualTeam getTeam(int line);

    interface VirtualTeam {

        String getName();

        String getPrefix();

        String getSuffix();

        void setPrefix(String prefix);

        void setSuffix(String suffix);

        PacketPlayOutScoreboardTeam createTeam();

        PacketPlayOutScoreboardTeam updateTeam();

        PacketPlayOutScoreboardTeam removeTeam();

        void setPlayer(String name);

        void reset();

        Iterable<PacketPlayOutScoreboardTeam> sendLine();

        PacketPlayOutScoreboardTeam changePlayer();

        PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName);

        String getCurrentPlayer();

        default String getValue() {
            return getPrefix() + getCurrentPlayer() + getSuffix();
        }

        void setValue(String value);

    }

}
