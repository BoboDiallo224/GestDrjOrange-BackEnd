package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.jwt.JwtProvider
import com.orange.gesdrj.gesdrj.jwt.JwtResponse
import com.orange.gesdrj.gesdrj.jwt.ldapAuthenticate
import com.orange.gesdrj.gesdrj.models.Employer2
import com.orange.gesdrj.gesdrj.models.Role
import com.orange.gesdrj.gesdrj.models.User
import com.orange.gesdrj.gesdrj.models.UserRepository
import com.orange.gesdrj.gesdrj.repositories.ApiAnnuaire
import com.orange.gesdrj.gesdrj.repositories.DivisionRepository
import com.orange.gesdrj.gesdrj.services.UserConnected

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit



@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    lateinit var userConnected:UserConnected

    @Autowired
    lateinit var encoder: PasswordEncoder

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var divisionRepository: DivisionRepository

    @PostMapping("/login")
    @CrossOrigin(origins = ["*"])
    fun login(@RequestBody loginInfo: User): JwtResponse {

        var jwtResponse = JwtResponse("400")

        //val userSave = User()

        /*val retrofit = Retrofit.Builder()
                .baseUrl("http://172.21.70.69:8080/annuaireog/api/ogn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()*/
        //val service = retrofit.create(ApiAnnuaire::class.java)

        val user = userRepository.findByUsername(loginInfo.username) //?: return JwtResponse("error not exit")

        if (user == null){

            return jwtResponse

       }

    else{
        userRepository.save(user.copy(password = encoder.encode(loginInfo.password)))

            jwtResponse = userConnected.userAuthentification(loginInfo)
   }
        return jwtResponse

    }

    @PostMapping("register")
    fun register(@RequestBody user: User) =
            userRepository.save(user.copy(password = encoder.encode(user.password)))

}

/*  service.getEmployer(loginInfo.username!!.toUpperCase())!!.

                    enqueue(object :Callback<Employer2?> {

                         override fun onFailure(call: Call<Employer2?>, t: Throwable) {
                             print("Faild")

                        }

                        override fun onResponse(call: Call<Employer2?>, response: Response<Employer2?>) {

                            if (response.isSuccessful && response.body() == null){
                                println("not exist in Annuaire app")
                            }

                            if (response.code() != 500){

                                userSave.username = response.body()!!.login.toLowerCase()
                                userSave.name = response.body()!!.prenom+" "+response.body()!!.nom
                                userSave.matricule = response.body()!!.matricule
                                userSave.email = response.body()!!.email
                                userSave.direction = response.body()!!.direction2

                                //userSave.division = divisionRepository.getOne(1)
                            }
                            else{
                                println("Not Found")
                            }

                            userSave.password = encoder.encode(loginInfo.password)
                            userRepository.save(userSave)

                            jwtResponse = userConnected.userAuthentification(loginInfo)

                        }
            })*/