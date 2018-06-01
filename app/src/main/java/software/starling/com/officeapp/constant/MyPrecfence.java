package software.starling.com.officeapp.constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Harsh on 4/23/2018.
 */

public class MyPrecfence {
    private static MyPrecfence preferences = null;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor editor;
    String PKEmployeeId;
    String EmpCode;
    String FirstName;
    String LastName;
    String PKDeptId;
    String projectId;
    String LoginId;
    String LoginPwd;
    String workID;
    String Agecny;
    String PKDailyManWorkId;
    String MatterialRecied;
    String MatterialBought;
    String petty;
    String Tomarrowplan;
    String MaterialReceivedUnit;
    String PKUnitId;

    public MyPrecfence(Context context) {
        setmPreferences(PreferenceManager.getDefaultSharedPreferences(context));
    }


    public SharedPreferences getmPreferences() {
        return mPreferences;
    }

    public void setmPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }


    public static MyPrecfence getActiveInstance(Context context) {
        if (preferences == null) {
            preferences = new MyPrecfence(context);
        }
        return preferences;
    }


    public String getEmpCode() {
        return mPreferences.getString(this.PKEmployeeId, "");
    }

    public void setEmpCode(String EmpCode) {
        editor = mPreferences.edit();
        editor.putString(this.EmpCode, EmpCode);
        editor.commit();
    }

    public String getFirstName() {
        return mPreferences.getString(this.FirstName, "");
    }

    public void setFirstName(String FirstName) {
        editor = mPreferences.edit();
        editor.putString(this.FirstName, FirstName);
        editor.commit();
    }


    public String getLastName() {
        return mPreferences.getString(this.FirstName, "");
    }

    public void setLastName(String LastName) {
        editor = mPreferences.edit();
        editor.putString(this.LastName, LastName);
        editor.commit();
    }

    public String getPKDeptId() {
        return mPreferences.getString(this.FirstName, "");
    }

    public void setPKDeptId(String PKDeptId) {
        editor = mPreferences.edit();
        editor.putString(this.LastName, PKDeptId);
        editor.commit();
    }

    public String getPKEmployeeId() {
        return mPreferences.getString(this.PKEmployeeId, "");
    }

    public void setPKEmployeeId(String PKEmployeeId) {
        editor = mPreferences.edit();
        editor.putString(this.PKEmployeeId, PKEmployeeId);
        editor.commit();
    }


    public void setProjectId(String projectId) {
        editor = mPreferences.edit();
        editor.putString(this.projectId, projectId);
        editor.commit();
    }

    public String getProjectId() {
        return mPreferences.getString(this.projectId, null);
    }


    public void setLoginId(String LoginId) {
        editor = mPreferences.edit();
        editor.putString(this.LoginId, LoginId);
        editor.commit();
    }

    public String getLoginId() {
        return mPreferences.getString(this.LoginId, null);
    }

    public void setLoginPwd(String LoginPwd) {
        editor = mPreferences.edit();
        editor.putString(this.LoginPwd, LoginPwd);
        editor.commit();
    }

    public String getLoginPwd() {
        return mPreferences.getString(this.LoginPwd, null);
    }


    public void setPWorkID(String Workid) {
        editor = mPreferences.edit();
        editor.putString(this.workID, Workid);
        editor.commit();
    }

    public String getWorkID() {
        return mPreferences.getString(this.workID, "");
    }

    public void setAgengy(String agency) {
        editor = mPreferences.edit();
        editor.putString(this.Agecny, agency);
        editor.commit();
    }

    public String getAgecny() {
        return mPreferences.getString(this.Agecny, "");
    }

    public void setPKDailyManWorkId(String PKDailyManWorkId) {
        editor = mPreferences.edit();
        editor.putString(this.PKDailyManWorkId, PKDailyManWorkId);
        editor.commit();
    }

    public String getPKDailyManWorkId() {
        return mPreferences.getString(this.PKDailyManWorkId, "");
    }


    public void setMatterial(String Matterial) {
        editor = mPreferences.edit();
        editor.putString(this.MatterialRecied, Matterial);
        editor.commit();
    }

    public String getMatterial() {
        return mPreferences.getString(this.MatterialRecied, "");
    }

    public void setMatterialBought(String MatterialBought) {
        editor = mPreferences.edit();
        editor.putString(this.MatterialBought, MatterialBought);
        editor.commit();
    }

    public String getMatterialBought() {
        return mPreferences.getString(this.MatterialBought, "");
    }

    public void setPetty(String petty_cash) {
        editor = mPreferences.edit();
        editor.putString(this.petty, petty_cash);
        editor.commit();
    }

    public String getPetty() {
        return mPreferences.getString(this.petty, "");
    }


    public void setPlan(String Topaln) {
        editor = mPreferences.edit();
        editor.putString(this.Tomarrowplan, Topaln);
        editor.commit();
    }

    public String getPlan() {
        return mPreferences.getString(this.Tomarrowplan, "");
    }

    public void setunitReceived(String mrunit) {
        editor = mPreferences.edit();
        editor.putString(this.MaterialReceivedUnit, mrunit);
        editor.commit();
    }

    public String getunitREceived() {
        return mPreferences.getString(this.MaterialReceivedUnit, "");
    }

    public void setPKUnitId (String PKUnitId) {
        editor = mPreferences.edit();
        editor.putString(this.PKUnitId, PKUnitId);
        editor.commit();
    }

    public String getPKUnitId() {
        return mPreferences.getString(this.PKUnitId, "");
    }

}
