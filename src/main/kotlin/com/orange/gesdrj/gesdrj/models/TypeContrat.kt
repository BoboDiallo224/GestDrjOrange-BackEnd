package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "typeContrats")
data class TypeContrat(
        @Id
        @GeneratedValue
        val id : Long?=null,
        var libelle: String?=null,
        var delayOfTraitment : Long?=null,

        @JsonIgnore
        @OneToMany(mappedBy = "typeContrat")
        val users:  List<User> = emptyList(),

        @JsonIgnore
        @OneToMany(mappedBy = "typeContrat")
        val demandes:  List<Demande> = emptyList()

)
