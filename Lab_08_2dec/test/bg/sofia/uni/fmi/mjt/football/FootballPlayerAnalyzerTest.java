package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FootballPlayerAnalyzerTest {

    @Test
    void testFootballPlayerAnalyzerInitialization() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        assertEquals(3, fpa.getAllPlayers().size());
    }

    @Test
    void testGetAllNationalities() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        assertEquals(3, fpa.getAllNationalities().size());
    }

    @Test
    void testGroupByPosition() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        assertEquals(6, fpa.groupByPosition().size());
    }

    @Test
    void testGetHighestPaidPlayerByNationality() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        assertEquals("L. Messi", fpa.getHighestPaidPlayerByNationality("Argentina").name());
        assertEquals("Argentina", fpa.getHighestPaidPlayerByNationality("Argentina").nationality());
        assertThrows(NoSuchElementException.class, () -> fpa.getHighestPaidPlayerByNationality("Brazil"));
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudget() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        assertFalse(fpa.getTopProspectPlayerForPositionInBudget(Position.CAM, 80000000).isEmpty());
        assertTrue(fpa.getTopProspectPlayerForPositionInBudget(Position.CAM, 80000).isEmpty());
        assertEquals("P. Pogba", fpa.getTopProspectPlayerForPositionInBudget(Position.CAM, 80000000).get().name());
        assertThrows(IllegalArgumentException.class, () ->
            fpa.getTopProspectPlayerForPositionInBudget(null, 80000000).get().name());
    }

    @Test
    void testGetSimilarPlayers() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        Player pogba = fpa.getTopProspectPlayerForPositionInBudget(Position.CAM, 80000000).get();
        assertEquals(2, fpa.getSimilarPlayers(pogba).size());
        assertTrue(fpa.getSimilarPlayers(pogba).contains(pogba));
        assertThrows(IllegalArgumentException.class, () -> fpa.getSimilarPlayers(null).contains(pogba));
    }

    @Test
    void testGetPlayersByFullNameKeyword() {
        StringReader reader = new StringReader(
            "name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right\n" +
                "L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right\n" +
                "K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right\n" +
                "V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right\n" +
                "K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right\n" +
                "E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right\n" +
                "Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right\n" +
                "T. Courtois;Thibaut Courtois;5/11/1992;26;198.12;96.2;GK;Belgium;89;90;53500000;240000;Left\n" +
                "M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right\n" +
                "A. Griezmann;Antoine Griezmann;3/21/1991;27;175.26;73;CF,ST;France;89;90;78000000;145000;Left\n" +
                "M. Salah;Mohamed  Salah Ghaly;6/15/1992;26;175.26;71.2;RW,ST;Egypt;89;90;78500000;265000;Left\n" +
                "P. Dybala;Paulo Bruno Exequiel Dybala;11/15/1993;25;152.4;74.8;CAM,RW;Argentina;89;94;89000000;205000;Left\n" +
                "M. Škriniar;Milan Škriniar;2/11/1995;24;187.96;79.8;LWB;Slovakia;86;93;53500000;89000;Right\n" +
                "Fernandinho;Fernando Luiz Rosa;5/4/1985;33;152.4;67.1;LM;Brazil;87;87;20500000;200000;Right\n" +
                "G. Higuaín;Gonzalo Gerardo Higuaín;12/10/1987;31;185.42;88.9;ST;Argentina;87;87;48500000;205000;Right\n" +
                "I. Rakitić;Ivan Rakitić;3/10/1988;30;182.88;78;CM,CDM;Croatia;87;87;46500000;260000;Right\n" +
                "J. Vertonghen;Jan Vertonghen;4/24/1987;31;187.96;86.2;RB;Belgium;87;87;34000000;155000;Left\n" +
                "D. Mertens;Dries Mertens;5/6/1987;31;170.18;60.8;CF,ST;Belgium;87;87;45000000;135000;Right\n" +
                "Marcelo;Marcelo Vieira da Silva Júnior;5/12/1988;30;175.26;79.8;LB;Brazil;87;87;36500000;240000;Left\n" +
                "T. Alderweireld;Toby Alderweireld;3/2/1989;30;185.42;81.2;CB;Belgium;87;88;44000000;165000;Right");
        FootballPlayerAnalyzer fpa = new FootballPlayerAnalyzer(reader);
        Player pogba = fpa.getTopProspectPlayerForPositionInBudget(Position.CAM, 80000000).get();
        assertTrue(fpa.getPlayersByFullNameKeyword("Pogb").contains(pogba));
        assertThrows(IllegalArgumentException.class, () -> fpa.getPlayersByFullNameKeyword(null));
    }

}
