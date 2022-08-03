package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.RequestMapping
import com.orange.gesdrj.gesdrj.vendor.ListStringToStringConverter
import java.util.*
import javax.persistence.*

@Entity(name = "users")
data class User(
        @Id
        @GeneratedValue
        val id : Long?=null,
        var name: String?="",
        var username: String="",
        var password : String="",
        var email : String?="",
        var matricule : String?="",

        var isActivated : Boolean = false,

        @Convert(converter = ListStringToStringConverter::class)
        var roles: List<String> = listOf(Role.DEMANDEUR),

        @ManyToOne
        @JoinColumn(name = "typeContrat_id", nullable = true)
        var typeContrat: TypeContrat? = null,

        @ManyToOne
        @JoinColumn(name = "division_id", nullable = true)
        var division: Division? = null,

        var direction: String? = null,
        //var division: String? = null,
        @JsonIgnore
        @OneToMany(mappedBy = "user")
        val demande: List<Demande>?= emptyList(),

        var typeImageSignature: String? = null,

        @Column(columnDefinition = "BLOB NULL", nullable = true)
        var contentImageSignature: String? = null

)

interface UserRepository: JpaRepository<User, Long> {

        fun findByUsernameOrEmail(username: String, email: String): Optional<User>

        fun findByUsername(username: String): User?

        @Query("select u from users u")
        fun findUserByRole(): List<User>?

        @Query("select u from users u where u.username = :x")
        fun findUserByUserName(@Param("x") username:String): User?

        @Query("select u from users u order by id desc")
        fun getAllUsers():List<User>

        fun findByDivision(division :Division):List<User>

        @Query("select u from users u where u.typeContrat = :x")
        fun findByTypeContrat(@Param("x")typeContrat: TypeContrat):List<User>?

        @Query("select distinct (u.division) AS division from users u")
        fun findByDistincDivision():List<String>

        @Query("select u from users u where u.division.libelle = :x and u.typeContrat = :y")
        fun findUserByDivision(@Param("x")division: String,
                               @Param("y")typeContrat: TypeContrat):List<User>?

}

enum class UserStatus {Activated, Desactivated}

class Role {
        companion object {
                const val DEMANDEUR = "ROLE_DEMANDEUR"
                const val ADMIN = "ROLE_ADMIN"
                const val GESTIONNAIRE = "ROLE_GESTIONNAIRE"
                const val VALIDATEUR = "ROLE_VALIDATEUR"
        }

    }

data class Employer2(
        var idemploye: Long,
        var login: String,
        var nom: String,
        var prenom: String,
        var matricule: String,
        var contact: String,
        var email: String,
        var sexe: String,
        var direction2: String,
        var fonction: String,
        var siteaffectation: String,
        var dateaffectation: Date? = null,
        var datefin: Date? = null
)




