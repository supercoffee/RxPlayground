package com.bendaschel.rxjavaplayground

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.BehaviorSubject
import org.apache.commons.codec.digest.DigestUtils

class HashControlState(digests: List<DigestUtils>): Observable<List<HashFunctionControl>>() {

    private val currentState = BehaviorSubject.create<List<HashFunctionControl>>()

    init {
        val initialState = digests.map {
            HashFunctionControl(it, true)
        }
        currentState.onNext(initialState)
    }

    fun onStateChanged(changed: HashFunctionControl) {
        val current = currentState.value!!
        val next = current.map {
            if (it.digest.messageDigest.algorithm == changed.digest.messageDigest.algorithm){
                changed
            } else {
                it
            }
        }
        currentState.onNext(next)
    }

    override fun subscribeActual(observer: Observer<in List<HashFunctionControl>>) {
        currentState.subscribe(observer)
    }
}