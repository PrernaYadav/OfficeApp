package software.starling.com.officeapp.model;

/**
 * Created by Harsh on 4/23/2018.
 */

public class Matterial {
    String material;
    String Quantity;
    String unit;
    //String amount;
    String PKMatReceivedId;


    public Matterial(String PKMatReceivedId, String material,String unit, String quantity) {
        this.material = material;
        Quantity = quantity;
        this.unit = unit;

       // this.amount = amount;
        this.PKMatReceivedId=PKMatReceivedId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

   /* public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }*/

    public String getPKMatReceivedId() {
        return PKMatReceivedId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPKMatReceivedId(String PKMatReceivedId) {
        this.PKMatReceivedId = PKMatReceivedId;

    }
}
