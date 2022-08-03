package com.orange.gesdrj.gesdrj.jwt

import com.fasterxml.jackson.annotation.JsonIgnore
import com.orange.gesdrj.gesdrj.models.User

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

data class UserPrinciple(val id: Long?, private val username: String, val email: String, @field:JsonIgnore
    private val password: String, private val authorities: Collection<GrantedAuthority>) : UserDetails {

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        private const val serialVersionUID = 1L

        fun build(user: User): UserPrinciple {
            val authorities = user.roles.stream().map { SimpleGrantedAuthority(it) }.collect(Collectors.toList())

            return UserPrinciple(
                    user.id,
                    user.username,
                    user.email!!,
                    user.password,
                    authorities
            )
        }
    }
}