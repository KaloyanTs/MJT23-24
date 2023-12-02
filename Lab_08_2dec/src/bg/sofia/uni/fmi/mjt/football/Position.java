package bg.sofia.uni.fmi.mjt.football;

public enum Position {
    // Diagram: https://fifauteam.com/fifa-21-positions/

    ST, // Striker
    LM, // Left Midfielder
    CF, // Centre Forward
    GK, // Goalkeeper
    RW, // Right Winger
    CM, // Centre Midfielder
    LW, // Left Winger
    CDM, // Defensive Midfielder
    CAM, // Attacking midfielder
    RB, // Right back
    LB, // Left back
    LWB, // Left Wing-back
    RM, // Right Midfielder
    RWB, // Right Wing-back
    CB; // Centre Back

    public static Position fromString(String str) {
        return switch (str) {
            case "ST" -> ST;
            case "LM" -> LM;
            case "CF" -> CF;
            case "GK" -> GK;
            case "RW" -> RW;
            case "CM" -> CM;
            case "LW" -> LW;
            case "CDM" -> CDM;
            case "CAM" -> CAM;
            case "RB" -> RB;
            case "LB" -> LB;
            case "LWB" -> LWB;
            case "RM" -> RM;
            case "RWB" -> RWB;
            case "CB" -> CB;
            default -> throw new IllegalArgumentException("String cannot be converted to position...");
        };
    }
}