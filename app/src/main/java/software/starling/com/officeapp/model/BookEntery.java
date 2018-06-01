package software.starling.com.officeapp.model;

/**
 * Created by Harsh on 4/24/2018.
 */

public class BookEntery {
    String desription;
    String nos;
    String mesuureL;
    String mesuremnetB;
    String messuremnetH;
    String ques;
    String spinner_pos;
    String PKMeasurementBookId;

    public BookEntery( String description, String nos, String spinner_pos, String l, String b, String h, String qtys) {
    }

    public BookEntery(String PKMeasurementBookId,String desription, String nos, String spinner_pos, String mesuureL, String mesuremnetB, String messuremnetH, String ques) {
        this.desription = desription;
        this.nos = nos;
        this.mesuureL = mesuureL;
        this.mesuremnetB = mesuremnetB;
        this.messuremnetH = messuremnetH;
        this.ques = ques;
        this.spinner_pos=spinner_pos;
        this.PKMeasurementBookId=PKMeasurementBookId;
    }

    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public String getNos() {
        return nos;
    }

    public void setNos(String nos) {
        this.nos = nos;
    }

    public String getMesuureL() {
        return mesuureL;
    }

    public void setMesuureL(String mesuureL) {
        this.mesuureL = mesuureL;
    }

    public String getMesuremnetB() {
        return mesuremnetB;
    }

    public void setMesuremnetB(String mesuremnetB) {
        this.mesuremnetB = mesuremnetB;
    }

    public String getMessuremnetH() {
        return messuremnetH;
    }

    public void setMessuremnetH(String messuremnetH) {
        this.messuremnetH = messuremnetH;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getSpinner_pos() {
        return spinner_pos;
    }

    public void setSpinner_pos(String spinner_pos) {
        this.spinner_pos = spinner_pos;
    }

    public String getPKMeasurementBookId() {
        return PKMeasurementBookId;
    }

    public void setPKMeasurementBookId(String PKMeasurementBookId) {
        this.PKMeasurementBookId = PKMeasurementBookId;
    }
}
