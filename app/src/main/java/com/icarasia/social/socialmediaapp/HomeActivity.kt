package com.icarasia.social.socialmediaapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.icarasia.social.socialmediaapp.DataModels.User
import com.icarasia.social.socialmediaapp.Login.getUserlogedIn
import com.icarasia.social.socialmediaapp.Posts.PostsFragment
import com.icarasia.social.socialmediaapp.UserDetalsFragmet.UserDetailsFragment
import com.icarasia.social.socialmediaapp.abstracts.SocialMediaNetworkActivity
import kotlinx.android.synthetic.main.app_bar_navigation.*
import kotlinx.android.synthetic.main.content_navigation.*
import kotlinx.android.synthetic.main.home_navigation_avtivity.*

class HomeActivity : SocialMediaNetworkActivity((R.id.drawer_layout)), NavigationView.OnNavigationItemSelectedListener {

    private val fragmentPost: PostsFragment by lazy { PostsFragment.newInstance() }
    private val fragmentUserDetails: UserDetailsFragment by lazy { UserDetailsFragment.newInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_navigation_avtivity)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        showActionbar()

        fragmentPost.setShowHidActionBar(showActionbar, hidActionbar)
        openFragment(fragmentPost)

        nav_view.setNavigationItemSelectedListener(this)
        navigationNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        with(getUserlogedIn(this)) {
            this?.let {
                setUpheader(this)
            }
        }
    }

    private fun setUpheader(user: User) {
        with(findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)) {
            findViewById<TextView>(R.id.userName).text = "${user.username} ${user.id}"
            findViewById<TextView>(R.id.userEmail).text = user.email
        }
    }


    private val showActionbar: () -> Unit = {
        with(this.supportActionBar!!) { show(); title = "Posts" }
    }

    private val hidActionbar: () -> Unit = {
        with(this.supportActionBar!!) { hide() }
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
                showActionbar()
                openFragment(fragmentPost)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                openFragment(fragmentUserDetails)
                hidActionbar()
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
                Log.d("position At $$$$$  ",fragmentPost.curruntAdapterPosition.toString())

                with(fragmentPost){
                    recyclerView.findViewHolderForAdapterPosition(curruntAdapterPosition)
                            ?.itemView!!.performLongClick()
                }
            }
            R.id.clearRecyclerData ->{
                fragmentPost.postsAdapter.clear()
                fragmentPost.page = 1
                fragmentPost.callpost(fragmentPost.page++,20)
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
