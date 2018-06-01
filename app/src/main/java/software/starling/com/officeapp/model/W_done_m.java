package software.starling.com.officeapp.model;

/**
 * Created by Harsh on 4/19/2018.
 */

public class W_done_m {
    String Work_Name;
    String Ski_No;
    String Ski_Payment;
    String U_ski_No;
    String Un_Ski_payment;
    String Agency;
    String Amount;
    String PKDailyManWorkId;

    public W_done_m(String PKDailyManWorkId, String work_Name, String ski_No, String ski_Payment, String u_ski_No, String un_Ski_payment, String agency, String amount) {
        Work_Name = work_Name;
        Ski_No = ski_No;
        Ski_Payment = ski_Payment;
        U_ski_No = u_ski_No;
        Un_Ski_payment = un_Ski_payment;
        Agency = agency;
        Amount = amount;
        this.PKDailyManWorkId = PKDailyManWorkId;
    }

    public String getWork_Name() {
        return Work_Name;
    }

    public void setWork_Name(String work_Name) {
        Work_Name = work_Name;
    }

    public String getSki_No() {
        return Ski_No;
    }

    public void setSki_No(String ski_No) {
        Ski_No = ski_No;
    }

    public String getSki_Payment() {
        return Ski_Payment;
    }

    public void setSki_Payment(String ski_Payment) {
        Ski_Payment = ski_Payment;
    }

    public String getU_ski_No() {
        return U_ski_No;
    }

    public void setU_ski_No(String u_ski_No) {
        U_ski_No = u_ski_No;
    }

    public String getUn_Ski_payment() {
        return Un_Ski_payment;
    }

    public void setUn_Ski_payment(String un_Ski_payment) {
        Un_Ski_payment = un_Ski_payment;
    }

    public String getAgency() {
        return Agency;
    }

    public void setAgency(String agency) {
        Agency = agency;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPKDailyManWorkId() {
        return PKDailyManWorkId;
    }

    public void setPKDailyManWorkId(String PKDailyManWorkId) {
        this.PKDailyManWorkId = PKDailyManWorkId;
    }
}
