package com.orange.gesdrj.gesdrj.repositories


import com.orange.gesdrj.gesdrj.models.Employer2
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//http://172.21.70.69:8080/annuaireog/api/ogn/getEmployeByLogin?login=

interface ApiAnnuaire {

    @GET("getEmployeByLogin")
    fun getEmployer(@Query("login") userName: String):Call<Employer2>?

    @GET("getEmployeByMatricule")
    fun getEmployerByMatricule(@Query("matricule") matricule: String):Call<Employer2>?

}