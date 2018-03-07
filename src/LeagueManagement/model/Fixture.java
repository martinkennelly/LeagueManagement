package LeagueManagement.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Fixture {
    private final SimpleIntegerProperty fixtureId;
    private final SimpleIntegerProperty homeId;
    private final SimpleIntegerProperty awayId;
    private SimpleIntegerProperty homeResult = null;
    private SimpleIntegerProperty awayResult = null;
    private SimpleStringProperty homeParticipantName;
    private SimpleStringProperty awayParticipantName;

    public Fixture(int fixtureId, int homeId, int awayId) {
        this.fixtureId = new SimpleIntegerProperty(fixtureId);
        this.homeId = new SimpleIntegerProperty(homeId);
        this.awayId = new SimpleIntegerProperty(awayId);
    }

    public Fixture(int fixtureId, int homeId, int awayId, String homeName, String awayName) {
        this(fixtureId,homeId,awayId);
        this.homeParticipantName = new SimpleStringProperty(homeName);
        this.awayParticipantName = new SimpleStringProperty(awayName);
    }

    public String getHomeParticipantName() {
        return this.homeParticipantName.get();
    }

    public StringProperty homeParticipantNameProperty() {
        return this.homeParticipantName;
    }

    public StringProperty awayParticipantNameProperty() {
        return this.awayParticipantName;
    }

    public String getAwayParticipantName() {
        return this.awayParticipantName.get();
    }

    public void setHomeParticipantName(String homeParticipantName) {
        this.homeParticipantName.set(homeParticipantName);
    }

    public void setAwayParticipantName(String awayParticipantName) {
        this.awayParticipantName.set(awayParticipantName);
    }

    public int getFixtureId() {
        return this.fixtureId.get();
    }

    public int getHomeId() {
        return this.homeId.get();
    }

    public int getAwayId() {
        return this.awayId.get();
    }

    public Integer getHomeResult() {
        if (this.homeResult == null) {
            return null;
        }
        return this.homeResult.get();
    }

    public Integer getAwayResult() {
        if (this.awayResult == null) {
            return null;
        }
        return this.awayResult.get();
    }

    public IntegerProperty getFixtureIdIntegerProperty() {
        return this.fixtureId;
    }

    public IntegerProperty getHomeIdIntegerProperty() {
        return this.homeId;
    }

    public IntegerProperty getAwayIdIntegerProperty() {
        return this.awayId;
    }

    public IntegerProperty getHomeResultIntegerProperty() {
        return this.homeResult;
    }

    public IntegerProperty getAwayResultIntegerProperty() {
        return this.awayResult;
    }

    public void setHomeResult(Integer result){
        this.homeResult = new SimpleIntegerProperty(result);
    }

    public void setAwayResult(Integer result) {
        this.awayResult = new SimpleIntegerProperty(result);
    }



}
