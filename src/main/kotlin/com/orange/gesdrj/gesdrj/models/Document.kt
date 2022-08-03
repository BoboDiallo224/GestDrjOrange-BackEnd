package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "documents")
data class Document (
    @Id
    @GeneratedValue
    val id : Long?=null,
    var type: String?=null,
    var name: String?=null,
    var content: String?=null,

    @ManyToOne
    @JoinColumn(name = "demande_id", nullable = true)
    var documentDemande: Demande? = null

)