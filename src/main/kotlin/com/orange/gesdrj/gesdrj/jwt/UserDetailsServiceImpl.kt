package com.orange.gesdrj.gesdrj.jwt

import com.orange.gesdrj.gesdrj.models.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl : UserDetailsService {

    @Autowired
    internal var userRepository: UserRepository? = null

    @Transactional
    @Throws(UsernameNotFoundException::class)

    override fun loadUserByUsername(usernameOrEmail: String): UserDetails {

        val user = userRepository!!.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow { UsernameNotFoundException("User Not Found with -> username or email : $usernameOrEmail") }

        return UserPrinciple.build(user)
    }
}