package com.wyattconrad.cs_360weighttracker.data

sealed class LoginResult {
    data class Success(val userId: Long, val firstName: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object InvalidCredentials : LoginResult()
    object UserNotFound : LoginResult()
}