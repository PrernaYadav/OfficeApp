package software.starling.com.officeapp.model;

/**
 * Created by Admin on 4/24/2018.
 */

public class Pretty_cash {
    String description;
    String recv;
    String paid;
    String blance;
    String PKPettyCashId;

    public Pretty_cash(String PKPettyCashId, String description, String recv, String paid, String blance) {
        this.description = description;
        this.recv = recv;
        this.paid = paid;
        this.blance = blance;
        this.PKPettyCashId=PKPettyCashId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecv() {
        return recv;
    }

    public void setRecv(String recv) {
        this.recv = recv;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getBlance() {
        return blance;
    }

    public void setBlance(String blance) {
        this.blance = blance;
    }

    public String getPKPettyCashId() {
        return PKPettyCashId;
    }

    public void setPKPettyCashId(String PKPettyCashId) {
        this.PKPettyCashId = PKPettyCashId;
    }
}
