package com.company.ourfinances.model.service

interface IAuthService {

    fun loginWithGoogle(token: String) { }

    fun loginWithEmailAndPassword(email: String, password: String) { }

    fun registerWithEmailAndPassword(email: String, password: String) { }
}


