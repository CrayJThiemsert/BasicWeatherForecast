package com.example.basicweatherforecast

import android.os.Bundle
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.basicweatherforecast.api.RetrofitClient
import com.example.basicweatherforecast.model.GitHubResponse.GitHubUser
import com.example.basicweatherforecast.model.GitHubResponse.GitHubUserSince
import com.example.basicweatherforecast.utility.SetUpLayoutManager
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_github.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubUserActivity : AppCompatActivity() {

    private val mGitHubUserList = ArrayList<GitHubUserSince>()
    private lateinit var mGitHubUserAdapter: GitHubUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        SetUpLayoutManager.verticalLinearLayout(applicationContext, gitHubRecyclerView)

        mGitHubUserAdapter = GitHubUserAdapter(applicationContext, mGitHubUserList)
        gitHubRecyclerView.adapter = mGitHubUserAdapter

        val userObservable = getUserObservable(135)
        val userObserver = getUserObserver()

        userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userObserver)


    }

    private fun getUserObservable(since: Int): Single<List<GitHubUserSince>> {
        return RetrofitClient.gitHubMethods().getGitHubUserSingle(since)
    }

    private fun getUserObserver(): DisposableSingleObserver<List<GitHubUserSince>> {
        return object : DisposableSingleObserver<List<GitHubUserSince>>() {
            override fun onError(e: Throwable) {
                Log.i("onError", e.toString())
            }

            override fun onSuccess(gitHubUserSinceList: List<GitHubUserSince>) {
                mGitHubUserList.clear()
                mGitHubUserList.addAll(gitHubUserSinceList)
                mGitHubUserAdapter.notifyDataSetChanged()
            }
        }
    }
}