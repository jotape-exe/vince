package com.company.ourfinances.model.constants

class RemoteConstants private constructor(){

    object HTTP{
        const val OK = 200
        const val NOT_FOUND = 404
        const val SERVER_ERROR = 500
    }

    companion object{
        const val SERVER_CLIENT_ID = "688509858493-ih7seb3jb90qp0916qhgp12vcr264nrg.apps.googleusercontent.com"
        const val API_URL = "https://economia.awesomeapi.com.br/"
    }

}