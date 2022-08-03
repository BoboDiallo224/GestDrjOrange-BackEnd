package com.orange.gesdrj.gesdrj.repositories

import com.orange.gesdrj.gesdrj.models.Demande
import com.orange.gesdrj.gesdrj.models.Division
import com.orange.gesdrj.gesdrj.models.TypeContrat


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

interface DemandeRepository : JpaRepository<Demande,Long>  {

    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_pris_encharge_demande), date(d.date_upload_signed_contrat)) <= t.delay_of_traitment and d.has_signed_contrat = :x and d.date_souscription between str_to_date(:startDate,'%Y-%m-%d') and str_to_date(:endDate,'%Y-%m-%d')" +
            " and d.type_contrat_id = t.id", nativeQuery = true)
    fun checkIfDemandeIsTratedInTime(@Param("startDate") startDate: String,
                                     @Param("endDate") endDate: String,
                                     @Param("x") hasSignedContrat: Boolean):Long


    @Query("select d from demandes d where d.statusPrisEnCharge = :x and d.contratApproved = :y and d.hasSignedContrat = false and d.contratExpired = false and d.contratResilier = false order by d.id desc")
    fun getDemandeByStatut(@Param("x")statut:Boolean,
                           @Param("y")contratApproved:Boolean):List<Demande>

    @Query("select d from demandes d where d.statusPrisEnCharge = :x and d.contratApproved = :y and d.user.division = :z and d.hasSignedContrat = false and d.contratExpired = false and d.contratResilier = false order by d.id desc")
    fun getDemandeByStatutAndDivision(@Param("x")statut:Boolean,
                                      @Param("y")contratApproved:Boolean,
                                      @Param("z")division:Division):List<Demande>


    @Query("select d from demandes d where d.statusPrisEnCharge = :x and d.contratApproved = :y and d.typeContrat = :z and d.hasSignedContrat = false and d.contratExpired = false and d.contratResilier = false order by d.id desc")
    fun getDemandeByStatutAndTypeContrat(@Param("x")statut:Boolean,
                                         @Param("y")contratApproved:Boolean,
                                         @Param("z")typeContrat:TypeContrat):List<Demande>


    @Query("select d from demandes d where d.statusPrisEnCharge = :x")
    fun getDemandeByStatut(@Param("x")statut:Boolean):List<Demande>

    @Query("select count(d) from demandes d where d.dateSouscription between :startDate and :endDate and d.hasSignedContrat = :x and d.typeContrat.libelle like :y and d.contratExpired = false and d.contratResilier = false")
    //@Query("select count(d) from demandes d where d.hasSignedContrat = :x")
    fun findTreatedDemandeBetweenTwoDates(@Param("startDate") startDate: Date,@Param("endDate") endDate: Date,
                                          @Param("x") hasSignedContrat: Boolean,@Param("y") typeContrat: String):Long


    @Query("select count(d) from demandes d where d.hasSignedContrat = :x and d.contratExpired = false and d.contratResilier = false")
    fun findTreatedDemande(@Param("x") hasSignedContrat: Boolean):Long

    @Query("select count(d) from demandes d where d.hasSignedContrat = :x and d.typeContrat.libelle like :y and d.contratExpired = false and d.contratResilier = false")
    fun findTreatedDemandeByTypeContrat(@Param("x") hasSignedContrat: Boolean,
                                        @Param("y") typeContrat: String):Long

    fun findByhasSignedContratAndContratExpiredAndContratResilierOrderByIdDesc(hasSignedContrat:Boolean,contratExpired:Boolean,
                                                                               contratResilier:Boolean):List<Demande>

    @Query("select d from demandes d where d.hasSignedContrat = true and d.user.division.libelle = :x and d.contratExpired = false and d.contratResilier = false order by id desc")
    fun findByDivision(@Param("x") division: String):List<Demande>

    @Query("select d from demandes d where d.hasSignedContrat = true and d.typeContrat.libelle like :x and d.user.direction like :y and d.contratExpired = false and d.contratResilier = false order by id desc")
    fun findByTypeContratAndDirection(@Param("x") typeContrat: String,
                                      @Param("y") direction: String):List<Demande>

    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) <= t.delay_of_traitment and d.has_signed_contrat = true and d.date_souscription between str_to_date(:startDate,'%Y-%m-%d') and str_to_date(:endDate,'%Y-%m-%d')" +
            " and d.contrat_expired = false and d.contrat_resilier = false and t.libelle like :libelle and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedAtTimeBeetwenAndTypeContrat(@Param("startDate") startDate: String,
                                       @Param("endDate") endDate: String,
                                       @Param("libelle") libelle: String):Long

    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) <= t.delay_of_traitment and d.has_signed_contrat = true and d.date_souscription between str_to_date(:startDate,'%Y-%m-%d') and str_to_date(:endDate,'%Y-%m-%d')" +
            " and d.contrat_expired = false and d.contrat_resilier = false and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedAtTimeBeetwen(@Param("startDate") startDate: String,
                                       @Param("endDate") endDate: String):Long

    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) <= t.delay_of_traitment and d.has_signed_contrat = true and t.libelle like :libelle" +
            " and d.contrat_expired = false and d.contrat_resilier = false and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedAtTimeByTypeContrat(@Param("libelle") libelle: String):Long


    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) <= t.delay_of_traitment and d.has_signed_contrat = true " +
            " and d.contrat_expired = false and d.contrat_resilier = false and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedAtTime():Long


    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) > t.delay_of_traitment and d.has_signed_contrat = true" +
            " and d.contrat_expired = false and d.contrat_resilier = false and t.libelle like :libelle and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedInLateByTypeContrat(@Param("libelle") libelle: String):Long

    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) > t.delay_of_traitment and d.has_signed_contrat = true and d.date_souscription between str_to_date(:startDate,'%Y-%m-%d') and str_to_date(:endDate,'%Y-%m-%d')" +
            " and d.contrat_expired = false and d.contrat_resilier = false and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedInLateBeetwen(@Param("startDate") startDate: String,
                                       @Param("endDate") endDate: String):Long

    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) > t.delay_of_traitment and d.has_signed_contrat = true and d.date_souscription between str_to_date(:startDate,'%Y-%m-%d') and str_to_date(:endDate,'%Y-%m-%d')" +
            " and d.contrat_expired = false and d.contrat_resilier = false and t.libelle like :libelle and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeTreatedInLateBeetwenAndTypePret(@Param("startDate") startDate: String,
                                                  @Param("endDate") endDate: String,
                                                  @Param("libelle") libelle: String):Long


    @Query("select count(d.id) from drj.demandes d, drj.type_contrats t where datediff(date(d.date_upload_signed_contrat), date(d.date_pris_encharge_demande)) > t.delay_of_traitment and d.has_signed_contrat = true" +
            " and d.contrat_expired = false and d.contrat_resilier = false and d.type_contrat_id = t.id order by d.id desc", nativeQuery = true)
    fun getDemandeNotTreatedAtTime():Long


    fun findDemandeByContratResilierOrderByIdDesc(contratResilier: Boolean):List<Demande>

    @Query("select d from demandes d where d.contratResilier = :x and d.user.division = :div order by d.id desc")
    fun findDemandeByContratResilierAndDivision(@Param("x")contratResilier: Boolean,
                                                @Param("div")division: Division):List<Demande>


    @Query("select d from demandes d where d.dateFinContrat <= :x and d.isRenewalStatus = false order by d.id desc")
    fun findExpiredDemande(@Param("x")date: Date):List<Demande>

    @Query("select d from demandes d where d.dateFinContrat <= :x and d.user.division = :div and d.isRenewalStatus = false order by d.id desc")
    fun findExpiredDemandeByDivision(@Param("x")date: Date,
                                     @Param("div")division: Division):List<Demande>

    @Query("select d from demandes d where d.dateFinContrat <= :x and d.typeContrat = :div and d.isRenewalStatus = false order by d.id desc")
    fun findExpiredDemandeByTypeContrat(@Param("x")date: Date,
                                        @Param("div")typeContrat:TypeContrat):List<Demande>


    @Query("select d from demandes d where d.contratExpired = false and d.contratResilier = false and d.dateFinContrat >= :x " +
            "and d.isRenewalStatus = false order by id desc")
    fun findDemandeEnCours(@Param("x")date: Date):List<Demande>

    @Query("select d from demandes d where d.contratExpired = false and d.contratResilier = false and d.dateFinContrat >= :x  and d.user.division = :y " +
            "and d.isRenewalStatus = false order by id desc")
    fun findDemandeEnCoursByDivision(@Param("x")date: Date,
                                     @Param("y")division: Division):List<Demande>

    @Query("select d from demandes d where d.contratExpired = false and d.contratResilier = false and d.dateFinContrat >= :x  and d.typeContrat = :y " +
            "and d.isRenewalStatus = false order by id desc")
    fun findDemandeEnCoursByTypeContrat(@Param("x")date: Date,
                                        @Param("y")typeContrat:TypeContrat):List<Demande>


    @Query("select d from demandes d where d.contratExpired = false and d.contratResilier = false and d.dateFinContrat >= :x" +
            " and d.typeContrat.libelle like :y and d.user.direction like :z order by id desc")
    fun findSearchDemandeEnCours(@Param("x")date: Date,
                                 @Param("y") typeContrat: String,
                                 @Param("z") direction: String):List<Demande>

    @Query("select d from demandes d where d.contratExpired = false and d.contratResilier = false and d.dateFinContrat >= :x" +
            " and d.typeContrat.libelle like :y and d.user.direction like :z and d.user.division = :div order by id desc")
    fun findSearchDemandeEnCoursByDivision(@Param("x")date: Date,
                                           @Param("y") typeContrat: String,
                                           @Param("z") direction: String,
                                           @Param("div") division: Division):List<Demande>


}