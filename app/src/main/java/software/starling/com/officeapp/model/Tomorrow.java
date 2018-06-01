package software.starling.com.officeapp.model;

/**
 * Created by Harsh on 4/24/2018.
 */

public class Tomorrow {
    String planWork;

    String m_Type;
    String m_Amount;
    String manPower;
    String matterial;
    String fund;
    String f_Type;
    String f_amount;
    String PKTomorrowsPlanningId;


    public Tomorrow(String PKTomorrowsPlanningId, String planWork, String m_Type, String m_Amount, String manPower, String matterial, String fund, String f_Type, String f_amount) {
        this.planWork = planWork;
        this.m_Type = m_Type;
        this.m_Amount = m_Amount;
        this.manPower = manPower;
        this.matterial = matterial;
        this.fund = fund;
        this.f_Type = f_Type;
        this.f_amount = f_amount;
        this.PKTomorrowsPlanningId=PKTomorrowsPlanningId;
    }

    public String getPlanWork() {
        return planWork;
    }

    public void setPlanWork(String planWork) {
        this.planWork = planWork;
    }

    public String getM_Type() {
        return m_Type;
    }

    public void setM_Type(String m_Type) {
        this.m_Type = m_Type;
    }

    public String getM_Amount() {
        return m_Amount;
    }

    public void setM_Amount(String m_Amount) {
        this.m_Amount = m_Amount;
    }

    public String getManPower() {
        return manPower;
    }

    public void setManPower(String manPower) {
        this.manPower = manPower;
    }

    public String getMatterial() {
        return matterial;
    }

    public void setMatterial(String matterial) {
        this.matterial = matterial;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getF_Type() {
        return f_Type;
    }

    public void setF_Type(String f_Type) {
        this.f_Type = f_Type;
    }

    public String getF_amount() {
        return f_amount;
    }

    public void setF_amount(String f_amount) {
        this.f_amount = f_amount;
    }

    public String getPKTomorrowsPlanningId() {
        return PKTomorrowsPlanningId;
    }

    public void setPKTomorrowsPlanningId(String PKTomorrowsPlanningId) {
        this.PKTomorrowsPlanningId = PKTomorrowsPlanningId;
    }
}
