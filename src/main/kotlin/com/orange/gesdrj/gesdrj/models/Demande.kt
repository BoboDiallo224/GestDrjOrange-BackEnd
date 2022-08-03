package com.orange.gesdrj.gesdrj.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*
import javax.persistence.*

@Entity(name = "demandes")
data class Demande(
        @Id
        @GeneratedValue
        var id : Long?=null,
        var raisonSocial: String?=null,
        var statusPrisEnCharge: Boolean?=false,
        var statusDemandeComplement: Boolean?=false,
        var objetContrat: String?=null,
        var remarqueComplement: String?=null,
        var dateEntreeEnVigueur: Date?=null,
        var dateFinContrat: Date?=null,
        var datePrisEnchargeDemande: Date?=null,
        var dateUploadSignedContrat: Date?=null,
        var dateUploadFirstContrat: Date?=null,
        var dateSouscription: Date?=null,
        var dateResiliationContrat: Date?=null,
        var duration: String?=null,
        var preavis:String? = null,
        var modalityRenewal: String?=null,
        var monthlyAmountPrestation: String?=null,
        var niveauValidation: Int?=null,
        var statutHasContrat: Boolean?=false,
        var ifIsTreatedInTime:Boolean = false,
        var hasSignedContrat: Boolean?=false,
        @ManyToOne
        @JoinColumn(name = "typeContrat_id", nullable = true)
        var typeContrat: TypeContrat? = null,

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = true)
        var user: User? = null,

        @ManyToOne
        @JoinColumn(name = "demandeStatus_id", nullable = true)
        var demandeStatus: DemandeStatus? = null,

        @OneToMany(mappedBy = "documentDemande") @JsonIgnore
        val documents: List<Document>?= emptyList() ,

        @OneToMany(mappedBy = "demandeProcess")
        @JsonIgnore
        val processValidation: List<ProcessValidation>?= emptyList(),

        var contratApproved: Boolean?=false,
        var contratExpired: Boolean?=false,
        var contratResilier: Boolean?=false,

        @JsonIgnore
        @OneToMany(mappedBy = "demandeRenouvellement")
        val historiqueRenouvellement:  List<HistoriqueRenouvellement> = emptyList(),

        @OneToOne
        @JoinColumn(name = "parent_id", nullable = true)
        var parent:Demande? = null,

        var isRenewalStatus:Boolean = false,

        var confirmRenewalStatus:Boolean = false

)


