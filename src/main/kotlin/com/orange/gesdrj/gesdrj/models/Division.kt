package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity(name = "divisions")
data class Division(
        @Id
        @GeneratedValue
        val id : Long?=null,
        var libelle: String?=null,
       @OneToMany(mappedBy = "division") @JsonIgnore
       val usersDivision: List<User> = emptyList()
)

