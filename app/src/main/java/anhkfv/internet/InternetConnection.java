package anhkfv.internet;

import android.content.Context;
import android.net.ConnectivityManager;

import lombok.NonNull;

public class InternetConnection {
    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(@NonNull Context context) {
        return  ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
