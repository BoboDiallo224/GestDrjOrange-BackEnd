package com.orange.gesdrj.gesdrj.services

import com.orange.gesdrj.gesdrj.jwt.JwtProvider
import com.orange.gesdrj.gesdrj.jwt.JwtResponse
import com.orange.gesdrj.gesdrj.jwt.ldapAuthenticate
import com.orange.gesdrj.gesdrj.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class UserConnected{

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtProvider: JwtProvider

    fun userAuthentification(loginInfo: User): JwtResponse {


        return if (true) {
        //return if (ldapAuthenticate(loginInfo.username, loginInfo.password)) {

            val authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(loginInfo.username, loginInfo.password))

            SecurityContextHolder.getContext().authentication = authentication
            val jwt = jwtProvider.generateJwtToken(authentication)
            val userDetails = authentication.principal as UserDetails
            JwtResponse("success", jwt, userDetails.authorities)


        } else JwtResponse("error password")

    }


}

