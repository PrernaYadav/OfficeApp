package software.starling.com.officeapp.model;

/**
 * Created by Admin on 4/24/2018.
 */

public class Hidrance {
    String customer;
    String issues;
    String PKProblemsId;

    public Hidrance(String PKProblemsId, String customer, String issues) {
        this.customer = customer;
        this.issues = issues;
        this.PKProblemsId=PKProblemsId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getPKProblemsId() {
        return PKProblemsId;
    }

    public void setPKProblemsId(String PKProblemsId) {
        this.PKProblemsId = PKProblemsId;
    }
}
