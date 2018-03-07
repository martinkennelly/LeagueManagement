package LeagueManagement.model;

import LeagueManagement.utilities.FileUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;

public class League {
    public ObservableList<Participant> participantData = FXCollections.observableArrayList();
    public ObservableList<Fixture> fixtureData = FXCollections.observableArrayList();
    private StringProperty leagueName;
    private IntegerProperty numberOfParticipants;
    private final IntegerProperty leagueId;
    private final IntegerProperty adminId;
    private boolean hasResults = false;

    public League(String leagueName, int numberOfParticipants, int leagueId, int adminId) {
        this.leagueName = new SimpleStringProperty(leagueName);
        this.numberOfParticipants = new SimpleIntegerProperty(numberOfParticipants);
        this.leagueId = new SimpleIntegerProperty(leagueId);
        this.adminId = new SimpleIntegerProperty(adminId);
    }
    //Just for testing, delete later
    public void generateDummyParticipants() {
        int amount = this.numberOfParticipants.get();
        if (amount % 2 == 1) {
            amount++;
        }
        for (int i = 0; i < amount; i++) {
            participantData.add(new Participant("Participant " + (i + 1),i+1));
        }
    }

    //Generate simple fixtures todo
    public void generateFixtures() {
        fixtureData.clear();
        //todo ~ better implememt
        int numberOfTeams = participantData.size();
        int totalRounds = numberOfTeams - 1;
        int matchesPerRound = numberOfTeams / 2;
        String[][] rounds = new String[totalRounds][matchesPerRound];

        for (int round = 0; round < totalRounds; round++) {
            for (int match = 0; match < matchesPerRound; match++) {
                int home = (round + match) % (numberOfTeams - 1);
                int away = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1);
                if (match == 0) {
                    away = numberOfTeams - 1;
                }
                rounds[round][match] = (home + 1) + " v " + (away + 1);

            }
        }

        //Interleaved
        String[][] interleaved = new String[totalRounds][matchesPerRound];
        int even = 0;
        int odd = (numberOfTeams/2);
        for (int i = 0; i < rounds.length; i++) {
            if (i % 2 == 0) {
                interleaved[i] = rounds[even++];
            } else {
                interleaved[i] = rounds[odd++];
            }
        }
        rounds = interleaved;
        for (int round = 0; round < rounds.length; round++) {
            if (round % 2 == 1) {
                rounds[round][0] = flip(rounds[round][0]);
            }
        }
        //todo only does half seasons, needs mirror
        for (int roundNo = 0; roundNo < totalRounds ; roundNo++) {
            for (int match = 0; match < matchesPerRound; match++) {
                int homeId = Integer.parseInt(rounds[roundNo][match].split(" v ")[0]);
                int awayId = Integer.parseInt(rounds[roundNo][match].split(" v ")[1]);
                fixtureData.add(new Fixture(((roundNo+1) * 100) + match + 1,homeId,awayId,getParticipantName(homeId),getParticipantName(awayId)));
            }
        }
    }

    public void generateTable() {
        clearPartisipantsResults();
        for (Fixture each : fixtureData) {
            if (each.getAwayResult() != null && each.getHomeResult() != null) {
                int res = each.getHomeResult().compareTo(each.getAwayResult());
                if (res == 0) {
                    getParticipant(each.getHomeId()).addD();
                    getParticipant(each.getAwayId()).addD();
                } else if (res < 0) {
                    getParticipant(each.getAwayId()).addW();
                    getParticipant(each.getHomeId()).incPld();
                } else {
                    getParticipant(each.getHomeId()).addW();
                    getParticipant(each.getAwayId()).incPld();
                }
            }
        }
    }

    private static String flip(String match) {
        String[] components = match.split(" v ");
        return components[1] + " v " + components[0];
    }

    public String getParticipantName(int participantId) {
        String participantName = null;
        if (participantData.size() != 0) {
            for (Participant each : participantData) {
                if (each.getParticipantId() == participantId) {
                    participantName = each.getParticipantName();
                }
            }
        }
        return participantName;
    }

    public Participant getParticipant(int participantId) {
        for (Participant participant : participantData) {
            if (participant.getParticipantId() == participantId) {
                return participant;
            }
        }
        return null;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants.set(numberOfParticipants);
    }

    public int getNumberOfParticipants() {
        this.numberOfParticipants.set(this.participantData.size());
        return this.numberOfParticipants.get();
    }

    public IntegerProperty getAdminId(){
        return this.adminId;
    }

    public IntegerProperty getLeagueId() {
        return this.leagueId;
    }

    public String getLeagueName() {
        return leagueName.get();
    }

    public StringProperty getLeagueNameProperty() {
        return this.leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName.set(leagueName);
    }

    public ObservableList<Participant> getParticipantData() {
        return this.participantData;
    }

    public IntegerProperty getNumberOfParticipantsProperty(){
        this.numberOfParticipants.set(this.participantData.size());
        return this.numberOfParticipants;
    }

    public ObservableList<Fixture> getFixtureData() {
        return this.fixtureData;
    }

    public void clearPartisipantsResults() {
        for(Participant participant : this.participantData) {
            participant.clearResults();
        }
    }

    public boolean getHasResults() {
        return this.hasResults;
    }

    public void setHasResults(boolean hasResults) {
        this.hasResults = hasResults;
    }

    public void takeADump() {
        File file = new File(this.getLeagueId().get() + "_Fixtures.csv");
        StringBuilder sb = new StringBuilder();

        if (fixtureData.size() != 0) {
            for (Fixture fixture : fixtureData) {
                sb.append(fixture.getFixtureId() + "," + fixture.getHomeId() + "," + fixture.getAwayId() + "\r\n");
            }
            FileUtils.writeFile(file,sb.substring(0, sb.length()-2));
        }
        sb.delete(0,sb.length());
        file = new File(this.getLeagueId().get() + "_Partisipants.csv");
        if (participantData.size() != 0) {
            for (Participant participant : participantData) {
                sb.append(participant.getParticipantId() + "," + participant.getParticipantName() + "\r\n");
            }
            FileUtils.writeFile(file,sb.substring(0,sb.length() - 2));
        }
        sb.delete(0,sb.length());
        file = new File(this.getLeagueId().get() + "_Results.csv");
        if (fixtureData.size() != 0) {
            for (Fixture fixture : fixtureData) {
                sb.append(fixture.getFixtureId() + "," + fixture.getHomeResult() + "," + fixture.getAwayResult() + "\r\n");
            }
            FileUtils.writeFile(file,sb.substring(0,sb.length()- 2));
        }

    }

    public void addParticipant(Participant participant) {
        this.participantData.add(participant);
        this.numberOfParticipants.set(this.participantData.size());
    }

    public void addFixture(Fixture fixture) {
        this.fixtureData.add(fixture);
    }

    public Fixture findFixture(int fixtureId) {
        Fixture result = null;
        for (Fixture fixture : this.fixtureData) {
            if (fixture.getFixtureId() == fixtureId) {
                result = fixture;
            }
        }
        return result;
    }

    public void updateFixturesNames() {
        for (Participant participant : this.getParticipantData()) {
            for (Fixture fixture : this.getFixtureData()) {
                if (participant.getParticipantId() == fixture.getAwayId() && !participant.getParticipantName().equals(fixture.getAwayParticipantName())) {
                    fixture.setAwayParticipantName(participant.getParticipantName());
                } else if (participant.getParticipantId() == fixture.getHomeId() && !participant.getParticipantName().equals(fixture.getHomeParticipantName())) {
                    fixture.setHomeParticipantName(participant.getParticipantName());
                }
            }
        }
    }
}
