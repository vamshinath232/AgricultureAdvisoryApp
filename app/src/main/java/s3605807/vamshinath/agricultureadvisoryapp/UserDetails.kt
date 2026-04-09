package s3605807.vamshinath.agricultureadvisoryapp

import android.content.Context

object UserDetails {

    private const val USERDETAILS_PREF = "FarmerData"

    fun saveUserLoginStatus(currentActivity: Context, value: Boolean) {
        val userLoginSP = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userLoginSP.edit()
        editor.putBoolean("LOGIN_STATUS", value).apply()
    }

    fun getUserLoginStatus(currentActivity: Context): Boolean {
        val userLoginSP = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        return userLoginSP.getBoolean("LOGIN_STATUS", false)
    }

    fun saveName(currentActivity: Context, name: String) {
        val userName = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userName.edit()
        editor.putString("USER_NAME", name).apply()
    }

    fun getName(currentActivity: Context): String? {
        val userName = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        return userName.getString("USER_NAME", null)
    }

    fun saveGender(currentActivity: Context, gender: String) {
        val userGender = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userGender.edit()
        editor.putString("USER_GENDER", gender).apply()
    }

    fun getGender(currentActivity: Context): String? {
        val userGender = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        return userGender.getString("USER_GENDER", null)
    }


    fun saveEmail(currentActivity: Context, email: String) {
        val userEmail = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        val editor = userEmail.edit()
        editor.putString("USER_EMAIL", email).apply()
    }

    fun getEmail(currentActivity: Context): String? {
        val userEmail = currentActivity.getSharedPreferences(USERDETAILS_PREF, Context.MODE_PRIVATE)
        return userEmail.getString("USER_EMAIL", null)
    }
}
