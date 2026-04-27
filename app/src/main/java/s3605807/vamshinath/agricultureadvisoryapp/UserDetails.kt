package s3605807.vamshinath.agricultureadvisoryapp

import android.content.Context

object UserDetails {

    private const val USERDETAILS_PREF = "FarmerData"

    fun isUserLoggedIn(currentActivity: Context, value: Boolean) {
        val userLoginSP = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userLoginSP.edit()
        editor.putBoolean("LOGIN_STATUS", value).apply()
    }

    fun findLoginStatus(currentActivity: Context): Boolean {
        val userLoginSP = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        return userLoginSP.getBoolean("LOGIN_STATUS", false)
    }

    fun putUserName(currentActivity: Context, name: String) {
        val userName = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userName.edit()
        editor.putString("USER_NAME", name).apply()
    }

    fun putUserEmail(currentActivity: Context, email: String) {
        val userEmail = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userEmail.edit()
        editor.putString("USER_EMAIL", email).apply()
    }

    fun findEmail(currentActivity: Context): String? {
        val userEmail = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        return userEmail.getString("USER_EMAIL", null)
    }
}
