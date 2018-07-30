package com.icarasia.social.socialmediaapp.homeActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.TextView
import com.icarasia.social.socialmediaapp.homeActivity.DI.DaggerHomeActivityComponent
import com.icarasia.social.socialmediaapp.homeActivity.DI.HomeActivityModule
import com.icarasia.social.socialmediaapp.loginActivity.LoginActivity
import com.icarasia.social.socialmediaapp.loginActivity.User
import com.icarasia.social.socialmediaapp.postsFragment.PostsFragment
import com.icarasia.social.socialmediaapp.R
import com.icarasia.social.socialmediaapp.userDetailsFragmet.UserDetailsFragment
import com.icarasia.social.socialmediaapp.abstracts.SocialMediaNetworkActivity
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*
import kotlinx.android.synthetic.main.home_navigation_avtivity.*
import javax.inject.Inject

class HomeActivity : SocialMediaNetworkActivity((R.id.drawer_layout)),
        NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var fragmentPost : PostsFragment
    @Inject
    lateinit var fragmentUserDetails: UserDetailsFragment
    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_navigation_avtivity)
        setSupportActionBar(toolbar)

        DaggerHomeActivityComponent.builder()
                .homeActivityModule(HomeActivityModule())
                .build()
                .inject(this)

        toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        injectPostfragment()
        openFragment(fragmentPost)

        nav_view.setNavigationItemSelectedListener(this)
        navigationNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        LoginActivity.getUserlogedIn(this)?.let {
            setUpheader(it)
        }

    }

    fun injectPostfragment(){
        fragmentPost.injectDeletionGroup(deletionGroup, deleteCancelation, selectionCounter, deleteConfirmation)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpheader(user: User) {
        with(findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)) {
            findViewById<TextView>(R.id.userName).text = "${user.username} ${user.id}"
            findViewById<TextView>(R.id.userEmail).text = user.email
        }
    }


    fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.framLayoutNavigation, fragment)
                .commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                injectPostfragment()
                openFragment(fragmentPost)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                openFragment(fragmentUserDetails)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    companion object {
        fun StartActivity(context: Context) {
            context.startActivity(Intent(context, HomeActivity::class.java))
        }
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.orderAce -> {
                fragmentPost.postsAdapter.sortAsc()
            }
            R.id.orderDec -> {
                fragmentPost.postsAdapter.sortDec()
            }
            R.id.delete -> {
                fragmentPost.trigerDeletionMode()
            }
            R.id.clearRecyclerData ->{
                fragmentPost.postsAdapter.clear()
                fragmentPost.postsViewModel.callpost(fragmentPost.postsViewModel.getCurrentPage()
                        ,fragmentPost.postsViewModel.getItemsPerPageCount())
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onInternetConnected() {
        snakBar.dismiss()
    }

    override fun onInternetDisconnected() {
        snakBar.show()
    }

}