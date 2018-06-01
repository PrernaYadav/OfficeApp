package software.starling.com.officeapp.model;

/**
 * Created by Admin on 4/21/2018.
 */

public class W_D_Machine {

    String description;
    String hours;
    String rate;
    String amount;
    String paid;
    String due_amount;
    String PKDailyMachineWorkId;


    public W_D_Machine(String PKDailyMachineWorkId, String description, String hours, String rate, String amount, String paid, String due_amount) {
        this.description = description;
        this.hours = hours;
        this.rate = rate;
        this.amount = amount;
        this.paid = paid;
        this.due_amount = due_amount;
        this.PKDailyMachineWorkId=PKDailyMachineWorkId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(String due_amount) {
        this.due_amount = due_amount;
    }

    public String getPKDailyMachineWorkId() {
        return PKDailyMachineWorkId;
    }

    public void setPKDailyMachineWorkId(String PKDailyMachineWorkId) {
        this.PKDailyMachineWorkId = PKDailyMachineWorkId;
    }
}
