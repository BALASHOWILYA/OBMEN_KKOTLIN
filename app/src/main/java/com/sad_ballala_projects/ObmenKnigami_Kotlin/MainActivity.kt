package com.sad_ballala_projects.ObmenKnigami_Kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.ListOfBooksFragment
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.MessengerFragment
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.ProfileFragment
import com.sad_ballala_projects.ObmenKnigami_Kotlin.Frag.RequestFragment
import com.sad_ballala_projects.ObmenKnigami_Kotlin.act.EditAdsAct
import com.sad_ballala_projects.ObmenKnigami_Kotlin.databinding.ActivityMainBinding
import com.sad_ballala_projects.ObmenKnigami_Kotlin.gialogshelper.DialogConst
import com.sad_ballala_projects.ObmenKnigami_Kotlin.gialogshelper.DialogHelper
import com.sad_ballala_projects.ObmenKnigami_Kotlin.gialogshelper.GoogleAccConst


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val profileFragment = ProfileFragment()
    private val messengerFragment = MessengerFragment()
    private val requestAds = RequestFragment()
    private val list_of_books = ListOfBooksFragment()

    private lateinit var tvAccount:TextView
    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val myAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        replaceFragment(messengerFragment)

        rootElement.bottom.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_profile -> replaceFragment(profileFragment)
                R.id.ic_messenger -> replaceFragment(messengerFragment)
                R.id.ic_add_ads -> startActivity(Intent(this, EditAdsAct::class.java))
                R.id.ic_request_ads -> replaceFragment(requestAds)
                R.id.ic_list_books -> replaceFragment(list_of_books)
            }
            true
        }


        init()

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment )
            transaction.commit()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.id_new_ads){
            val i = Intent(this, EditAdsAct::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
            val task =GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    Log.d("MyLog", "Api 0")
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }

            }catch(e:ApiException){
                Log.d("MyLog", "Api error : ${e.message}")
            }
            //Log.d("MyLog", "Sign in result")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart(){
        super.onStart()
        uiUpdate(myAuth.currentUser)
    }

    private fun init(){

        setSupportActionBar(rootElement.mainContent.toolbar)
       var  toggle = ActionBarDrawerToggle(this,rootElement.drawerLayout,rootElement.mainContent.toolbar,R.string.open,R.string.close)
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener (this)
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.id_a_science_book ->{
                Toast.makeText(this, "Presed id_a_science_book", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_humorous_story->{
                Toast.makeText(this, "Presed id_a_humorous_story", Toast.LENGTH_LONG).show()

            }
            R.id.id_a_romance ->{
                Toast.makeText(this, "Presed id_a_romance", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_detective_story ->{
                Toast.makeText(this, "Presed id_a_detective_story", Toast.LENGTH_LONG).show()
            }
            R.id.id_an_adventure_novel ->{
                Toast.makeText(this, "Presed id_an_adventure_novel", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_fiction_book ->{
                Toast.makeText(this, "Presed id_a_fiction_book", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_science_fiction_book ->{
                Toast.makeText(this, "Presed id_a_science_fiction_book", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_drama ->{
                Toast.makeText(this, "Presed id_a_drama", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_psychologyt ->{
                Toast.makeText(this, "Presed id_a_psychologyt", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_horror_story ->{
                Toast.makeText(this, "Presed id_a_horror_story", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_fairy_tale ->{
                Toast.makeText(this, "Presed id_a_fairy_tale", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_war_novel ->{
                Toast.makeText(this, "Presed id_a_war_novel", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_historical_novel ->{
                Toast.makeText(this, "Presed id_a_historical_novel", Toast.LENGTH_LONG).show()
            }
            R.id.id_an_autobiography ->{
                Toast.makeText(this, "Presed id_an_autobiography", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_biography ->{
                Toast.makeText(this, "Presed id_a_biography", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_sport ->{
                Toast.makeText(this, "Presed id_a_sport", Toast.LENGTH_LONG).show()
            }
            R.id.id_a_classic ->{
                Toast.makeText(this, "Presed id_a_classic", Toast.LENGTH_LONG).show()
            }


            R.id.id_textbooks_for_the_1st_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_1st_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_2nd_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_2nd_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_3rd_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_3rd_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_4th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_4th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_5th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_5th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_6th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_6th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_7th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_7th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_8th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_8th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_9th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_9th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_10th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_10th_grade", Toast.LENGTH_LONG).show()
            }
            R.id.id_textbooks_for_the_11th_grade ->{
                Toast.makeText(this, "Presed id_textbooks_for_the_11th_grade", Toast.LENGTH_LONG).show()
            }


            R.id.id_sign_up ->{
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
            }

            R.id.id_sign_in ->{
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }

            R.id.id_sign_out ->{
                uiUpdate(null)
                myAuth.signOut()
                dialogHelper.accHelper.signOutG()
            }


        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true

    }

    fun uiUpdate(user: FirebaseUser?){
        tvAccount.text = if(user==null){
            resources.getString(R.string.not_reg)
        } else{
            user.email
        }
    }
}