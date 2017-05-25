package com.vumobile.utils;

/**
 * Created by IT-10 on 5/25/2017.
 */

public class NetworkStateChangeReceiver{ // extends BroadcastReceiver

//    public static final String NETWORK_AVAILABLE_ACTION = "com.vumobile.utils.NetworkStateChangeReceiver";
//    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
//        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE,  isConnectedToInternet(context));
//        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
//    }
//
//    private boolean isConnectedToInternet(Context context) {
//        try {
//            if (context != null) {
//                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//                return networkInfo != null && networkInfo.isConnected();
//            }
//            return false;
//        } catch (Exception e) {
//            Log.e(NetworkStateChangeReceiver.class.getName(), e.getMessage());
//            return false;
//        }
//    }

    // ---------------------------------------------------
    // for activity
//    IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
//    LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
//            String networkStatus = isNetworkAvailable ? "connected" : "disconnected";
//
//            Snackbar.make(findViewById(R.id.activity_main), "Network Status: " + networkStatus, Snackbar.LENGTH_LONG).show();
//        }
//    }, intentFilter);

}
