package com.mike724.motoapi.games;

import org.bukkit.entity.Player;

import java.util.*;

@SuppressWarnings("unused")
public class TeamManager extends PlayerManager {

    private HashMap<TeamMeta, List<Player>> teams;

    public TeamManager() {
        teams = new HashMap<>();
    }

    public List<Player> getTeam(String name) {
        for (Map.Entry<TeamMeta, List<Player>> entry : teams.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public List<Player> getTeam(TeamMeta teamMeta) {
        return teams.get(teamMeta);
    }

    public int getAmountOfTeams() {
        return teams.size();
    }

    public List<Player> addTeam(TeamMeta teamMeta) {
        if (teams.containsKey(teamMeta) || getTeam(teamMeta.getName()) != null) {
            return null;
        }

        List<Player> empty = new ArrayList<>();
        teams.put(teamMeta, empty);
        return this.getTeam(teamMeta);
    }

    @Override
    public void addPlayers(Player... playersArray) {
        List<Player> players = Arrays.asList(playersArray);
        int amtTeams = this.getAmountOfTeams();
        if (amtTeams == 0) {
            //No teams, default to adding them into a "null" team.
            this.addTeam(new TeamMeta("null")).addAll(players);
            return;
        } else if (amtTeams == 1) {
            //Seems silly to only have one team, but OK, we'll work with it.
            this.getTeam(teams.keySet().iterator().next()).addAll(players);
        } else {
            //There must be >1 teams, so continue as normal.
        }

        //Shuffle player list
        Collections.shuffle(players);

        //Sorted map, keys will go in ascending order (player count on that team)
        Map<Integer, TeamMeta> sorted = new TreeMap<>();
        for (Map.Entry<TeamMeta, List<Player>> entry : teams.entrySet()) {
            sorted.put(entry.getValue().size(), entry.getKey());
        }

        //foreach the player list and add each player to the team with the least players.
        for (Player p : players) {
            boolean added = false;
            for (Map.Entry<Integer, TeamMeta> option : sorted.entrySet()) {
                TeamMeta meta = option.getValue();
                if (meta.hasPlayerLimit()) {
                    if(option.getKey() < meta.getMaxPlayers()) {
                        this.getTeam(meta).add(p);
                        added = true;
                    } else {
                        continue;
                    }
                } else {
                    this.getTeam(meta).add(p);
                    added = true;
                }
            }
            if(!added) {
                //We couldn't find them a team. This could happen if all
                //teams have a limit and they are all full. Default to adding
                //the player into a special "overflow" team instead of failing.
                this.addTeam(new TeamMeta("overflow"));
                this.getTeam("overflow").add(p);
            }
        }
    }

    @Override
    public void removePlayers(Player... playersArray) {
        List<Player> players = Arrays.asList(playersArray);
        for (List<Player> team : teams.values()) {
            team.removeAll(players);
        }
    }

    @Override
    public void removeAllPlayers() {
        for (List<Player> team : teams.values()) {
            team.clear();
        }
    }

    @Override
    public Player[] getAllPlayers() {
        List<Player> all = new ArrayList<>();
        for (List<Player> team : teams.values()) {
            all.addAll(team);
        }
        return (Player[]) all.toArray();
    }
}
