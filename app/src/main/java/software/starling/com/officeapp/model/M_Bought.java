package software.starling.com.officeapp.model;

/**
 * Created by Admin on 4/24/2018.
 */

public class M_Bought {

    String materila;
    String quantity;
    String rate;
    String amount;
    String PKMaterialBoughtId;

    public M_Bought(String PKMaterialBoughtId,String materila, String quantity, String rate,String amount) {
        this.materila = materila;
        this.quantity = quantity;
        this.rate=rate;
        this.amount = amount;
        this.PKMaterialBoughtId=PKMaterialBoughtId;
    }

    public String getMaterila() {
        return materila;
    }

    public void setMaterila(String materila) {
        this.materila = materila;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPKMaterialBoughtId() {
        return PKMaterialBoughtId;
    }

    public void setPKMaterialBoughtId(String PKMaterialBoughtId) {
        this.PKMaterialBoughtId = PKMaterialBoughtId;
    }
}
