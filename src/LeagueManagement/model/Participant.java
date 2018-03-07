package LeagueManagement.model;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Participant {
    public final StringProperty participantName;
    public final IntegerProperty participantId;
    public final IntegerProperty W = new SimpleIntegerProperty(0);
    public final IntegerProperty D = new SimpleIntegerProperty(0);
    public final IntegerProperty L = new SimpleIntegerProperty(0);
    public final IntegerProperty Pts = new SimpleIntegerProperty(0);
    public final IntegerProperty Pld = new SimpleIntegerProperty(0);


    public Participant(String participantName, int participantId) {
        this.participantName = new SimpleStringProperty(participantName);
        this.participantId = new SimpleIntegerProperty(participantId);
    }

    public String getParticipantName() {
        return this.participantName.get();
    }

    public StringProperty getTeamNameStringProperty(){
        return this.participantName;
    }

    public IntegerProperty getPldIntegerProperty() {
        return this.Pld;
    }

    public void setParticipantName(String name) {
        this.participantName.set(name);
    }

    public void setW(int W) {
        this.W.set(W);
    }

    public int getW() {
        return this.W.get();
    }

    public IntegerProperty getWIntegerProperty() {
        return this.W;
    }

    public void setD(int D){
        this.D.set(D);
    }

    public int getD() {
        return this.D.get();
    }

    public IntegerProperty getDIntegerProperty() {
        return this.D;
    }

    public void setL(int L) {
        this.L.set(L);
    }

    public int getL() {
        return this.L.get();
    }

    public IntegerProperty getLIntegerProperty(){
        return this.L;
    }

    public void setPts(int Pts){
        this.Pts.set(Pts);
    }

    public int getPts() {
        return this.Pts.get();
    }

    public IntegerProperty getPtsIntegerProperty(){
        return this.Pts;
    }

    public int getParticipantId() {
        return this.participantId.get();
    }

    public void addW() {
        this.W.set(this.W.get() + 1);
        incPld();
        calculatePoints();
    }

    public void addD() {
        this.D.set(this.D.get() + 1);
        incPld();
        calculatePoints();
    }

    public void addL() {
        this.L.set(this.L.get() + 1);
        incPld();
    }

    public void incPld() {
        this.Pld.set(this.Pld.get() + 1);
    }

    private void calculatePoints() {
        this.Pts.set(this.W.get() * 3 + this.D.get());
    }

    public void clearResults() {
        this.Pts.set(0);
        this.Pld.set(0);
        this.W.set(0);
        this.D.set(0);
        this.L.set(0);
    }

}
