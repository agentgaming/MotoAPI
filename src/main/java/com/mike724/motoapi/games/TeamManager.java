package com.mike724.motoapi.games;

import com.mike724.motoapi.MapUtil;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.Map;

@SuppressWarnings("unused")
public class TeamManager implements PlayerManager {

    private HashMap<TeamMeta, List<Player>> teams;

    public TeamManager() {
        teams = new HashMap<>();
    }

    public String toString() {
        String s = "";
        for (Map.Entry<TeamMeta, List<Player>> entry : teams.entrySet()) {
            TeamMeta meta = entry.getKey();
            s += "##### " + meta.getName() + "\n";
            for (Player p : entry.getValue()) {
                s += "## " + p.getName() + "\n";
            }
            s += "##### \n";
        }
        return s;
    }

    public List<Player> getTeam(String name) {
        for (Map.Entry<TeamMeta, List<Player>> entry : teams.entrySet()) {
            if (entry.getKey().getName().equals(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public String[] getTeamNames() {
        String[] names = new String[teams.size()];
        int i = 0;
        for (TeamMeta tm : teams.keySet()) {
            names[i++] = tm.getName();
        }
        return names;
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

    public boolean onSameTeam(Player... playersArray) {
        List<Player> players = Arrays.asList(playersArray);
        for (String teamName : this.getTeamNames()) {
            List<Player> team = this.getTeam(teamName);
            if (team.containsAll(players)) {
                return true;
            }
        }
        return false;
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

        //Sorted map, keys will go in ascending order (player count on that team)\
        HashMap<TeamMeta, Integer> map = new HashMap<>();
        for (java.util.Map.Entry<TeamMeta, List<Player>> entry : teams.entrySet()) {
            map.put(entry.getKey(), entry.getValue().size());
        }

        Map<TeamMeta, Integer> sorted = MapUtil.sortByValue(map);

        //foreach the player list and add each player to the team with the least players.
        for (Player p : players) {
            boolean added = false;
            for (Map.Entry<TeamMeta, Integer> option : sorted.entrySet()) {
                TeamMeta meta = option.getKey();
                if (meta.hasPlayerLimit()) {
                    if (option.getValue() < meta.getMaxPlayers()) {
                        this.getTeam(meta).add(p);
                        added = true;
                    } else {
                        continue;
                    }
                } else {
                    this.getTeam(meta).add(p);
                    added = true;
                }
                //Increment and resort map
                if (added) {
                    map.put(meta, map.get(meta) + 1);
                    break;
                }
            }
            if (!added) {
                //We couldn't find them a team. This could happen if all
                //teams have a limit and they are all full. Default to adding
                //the player into a special "overflow" team instead of failing.
                this.addTeam(new TeamMeta("overflow"));
                this.getTeam("overflow").add(p);
            } else {
                sorted = MapUtil.sortByValue(map);
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

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;

    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
