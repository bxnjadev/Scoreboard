package com.comugamers.scoreboard.board;

import com.comugamers.scoreboard.util.ReflectionUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleBoard implements Board {

    private boolean created;
    private final VirtualTeam[] lines = new VirtualTeam[15];
    private final UUID uuidPlayer;
    private String objectiveName;

    public SimpleBoard(Player player, String objectiveName){
        uuidPlayer = player.getUniqueId();
        this.objectiveName = objectiveName;

        created = false;

    }

    @Override
    public void create() {

        if(created){
            return;
        }

        PlayerConnection playerConnection = getConnection();
        playerConnection.sendPacket(createObjectivePacket(0,objectiveName));
        playerConnection.sendPacket(setObjectiveSlot());

        for(int i=0; i<lines.length;i++){
            sendLine(i);
        }
        
        created = true;
    }

    @Override
    public void destroy() {

        if(!created){
            return;
        }

        getConnection().sendPacket(createObjectivePacket(1,null));

        for (VirtualTeam team : lines) {

            if(team == null) {
                continue;
            }

            getConnection().sendPacket(team.removeTeam());
        }

        created = false;
    }

    @Override
    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;

        if(created){
            getConnection().sendPacket(createObjectivePacket(2,objectiveName));
        }

    }

    @Override
    public void setLine(int line, String value) {

        VirtualTeam virtualTeam = getTeam(line);

        if((virtualTeam.getCurrentPlayer() != null) && (created)){
            getConnection().sendPacket(removeLine(
				virtualTeam.getCurrentPlayer()
			));
        }

        virtualTeam.setValue(value);
        sendLine(line);
    }

    @Override
    public void removeLine(int line) {

        VirtualTeam virtualTeam = getOrCreateTeam(line);
        String old = virtualTeam.getCurrentPlayer();

        if((old != null) && (created)){
            getConnection().sendPacket(removeLine(old));
            getConnection().sendPacket(virtualTeam.removeTeam());
        }
        lines[line] = null;

    }

    @Override
    public VirtualTeam getTeam(int line) {
        if(line > 14){
            return null;
        }

        if(line < 0){
            return null;
        }

        return getOrCreateTeam(line);
    }

    @Override
    public String getLine(int line) {
        return getTeam(line).getValue();
    }

    private void sendLine(int line) {

        if (line > 14) {
            System.out.println("aa");
            return;
        }
        if (line < 0) {
            System.out.println("bb");
            return;
        }

        if (!this.created) {
            return;
        }

        int score = 15 - line;
        VirtualTeam virtualTeam = getOrCreateTeam(line);
        for (Packet packet : virtualTeam.sendLine()) {
            System.out.println("Packet enviado");
            getConnection().sendPacket(packet);
        }

        getConnection().sendPacket(sendScore(virtualTeam.getCurrentPlayer(), score));
        virtualTeam.reset();
    }

    private VirtualTeam getOrCreateTeam(int line) {
        if (this.lines[line] == null) {
            this.lines[line] = new SimpleVirtualTeam("__fakeScore" + line);
        }
        
        return this.lines[line];
    }

    private PlayerConnection getConnection() {
        Player player = Bukkit.getPlayer(uuidPlayer);
        return ((CraftPlayer)player).getHandle().playerConnection;
    }

    private PacketPlayOutScoreboardObjective createObjectivePacket(int mode, String displayName) {

        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

        ReflectionUtil.setField(packet, "a", Bukkit.getPlayer(uuidPlayer).getName());
        ReflectionUtil.setField(packet, "d", Integer.valueOf(mode));

        if ((mode == 0) || (mode == 2)) {

            ReflectionUtil.setField(packet, "b", displayName);
            ReflectionUtil.setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
        }

        return packet;
    }

    private PacketPlayOutScoreboardDisplayObjective setObjectiveSlot() {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

        ReflectionUtil.setField(packet, "a", Integer.valueOf(1));
        ReflectionUtil.setField(packet, "b", Bukkit.getPlayer(uuidPlayer).getName());

        return packet;
    }

    private PacketPlayOutScoreboardScore sendScore(String line, int score) {

        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);

        ReflectionUtil.setField(packet, "b", Bukkit.getPlayer(uuidPlayer).getName());
        ReflectionUtil.setField(packet, "c", Integer.valueOf(score));
        ReflectionUtil.setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);

        return packet;
    }

    private PacketPlayOutScoreboardScore removeLine(String line) {
        return new PacketPlayOutScoreboardScore(line);
    }

    private static class SimpleVirtualTeam implements VirtualTeam {

        private final String name;
        private String prefix;
        private String suffix;
        private String currentPlayer;
        private String oldPlayer;
        private boolean prefixChanged;
        private boolean suffixChanged;
        private boolean playerChanged;
        private boolean first;

        public SimpleVirtualTeam(String name, String prefix, String suffix){

            this.name = name;
            this.prefix = prefix;
            this.suffix = suffix;

            playerChanged = false;
            first = true;

        }

        public SimpleVirtualTeam(String name) {
            this(name,"","");
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getPrefix() {
            return prefix;
        }

        @Override
        public String getSuffix() {
            return suffix;
        }

        @Override
        public void setPrefix(String prefix) {
            if ((this.prefix == null) || (!this.prefix.equals(prefix))) {
                this.prefixChanged = true;
            }
            this.prefix = prefix;
        }

        @Override
        public void setSuffix(String suffix) {
            if ((this.suffix == null) || (!this.suffix.equals(this.prefix))) {
                this.suffixChanged = true;
            }
            this.suffix = suffix;
        }

        @Override
        public PacketPlayOutScoreboardTeam createTeam() {
            return createTeamPacket(0);
        }

        @Override
        public PacketPlayOutScoreboardTeam updateTeam() {
            return createTeamPacket(2);
        }

        @Override
        public PacketPlayOutScoreboardTeam removeTeam() {

            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();

            ReflectionUtil.setField(packet, "a", name);
            ReflectionUtil.setField(packet, "h", 1);
            this.first = true;

            return packet;
        }

        @Override
        public void setPlayer(String name) {
            if ((this.currentPlayer == null) || (!this.currentPlayer.equals(name))) {
                this.playerChanged = true;
            }
            this.oldPlayer = this.currentPlayer;
            this.currentPlayer = name;
        }

        @Override
        public void reset() {
            this.prefixChanged = false;
            this.suffixChanged = false;
            this.playerChanged = false;
            this.oldPlayer = null;
        }

        @Override
        public Iterable<PacketPlayOutScoreboardTeam> sendLine() {

            List<PacketPlayOutScoreboardTeam> packets = new ArrayList<>();

            if (this.first) {
                packets.add(createTeam());
            } else if ((this.prefixChanged) || (this.suffixChanged)) {
                packets.add(updateTeam());
            }

            if ((this.first) || (this.playerChanged)) {

                if (this.oldPlayer != null) {
                    packets.add(addOrRemovePlayer(4, this.oldPlayer));
                }

                packets.add(changePlayer());
            }
            if (this.first) {
                this.first = false;
            }
            return packets;
        }

        @Override
        public PacketPlayOutScoreboardTeam changePlayer() {
            return addOrRemovePlayer(3, this.currentPlayer);
        }

        @Override
        public PacketPlayOutScoreboardTeam addOrRemovePlayer(int mode, String playerName) {

            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            ReflectionUtil.setField(packet, "a", this.name);
            ReflectionUtil.setField(packet, "h", Integer.valueOf(mode));

            try {
                Field f = packet.getClass().getDeclaredField("g");
                f.setAccessible(true);
                ((List)f.get(packet)).add(playerName);
            } catch (NoSuchFieldException|IllegalAccessException e) {
                e.printStackTrace();
            }
            return packet;
        }

        @Override
        public String getCurrentPlayer() {
            return currentPlayer;
        }

        @Override
        public void setValue(String value) {

            if (value.length() <= 16) {
                setPrefix("");
                setSuffix("");
                setPlayer(value);
                return;
            }

            if (value.length() <= 32) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16));
                setSuffix("");
                return;
            }

            if (value.length() <= 48) {
                setPrefix(value.substring(0, 16));
                setPlayer(value.substring(16, 32));
                setSuffix(value.substring(32));
            }
            else {
                throw new IllegalArgumentException("Too long value ! Max 48 characters, value was " + value.length() + " !");
            }
        }

        private PacketPlayOutScoreboardTeam createTeamPacket(int mode) {

            PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
            ReflectionUtil.setField(packet, "a", this.name);
            ReflectionUtil.setField(packet, "h", Integer.valueOf(mode));
            ReflectionUtil.setField(packet, "b", "");
            ReflectionUtil.setField(packet, "c", this.prefix);
            ReflectionUtil.setField(packet, "d", this.suffix);
            ReflectionUtil.setField(packet, "i", Integer.valueOf(0));
            ReflectionUtil.setField(packet, "e", "always");
            ReflectionUtil.setField(packet, "f", Integer.valueOf(0));

            return packet;
        }

    }

}
