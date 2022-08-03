package com.orange.gesdrj.gesdrj.models

import java.util.*
import javax.persistence.*

@Entity(name = "historiqueRenouvellement")
data class HistoriqueRenouvellement (

        @Id
        @GeneratedValue
        var id : Long?=null,
        var dateEntreeEnVigueur: Date?=null,
        var dateFinContrat: Date?=null,
        var dateRenouvellement: Date?=null,

        @ManyToOne
        @JoinColumn(name = "demande_id", nullable = false)
        var demandeRenouvellement: Demande? = null
)
