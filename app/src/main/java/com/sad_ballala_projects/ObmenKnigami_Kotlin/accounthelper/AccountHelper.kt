package com.sad_ballala_projects.ObmenKnigami_Kotlin.accounthelper

import android.widget.Toast
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseUser
import com.sad_ballala_projects.ObmenKnigami_Kotlin.MainActivity
import com.sad_ballala_projects.ObmenKnigami_Kotlin.R

class AccountHelper(act:MainActivity) {
    private val act = act
    fun signUpWithEmail(email:String,password:String){
        if(email.isNotEmpty()&&password.isNotEmpty()){
            act.myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                if(task.isSuccessful){
                    sendEmailVerification(task.result?.user!!)
                    act.uiUpdate(task.result?.user)
                }
                else{
                        Toast.makeText(act, act.resources.getString(R.string.sign_up_error),Toast.LENGTH_LONG).show()
                    }

            }
        }
    }

    fun signInWithEmail(email:String,password:String){
        if(email.isNotEmpty()&&password.isNotEmpty()){
            act.myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{ task->
                if(task.isSuccessful){
                    act.uiUpdate(task.result?.user)
                }
                else{
                    Toast.makeText(act, act.resources.getString(R.string.sign_in_error),Toast.LENGTH_LONG).show()
                }

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