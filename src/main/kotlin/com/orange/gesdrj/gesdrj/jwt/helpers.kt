package com.orange.gesdrj.gesdrj.jwt

import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.naming.Context
import javax.naming.directory.InitialDirContext

//---------------------------------------authentication ldap-------------------------------------------------------

fun ldapAuthenticate(username: String, password: String): Boolean {

    val props = Hashtable<String, String>()
    props[Context.INITIAL_CONTEXT_FACTORY] = "com.sun.jndi.ldap.LdapCtxFactory"
    props[Context.PROVIDER_URL] = "ldap://10.100.55.80:389"
    props[Context.SECURITY_AUTHENTICATION] = "simple"
    props[Context.SECURITY_PRINCIPAL] = "$username@orange-sonatel.com"
    props[Context.SECURITY_CREDENTIALS] = password

    return try {
        InitialDirContext(props)
        true
    } catch (e: Exception) {
        false
    }
}

data class LoginInfo(val username: String, val password: String)

data class JwtResponse(val status: String, val token: String? = null, val authorities: Collection<GrantedAuthority>? = null)

//---------------------------------end authentication ldap------------------------------------------

