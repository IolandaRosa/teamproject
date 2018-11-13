package modelo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import groupf.taes.ipleiria.spots.R;

public enum InternetConnectionManager {
    INSTANCE;

    InternetConnectionManager() {
    }

    public boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void showErrorMessage(Context context, int message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);

        builder.show();
    }
}
