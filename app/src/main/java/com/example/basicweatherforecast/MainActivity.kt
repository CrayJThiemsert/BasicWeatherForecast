package com.example.basicweatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import org.jetbrains.anko.startActivity

//import io.reactivex.rxjava3.kotlin.toFlowable
//import io.reactivex.rxjava3.kotlin.toObservable

//import io.reactivex.rxkotlin.toObservable

//import io.reactivex.rxkotlin.toFlowable
//import io.reactivex.rxkotlin.toObservable

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rj_button.setOnClickListener{
//            startRJStream()
            Observable.just("Hello Gundam")
                .subscribe {
                    println(it)
                }
        }

        rk_button.setOnClickListener{ startRKStream() }

        rz_button.setOnClickListener{ startRZStream() }

        gitHubUserButton.setOnClickListener {
            startActivity<GitHubUserActivity>()
        }

        gitHubUserStartWithSButton.setOnClickListener {
            startActivity<GitHubUserStartWithSActivity>()
        }

        current_weather_button.setOnClickListener {
            startActivity<CurrentWeatherActivity>()
        }

        current_weather_rx_button.setOnClickListener {
            startActivity<WholeDayForecastActivity>()
        }

    }


    private fun startRZStream() {

        val numbers = Observable.range(1, 6)

        val strings = Observable.just("Gundam", "Two", "Three",

            "Four", "Five", "Six" )

        val zipped = Observables.zip(strings, numbers) { s, n -> "$s $n" }
        zipped.subscribe(::println)
    }

    private fun startRKStream() {

        val list = listOf("2", "2", "3", "4", "5")

        //Apply the toObservable() extension function//
        list.toObservable()

            //Construct your Observer using the subscribeBy() extension function//
            .subscribeBy(

                onNext = { println(it) },
                onError = { it.printStackTrace() },
                onComplete = { println("onComplete!") }

            )
    }

    private fun startRJStream() {
        //Create an Observable//
        val myObservable = getObservable()

        //Create an Observer//
        val myObserver = getObserver()

        //Subscribe myObserver to myObservable//
        myObservable
            .subscribe(myObserver)

    }

    private fun getObserver(): Observer<String> {
        return object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
            }

            //Every time onNext is called, print the value to Android Studio’s Logcat//
            override fun onNext(s: String) {
                Log.d(TAG, "onNext: $s")
            }

            //Called if an exception is thrown//
            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            //When onComplete is called, print the following to Logcat//
            override fun onComplete() {
                Log.d(TAG, "onComplete")
            }
        }
    }

    private fun getObservable(): Observable<String> {
        return Observable.just("1", "2", "3", "4", "5", "6")
    }

}