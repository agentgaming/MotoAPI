package com.mike724.motoapi;

import com.mike724.motoapi.games.TeamManager;
import com.mike724.motoapi.games.TeamMeta;

public class TeamTests {

    public static void main(String[] args) {
        String[] names = {"AgentMoto", "Dakota628", "AgentApplez", "xxMatthewo", "shyloft", "Notch"};
        TeamManager tm = new TeamManager();

        print("First print");
        print(tm.toString());

        tm.addTeam(new TeamMeta("red", 2));
        tm.addTeam(new TeamMeta("blue"));

        print("Added teams");
        print(tm.toString());

/*        tm.addPlayers(names[0], names[1]);

        print("Added two players");
        print(tm.toString());

        tm.addPlayers(names[2]);

        print("Added one player");
        print(tm.toString());

        tm.addPlayers(names[3]);

        print("Added one player");
        print(tm.toString());

        tm.addPlayers(names[4], names[5]);

        print("Added two players");
        print(tm.toString());

        tm.removePlayers(names[0], names[1]);

        print("Removed two players");
        print(tm.toString());

        tm.removeAllPlayers();

        print("Removed all players");
        print(tm.toString());
*/
        tm.addPlayers(names);

        print("Added ALL players");
        print(tm.toString());
    }

    public static void print(String s) {
        System.out.println(s);
    }

}