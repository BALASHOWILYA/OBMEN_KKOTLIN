package com.sad_ballala_projects.ObmenKnigami_Kotlin.accounthelper

import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.*
import com.sad_ballala_projects.ObmenKnigami_Kotlin.MainActivity
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R
import com.sad_ballala_projects.ObmenKnigami_Kotlin.constants.FirebaseAuthConstants
import com.sad_ballala_projects.ObmenKnigami_Kotlin.gialogshelper.GoogleAccConst

class AccountHelper(act:MainActivity) {
    private val act = act
    private  lateinit var signInClient:GoogleSignInClient

    fun signUpWithEmail(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            act.myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sendEmailVerification(task.result?.user!!)
                    act.uiUpdate(task.result?.user)
                } else {
                    Toast.makeText(act, act.resources.getString(R.string.sign_up_error), Toast.LENGTH_LONG).show()
                    Log.d("MyLog", "Exception : ${task.exception}")
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        val exception = task.exception as FirebaseAuthUserCollisionException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE) {
                            //Toast.makeText(act, FirebaseAuthConstants.ERROR_EMAIL_ALREADY_IN_USE, Toast.LENGTH_LONG).show()
                            //Link email
                            linkEmailToG(email, password)
                        }
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException
                        if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                            // Log.d("MyLog", "Exception :  ${exception.errorCode} ")
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                        }

                    }
                    if (task.exception is FirebaseAuthWeakPasswordException) {
                        val exception = task.exception as FirebaseAuthWeakPasswordException
                        Log.d("MyLog", "Exception : ${exception.errorCode}")

                        if (exception.errorCode == FirebaseAuthConstants.ERROR_WEAK_PASSWORD) {
                            // Log.d("MyLog", "Exception :  ${exception.errorCode} ")
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_WEAK_PASSWORD, Toast.LENGTH_LONG).show()
                        }

                    }
                    //FirebaseAuthWeakPasswordException
                }

            }
        }
    }

    fun signInWithEmail(email:String,password:String){
        if(email.isNotEmpty()&&password.isNotEmpty()){
            act.myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                if(task.isSuccessful){
                    act.uiUpdate(task.result?.user)
                } else{
                   // Log.d("MyLog", "Exception : ${task.exception}")
                    if(task.exception is FirebaseAuthInvalidCredentialsException) {
                        Log.d("MyLog", "Exception :  ${task.exception} ")
                        val exception = task.exception as FirebaseAuthInvalidCredentialsException

                        if (exception.errorCode == FirebaseAuthConstants.ERROR_INVALID_EMAIL) {
                            // Log.d("MyLog", "Exception :  ${exception.errorCode} ")
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_INVALID_EMAIL, Toast.LENGTH_LONG).show()
                        } else if (exception.errorCode == FirebaseAuthConstants.ERROR_WRONG_PASSWORD) {
                            // Log.d("MyLog", "Exception :  ${exception.errorCode} ")
                            Toast.makeText(act, FirebaseAuthConstants.ERROR_WRONG_PASSWORD, Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }
        }
    }

    private fun linkEmailToG(email:String, password: String) {
        val credential = EmailAuthProvider.getCredential(email, password)
        if (act.myAuth.currentUser != null) {
            act.myAuth.currentUser?.linkWithCredential(credential)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(act, act.resources.getString(R.string.link_is_done), Toast.LENGTH_LONG).show()
                }

            }
        } else {
            Toast.makeText(act, act.resources.getString(R.string.enter_to_g), Toast.LENGTH_LONG).show()
        }
    }



    private fun getSignInClient():GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(act.getString(R.string.default_web_client_id)).requestEmail().build()
        return GoogleSignIn.getClient(act, gso)
    }


    fun signInWithGoogle(){
        signInClient = getSignInClient()
        val intent = signInClient.signInIntent
        act.startActivityForResult(intent, GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    fun signInFirebaseWithGoogle(token:String){
        val credential = GoogleAuthProvider.getCredential(token, null)
        act.myAuth.signInWithCredential(credential).addOnCompleteListener{task->
            if(task.isSuccessful){
                Toast.makeText(act, "Sign in done", Toast.LENGTH_LONG).show()
                act.uiUpdate(task.result?.user)
            } else {
                Log.d("MyLog", "Google Sign In Exception : ${task.exception}")
            }

        }
    }


    private fun sendEmailVerification(user:FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener{ task->
            if(task.isSuccessful){
                Toast.makeText(act, act.resources.getString(R.string.send_verification_done),Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(act, act.resources.getString(R.string.send_verification_email_error),Toast.LENGTH_LONG).show()
            }

        }
    }
}