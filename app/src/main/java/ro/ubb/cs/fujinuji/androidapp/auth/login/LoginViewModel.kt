package ro.ubb.cs.fujinuji.androidapp.auth.login

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.ubb.cs.fujinuji.androidapp.R
import ro.ubb.cs.fujinuji.androidapp.auth.data.AuthRepository
import ro.ubb.cs.fujinuji.androidapp.auth.data.TokenHolder
import ro.ubb.cs.fujinuji.androidapp.core.Result
import ro.ubb.cs.fujinuji.androidapp.core.TAG

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableLoginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = mutableLoginFormState

    private val mutableLoginResult = MutableLiveData<Result<TokenHolder>>()
    val loginResult: LiveData<Result<TokenHolder>> = mutableLoginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            Log.v(TAG, "login...");
            mutableLoginResult.value = AuthRepository.login(username, password)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            mutableLoginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            mutableLoginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            mutableLoginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 1
    }
}