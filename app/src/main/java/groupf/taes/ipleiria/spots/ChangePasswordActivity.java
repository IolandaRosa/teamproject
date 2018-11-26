package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import modelo.InternetConnectionManager;
import modelo.UsersManager;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText editCurrentPassword;
    private EditText editNewPassword;
    private EditText editNewPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(DashboardActivity.getIntent(this));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editCurrentPassword = findViewById(R.id.editOldPassword);
        editNewPassword = findViewById(R.id.editNewPassword);
        editNewPasswordConfirmation = findViewById(R.id.editNewPasswordConfirmation);
    }

    public void onClick_btnSave(View view) {

        String currentPassword = editCurrentPassword.getText().toString();
        final String newPassword = editNewPassword.getText().toString();
        String newPasswordConfirmation = editNewPasswordConfirmation.getText().toString();

        Map<String, Integer> errorMap = UsersManager.INSTANCE.validateChangePassword(currentPassword, newPassword, newPasswordConfirmation);

        if (errorMap.containsKey("emptyFields")) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.emptyFields);
            return;
        }

        if (errorMap.containsKey("currentPassword")) {
            editCurrentPassword.setError(getResources().getString(errorMap.getOrDefault("currentPassword", -1)));
            editCurrentPassword.requestFocus();
            return;
        }

        if (errorMap.containsKey("newPassword")) {
            editNewPassword.setError(getResources().getString(errorMap.getOrDefault("newPassword", -1)));
            editNewPassword.requestFocus();
            return;
        }

        if (errorMap.containsKey("newPasswordConfirmation")) {
            editNewPasswordConfirmation.setError(getResources().getString(errorMap.getOrDefault("newPasswordConfirmation", -1)));
            editNewPasswordConfirmation.requestFocus();
            return;
        }

        if (errorMap.containsKey("passwordMismatch")) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.errorConfirmationPass);
            return;
        }

        if (errorMap.containsKey("newInvalidPassword")) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.errorNewPasswordEquals);
            return;
        }

        //Se passou todos os testes anteriores vai reautenticar e depois se falhar então é porque a current password esta errada
        AuthCredential credentials = EmailAuthProvider.getCredential(FirebaseAuth.getInstance().getCurrentUser().getEmail(), currentPassword);

        FirebaseAuth.getInstance().getCurrentUser().reauthenticateAndRetrieveData(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword);
                    startActivity(DashboardAuthActivity.getIntent(ChangePasswordActivity.this));
                } else {
                    InternetConnectionManager.INSTANCE.showErrorMessage(ChangePasswordActivity.this, R.string.invalidPassword);
                }
            }
        });
    }

    public void onClick_btnCancel(View view) {
        startActivity(DashboardAuthActivity.getIntent(this));
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ChangePasswordActivity.class);
    }
}
