package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity(name = "processValidation")
data class ProcessValidation (
    @Id
    @GeneratedValue
    var id : Long? = null,
    var division: String? = null,
    var ordreValidation: Int? = null,
    var statutValidation: Boolean? = false,
    var commentaire: String? = null,
    var dateValidation:Date? = Date(),
    @ManyToOne
    @JoinColumn(name = "idDemande", nullable = true)
    var demandeProcess: Demande?  = null,

    var userValid: Long? = null

)
