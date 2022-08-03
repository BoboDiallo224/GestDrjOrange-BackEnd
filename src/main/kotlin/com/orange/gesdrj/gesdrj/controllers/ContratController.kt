
package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.email.Mail
import com.orange.gesdrj.gesdrj.email.Sender
import com.orange.gesdrj.gesdrj.models.Contrat
import com.orange.gesdrj.gesdrj.models.HistoriqueRenouvellement
import com.orange.gesdrj.gesdrj.repositories.ContratRepository
import com.orange.gesdrj.gesdrj.repositories.DemandeRepository
import com.orange.gesdrj.gesdrj.repositories.HistoriqueRepository
import com.orange.gesdrj.gesdrj.vendor.docServerAddressContrat
import com.orange.gesdrj.gesdrj.vendor.pdfContratLocation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File

import java.util.*


@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/contrats")
class ContratController {

    @Autowired
    lateinit var contratRepository: ContratRepository

    @Autowired
    lateinit var demandeRepository: DemandeRepository

    @Autowired
    lateinit var historiqueRepository: HistoriqueRepository

    //var imagesLocation = "/home/stg_diallo87/Documents/Projet2019/UploadFilePretRH/"

    var imagesLocation = pdfContratLocation
    val docServerAddress = docServerAddressContrat


    @PostMapping("/upload/{idDemande}")
    fun uploadContrat(@RequestParam("file") file: MultipartFile,
                      @PathVariable("idDemande") idDemande:Long): String {

        val listMail:ArrayList<String> =  ArrayList()
        val demande = demandeRepository.getOne(idDemande)

        if (!demande.statutHasContrat!!){

            val imagePath = File( imagesLocation+ file.originalFilename + demande.id + "_" + UUID.randomUUID() + Date().time + "." + file.extension)
                    .apply {
                        writeBytes(file.bytes)
                    }.absolutePath

            contratRepository.save(Contrat(type = file.extension, name = file.originalFilename, content = imagePath, contratDemande = demande))

            if (demande.parent != null){

                demandeRepository.save(demandeRepository.getOne(demande.parent!!.id!!).copy(isRenewalStatus = true)).also {
                    demandeRepository.save(demande.copy(dateUploadFirstContrat = Date(),statutHasContrat = true, isRenewalStatus = false))
                }
            }

            else{
                demandeRepository.save(demande.copy(dateUploadFirstContrat = Date(),statutHasContrat = true))
            }

            listMail.add(demande.user?.email!!)

            val mail= Mail("Upload Contrat","La premiere version du contrat a été uploader",listMail)
            Sender.send(mail)
        }

        else{
            //println("is update")
            val contrat = contratRepository.findByContratDemande(demande)

            val imagePath = File( imagesLocation+ file.originalFilename + demande.id + "_" + UUID.randomUUID() + Date().time+"_reupload" + "." + file.extension)
                    .apply {
                        writeBytes(file.bytes)
                    }.absolutePath

            contratRepository.save(contrat!!.copy(type = file.extension, name = file.originalFilename, content = imagePath, contratDemande = demande))


            listMail.add(demande.user?.email!!)

            val mail= Mail("Upload Contrat","Le contrat à été modifier",listMail)
            Sender.send(mail)

        }

        return "upload contrat success"
    }

    @PostMapping("/uploadContratSinged/{idDemande}/{dateEntreVigueur}/{dateFinContrat}")
    fun uploadContratSinged(@RequestParam("file") file: MultipartFile,
                            @PathVariable("idDemande") idDemande:Long,
                            @PathVariable("dateEntreVigueur") dateEntreVigueur:Date,
                            @PathVariable("dateFinContrat") dateFinContrat:Date): String {

        val listMail:ArrayList<String> =  ArrayList()
        val demande = demandeRepository.getOne(idDemande)
        val contrat = contratRepository.findByContratDemande(demande)

        val imagePath = File( imagesLocation+ file.originalFilename + demande.id + "_" + UUID.randomUUID() + Date().time+"_signer" + "." + file.extension)
                .apply {
                    writeBytes(file.bytes)
                }.absolutePath

        if (contrat != null){

            contrat.content = imagePath

            contratRepository.save(contrat)
        }
        else{
            contratRepository.save(Contrat(type = file.extension, name = file.originalFilename, content = imagePath, contratDemande = demande))
        }

        /*
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val startDate:String = sdf.format(demande.datePrisEnchargeDemande)
        val endDate:String = sdf.format(Date())*/

        /*val dif:Long = demandeRepository.checkIfDemandeIsTratedInTime(startDate, endDate,false)
        var ifInTime:Boolean = false
        if (dif != 0L){
            ifInTime  = true
        }*/

        demandeRepository.save(demande.copy(dateEntreeEnVigueur = dateEntreVigueur,dateFinContrat = dateFinContrat))

        /*historiqueRepository.save(HistoriqueRenouvellement(dateEntreeEnVigueur = dateEntreVigueur,dateFinContrat = dateFinContrat,
                                                           dateRenouvellement = Date(),demandeRenouvellement = demande))*/

        listMail.add(demande.user?.email!!)

        val mail= Mail("Contrat final Signé","Votre Contrat final a été Signé et chargé",listMail)
        Sender.send(mail)

        return "upload image success"
    }

    @GetMapping("/{idDemande}")
    fun getContrat(@PathVariable idDemande: Long):Contrat{
        return contratRepository.getContratByIdDemande(demandeRepository.getOne(idDemande))
                .let { it.copy(content = "$docServerAddress${it.content?.substringAfterLast("/")}") }
    }

    val MultipartFile.extension: String
        get() = originalFilename!!.substringAfterLast('.')

}