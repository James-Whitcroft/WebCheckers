package com.webcheckers.Appl;

import com.webcheckers.Model.AIPlayer;
import com.webcheckers.Model.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerLobby {

    private HashMap<String, Player> lobby;

    /**
     * constructor creates an empty Lobby
     */
    public PlayerLobby() {
        lobby = new HashMap<>();
    }

    /**
     * Handles name validation and storage of Players
     *
     * @param name
     *      String representing a Players name
     * @return
     *      true on successful addition to Lobby else false
     */
    public synchronized boolean addPlayer(String name) {
        if(name.length() < 1 || name.length() > 25 || name.equals(" ")) { return false; }
        Pattern pattern = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher match = pattern.matcher(name);
        if(match.find()) { return false; }
        if(!lobby.containsKey(name.toLowerCase())) {
            lobby.put(name.toLowerCase(), new Player(name));
            return true;
        }
        return false;
    }

    public synchronized boolean addAIPlayer(String name, int difficulty) {
        if(name.length() < 1 || name.length() > 25 || name.equals(" ")) { return false; }
        Pattern pattern = Pattern.compile("[^A-Za-z0-9 ]");
        Matcher match = pattern.matcher(name);
        if(match.find()) { return false; }
        if(!lobby.containsKey(name.toLowerCase())) {
            AIPlayer player = new AIPlayer(name, difficulty);
            lobby.put(name.toLowerCase(), player);
            return true;
        }
        return false;
    }


    /**
     * Remove player from the lobby
     *
     * @param player
     *      the String to remove
     */
    public synchronized void removePlayer(String player) {
        lobby.remove(player.toLowerCase());
    }


    public synchronized Player getPlayer(String player) {
        if(lobby.containsKey(player.toLowerCase())) {
            return lobby.get(player.toLowerCase());
        }
        return null;
    }

    /**
     * Get all the active players in the Lobby
     * @return
     *      A collection of Player objects
     */
    public Collection<Player> getActivePlayers() {
        return lobby.values();
    }

    /**
     * Get a collection of active player's minus excluded player
     *
     * @param exclude
     *      current player to exclude
     * @return
     *      a String Collection of Player names
     */
    public Collection<String> getOtherActivePlayers(Player exclude) {
        Collection<String> temp = new ArrayList<>();
        lobby.values().forEach(player -> temp.add(player.getName()));
        temp.remove(exclude.getName());
        return temp;
    }

}
