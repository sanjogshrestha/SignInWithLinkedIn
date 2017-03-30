package cnblabs.np.linkeninregister;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by SanjogStha on 8/1/2016.
 * sanjogshrestha.nepal@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generateHashkey();
    }

    /**
     * Generate hashkey.
     */
    public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "cnblabs.np.linkeninregister",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                System.out.println("check_key="+ Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    /**
     * Authenticate with linkedIn and initialize Session.
     * Login.
     */
    public void login(){
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                Toast.makeText(getApplicationContext(), "success"
                        + LISessionManager.getInstance(getApplicationContext())
                            .getSession().getAccessToken().toString(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                System.out.println("check_error=" + error.toString());
                Toast.makeText(getApplicationContext(),
                        "failed " + error.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }, true);
    }

    // After complete authentication start new HomePage Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                requestCode, resultCode, data);
        Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);
    }

    // This method is used to make permissions to retrieve data from linkedIn
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    /**
     * Login.
     *
     * @param view the view
     */
    public void login(View view) {
        if(view.getId() == R.id.login_button)
            login();
    }
}
