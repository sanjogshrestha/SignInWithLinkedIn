package cnblabs.np.linkeninregister;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;

import org.json.JSONObject;

/**
 * Created by SanjogStha on 8/1/2016.
 * sanjogshrestha.nepal@gmail.com
 */

public class HomePage extends AppCompatActivity {
    private static final String host = "api.linkedin.com";
    private static final String topCardUrl = "https://"
            + host + "/v1/people/~:"
            +
            "(email-address,formatted-name,phone-numbers,public-profile-url,picture-url,picture-urls::(original))";

    private ProgressDialog progress;
    private TextView user_name, user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        user_email = (TextView) findViewById(R.id.email);
        user_name = (TextView) findViewById(R.id.name);

        progress= new ProgressDialog(this);
        progress.setMessage("Fetching data");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        linkedInApiHelper();
    }

    /**
     * Once User's can authenticated,
       It make an HTTP GET request to LinkedIn's REST API using the currently authenticated user's credentials.
       If successful, A LinkedIn ApiResponse object containing all of the relevant aspects of the server's response will be returned.

     * Linked in api helper.
     */
    public void linkedInApiHelper(){
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(HomePage.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                progress.dismiss();
                try {
                    setProfile(result.getResponseDataAsJson());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError error) {
                progress.dismiss();
                System.out.println("check_error_HP=" + error.toString());
            }
        });
    }

    /**
     * Set User Profile Information in Navigation Bar.
     *
     * @param response the response
     */
    public void setProfile(JSONObject response){
        try{
            user_email.setText(response.get("emailAddress").toString());
            user_name.setText(response.get("formattedName").toString());
            System.out.println("check_response=" + response.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
