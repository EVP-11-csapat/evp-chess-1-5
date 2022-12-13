package chess15.engine;

import chess15.gamemode.Gamemode;

public class RuleSet {
    public static RuleSet instance = null;

    public int startTime; //minutes
    public int timeDelta; //seconds

    public boolean timer;       //true if enabled
    public boolean castling;    //true if enabled
    public boolean enpassant;   //true if enabled
    public boolean promotion;   //true if enabled

    public Gamemode gamemode;   //gamemode reference

    public static RuleSet getInstance() {
        if (instance == null) instance = new RuleSet();
        return instance;
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "startTime=" + startTime +
                ", timeDelta=" + timeDelta +
                ", timer=" + timer +
                ", castling=" + castling +
                ", enpassant=" + enpassant +
                ", promotion=" + promotion +
                ", gamemode=" + gamemode +
                '}';
    }
}
