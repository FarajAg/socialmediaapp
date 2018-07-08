package com.icarasia.social.socialmediaapp.Login

import com.icarasia.social.socialmediaapp.API.DataSourece
import org.junit.Before
import org.junit.Test

import org.mockito.Mockito.*

class LoginPresenterTest {


    lateinit var repo : DataSourece
    lateinit var view: viewContract
    lateinit var loginPresenter: LoginPresenter

    @Before
    fun setUp() {
        repo = mock(DataSourece::class.java)
        view = mock(viewContract::class.java)
        loginPresenter = LoginPresenter(view,repo)
    }

    @Test
    fun checkUserLogedInWhenUserIsLogedIn() {
        `when`(view.userLogedIn()).thenReturn(true)
        loginPresenter.checkUserLogedIn()

        verify(view).toPostsActivity()
    }

    @Test
    fun checkUserLogedInWhenUserIsNOTLogedIn() {
        `when`(view.userLogedIn()).thenReturn(false)
        loginPresenter.checkUserLogedIn()

        verify(view).hidActionBar()

    }

    @Test
    fun whenUserReceivedEmpty(){
        loginPresenter.whenUserReceived(ArrayList<User>())

        verify(view).showErrorMessage()
        verify(view).hidLoadingDialoge()
    }

    @Test
    fun whenUserReceivedNotEmpty(){
        var user = User(1)
        var arr = ArrayList<User>()
        arr.add(user)
        loginPresenter.whenUserReceived(arr)

        verify(repo).getAlbums(1)
        verify(repo).getTodos(1)
    }

    @Test
    fun checkUserLogedIn() {
    }
}