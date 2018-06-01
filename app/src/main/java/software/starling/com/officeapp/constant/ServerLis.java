package software.starling.com.officeapp.constant;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harsh on 4/21/2018.
 */

public interface ServerLis {
    void onSuccess(String response) throws JSONException;

    void onError(VolleyError error);


}
