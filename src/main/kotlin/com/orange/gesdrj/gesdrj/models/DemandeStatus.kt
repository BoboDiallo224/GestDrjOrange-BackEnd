package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity(name = "demandeStatus")
data class DemandeStatus (
    @Id
    @GeneratedValue
    val id : Long?=null,
    var libelle: String?=null,

    @JsonIgnore
    @OneToMany(mappedBy = "demandeStatus")
    val demandes:List<Demande> = emptyList()
    )