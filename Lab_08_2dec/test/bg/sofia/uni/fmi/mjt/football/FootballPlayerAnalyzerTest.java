package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class FootballPlayerAnalyzerTest {

    @Test
    void f() {

        fail();
    }

    public static void main(String[] args) {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        System.out.println(fpa.getAllPlayers());
    }
}
