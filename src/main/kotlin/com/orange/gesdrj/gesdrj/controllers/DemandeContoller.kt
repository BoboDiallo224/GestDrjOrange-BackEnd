package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.email.Mail
import com.orange.gesdrj.gesdrj.email.Sender
import com.orange.gesdrj.gesdrj.models.*
import com.orange.gesdrj.gesdrj.repositories.*
import com.orange.gesdrj.gesdrj.vendor.pdfDocumentLocation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import javax.websocket.server.PathParam


@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/demandes")
class DemandeController {

    @Autowired
    lateinit var demandeRepository: DemandeRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var documentRepository: DocumentRepository

    @Autowired
    lateinit var processRepository: ProcessRepository

    @Autowired
    lateinit var divisionRepository: DivisionRepository

    @Autowired
    lateinit var historiqueRepository: HistoriqueRepository

    var imagesLocation = pdfDocumentLocation

    //var imagesLocation = "/home/stg_diallo87/Documents/Projet2019/UploadFilePretRH/"


    @GetMapping ("/okk/{idProcess}")
    fun Okk(@PathVariable idProcess: Long){
        val processValidation: ProcessValidation = processRepository.getOne(idProcess)
        processValidation.statutValidation = false
                processRepository.save(processValidation)
    }


    @GetMapping ("/{idProcess}/{division}/{idUser}/{commentaire}")
    fun updateProcessValidation(@PathVariable idProcess: Long,
                                @PathVariable division: String,
                                @PathVariable idUser: Long,
                                @PathVariable commentaire:String): ProcessValidation {

        //val demande: Demande = demandeRepository.getOne(idDemande)
        val listMail:ArrayList<String> =  ArrayList()
        var nextDivision : String = ""

        val processValidation: ProcessValidation = processRepository.findByIdAndDivisionAndStatutValidation(
                idProcess,division.trim(' ','\n','\r', '\t'),false)!!

        processValidation.statutValidation = true
        processValidation.commentaire = commentaire
        processValidation.userValid = idUser
        processValidation.dateValidation = Date()

        //Recuperation des processValidation non valides
        val listProcessValidation:ArrayList<ProcessValidation>? =  processRepository.findByidAndStatutValidation(idProcess,false)

        //Changement du niveau de validation de la demande
        if (listProcessValidation!=null && listProcessValidation.size > 0) {

            processValidation.demandeProcess?.niveauValidation = processValidation.demandeProcess?.niveauValidation!! + 1

            nextDivision = listProcessValidation[0].division!!
        }

        //Update niveau validation demande
        demandeRepository.save(processValidation.demandeProcess!!)

        val users = userRepository.findUserByDivision(nextDivision, processValidation.demandeProcess!!.typeContrat!!)
        //print(user.size)
        if (users!!.isNotEmpty()) {

            users.forEach {
                listMail.add(it.email!!)
            }

            val mail= Mail("Validation de contrat","Hello, Vous avez une validation a faire. Urgent!",listMail)

            Sender.send(mail)
        }

        return processRepository.save(processValidation)
    }


    @GetMapping ("reject/{idDemande}/{division}/{idUser}/{commentaire}")
    fun rejectProcessValidation(@PathVariable idDemande: Long,
                                 @PathVariable division: String,
                                 @PathVariable idUser: Long,
                                 @PathVariable commentaire:String): ProcessValidation {

        val mail : Mail
        val listMail : ArrayList<String> =  ArrayList()

        val demande = demandeRepository.getOne(idDemande)

        //Recuperation des processValidation valides
        val listProcessValidation:ArrayList<ProcessValidation>? =  processRepository.findByDemandeProcessAndStatutValidation(demande,true)

        //Changement du niveau de validation de la demande
        if (listProcessValidation!=null && listProcessValidation.size > 0) {

            listProcessValidation.forEach {
                println("inside")
                it.statutValidation = false
                it.userValid = null
                processRepository.save(it)
            }
        }

        val processValidation:ProcessValidation = processRepository.findByDivisionAndDemandeProcess(division, demande)!!

        //Remettre le niveau de validation de la Demande a 0
        processValidation.demandeProcess?.niveauValidation = 1

        demandeRepository.save(processValidation.demandeProcess!!)

        //Find Users who are managing this type of Contrat
        userRepository.findByTypeContrat(processValidation.demandeProcess?.typeContrat!!)!!.map {

            listMail.add(it.email!!)
        }

        mail = Mail("Rejet de Validation ","la demande à été rejeter par la division "+division+" Voici le commentaire "+commentaire,listMail)

        Sender.send(mail)

        processValidation.userValid = idUser

        processValidation.commentaire = commentaire

        return processRepository.save(processValidation)
    }

    @GetMapping("/{id}")
    fun findDemande(@PathVariable id: Long): Optional<Demande> {

        return demandeRepository.findById(id)
    }


    @GetMapping("list/{statut}/{contratApproved}/{userLogin}")
    fun findAllDemande(@PathVariable statut : Boolean,
                       @PathVariable contratApproved : Boolean,
                       @PathVariable userLogin : String) : List<Demande> {

        return if (userLogin == "nan"){

             demandeRepository.getDemandeByStatut(statut,contratApproved)
        }

        else {

            val user = userRepository.findByUsername(userLogin)!!
            var role : String = ""

            user.roles.forEach {

                when (it == "ROLE_GESTIONNAIRE") {
                    true -> role = it
                }
            }

            if (role == "ROLE_GESTIONNAIRE") {

                val typeContrat:TypeContrat = user.typeContrat!!

                if (typeContrat.id != null) {
                    println("yes")
                    demandeRepository.getDemandeByStatutAndTypeContrat(statut,contratApproved,typeContrat)
                }

                else {
                    listOf()
                }

            }

            else {

                val division = user.division
                demandeRepository.getDemandeByStatutAndDivision(statut,contratApproved,division!!)
            }

        }

    }

    @GetMapping("list/traiter/{hasSignedContrat}/{contratExpired}/{contratResilier}")
    fun findAllDemandeTraiter(@PathVariable hasSignedContrat : Boolean,
                              @PathVariable contratExpired : Boolean,
                              @PathVariable contratResilier : Boolean): List<Demande> =

            demandeRepository.findByhasSignedContratAndContratExpiredAndContratResilierOrderByIdDesc(hasSignedContrat,
                                                                                                     contratExpired,
                                                                                                     contratResilier)
    @GetMapping("listtraiter/byDivision/{division}")
    fun findAllDemandeTraiterByDivision(@PathVariable division:String): List<Demande> =

            demandeRepository.findByDivision(division)

    @PostMapping("/saveDemande")
    //@Throws(RuntimeException::class)
    fun saveDemande(@RequestBody demande: Demande): Demande? {

        val listMail:ArrayList<String> =  ArrayList()

        val user = userRepository.findByTypeContrat(demande.typeContrat!!)//?:throw RuntimeException("Invalid.404")

        return if (user != null){

            val demandeSave = Demande(raisonSocial =  demande.raisonSocial, objetContrat = demande.objetContrat,
                                      dateSouscription = Date(), duration = demande.duration,
                                      preavis =  demande.preavis, modalityRenewal = demande.modalityRenewal,
                                      monthlyAmountPrestation = demande.monthlyAmountPrestation,
                                      user = demande.user, typeContrat = demande.typeContrat)

            //print(user.size)
            user.forEach  {
                listMail.add(it.email!!)
            }
            //print(listMail.size)

            val mail= Mail("Demande de Contrat","Bonjour,<br> La division "+demande.user!!.division!!.libelle+" vient de souscrir à une nouvelle demande d'élaboration de contrat <h5>"+demande.typeContrat!!.libelle+"</h5> merci de prendre en charge",listMail)

            Sender.send(mail)

            demandeRepository.save(demandeSave)
        }

        else {
            null
        }

    }



    @PutMapping("/approved")
    fun updateApprovedDemande(@RequestBody demande: Demande ): Demande {

        val listMail:ArrayList<String> =  ArrayList()
        val mail:Mail?
        val user = userRepository.findByTypeContrat(demande.typeContrat!!)

        //print(user.size)
        if (user != null){

            user.forEach  {
                listMail.add(it.email!!)

                //mail = Mail("Contrat Approuvé","Bonjour Mr "+it.name+" le contrat "+demande.typeContrat!!.libelle+" a été approuvé par la division "+demande.user!!.division!!.libelle,listMail)
            }

            mail = Mail("Contrat Approuvé","Bonjour le contrat "+demande.typeContrat!!.libelle+" a été approuvé par la division "+demande.user!!.division!!.libelle,listMail)
            Sender.send(mail)
        }

        else{

        }

        return demandeRepository.save(demande)
    }

    @GetMapping("/sendMail")
    fun sendMail(@PathParam("message") message : String,
                 @PathParam("idDemande") idDemande : Long) : String {

        val listMail:ArrayList<String> =  ArrayList()

        val user = userRepository.findByTypeContrat(demandeRepository.getOne(idDemande).typeContrat!!)
        //print(user.size)
        user!!.forEach  {
            listMail.add(it.email!!)
        }

        val mail= Mail("Contrat Non Approuvé", message, listMail)
        Sender.send(mail)

        return "Mail send successfully"
    }


    @PutMapping("/{id}")
    fun updateDemande(@PathVariable id : Long, @RequestBody demande : Demande) : Demande {

        val listMail:ArrayList<String> =  ArrayList()

        val user = userRepository.findByTypeContrat(demande.typeContrat!!)

        user!!.forEach  {
            listMail.add(it.email!!)
        }

        val mail= Mail("Documents Imcomplets","La demande de complement de document a été gérer par la <h5>"+demande.user!!.division!!.libelle+"</h5> </br>Merci de confirmer",listMail)

        Sender.send(mail)

        demande.id = id
        return demandeRepository.save(demande)
    }

    @PutMapping("/accept")
    fun updateDemandePrisEnCharge(@RequestBody demande: Demande):Boolean {

        //demande.datePrisEnchargeDemande = Date()
        val listMail:ArrayList<String> =  ArrayList()

        listMail.add(demande.user!!.email!!)

        val mail= Mail("Pris En charge","Bonjour,<br>Mr "+demande.user!!.name+"<br>Vôtre demande d'élaboration de contrat "+demande.typeContrat!!.libelle+" a été pris en charge et le delais de traitement est de <span style='color:orange'><b>${demande.typeContrat!!.delayOfTraitment}"+" jours</b></span>",listMail)

         demandeRepository.save(demande.copy(datePrisEnchargeDemande = Date()))

        Sender.send(mail)

        return true
    }

    @PutMapping("/rejet")
    fun updateDemandeManqueDoc(@RequestBody demande:Demande):Demande{

        val listMail:ArrayList<String> =  ArrayList()

        listMail.add(demande.user!!.email!!)

        val mail= Mail("Demande Rejetée","Bonjour,<br> Mr "+demande.user!!.name+"<br> Vôtre demande d'élaboration de contrat "+demande.typeContrat!!.libelle+" a été réjeter en " +
                "raison de:<br>"+"<h5>Motif:</h5>"+demande.remarqueComplement,listMail)

        Sender.send(mail)

        return demandeRepository.save(demande)

    }

    @PostMapping("/upload/{idDemande}")
    fun saveDocument(@RequestParam("file") file: MultipartFile, @PathVariable("idDemande") idDemande:Long): String {

        val demande = demandeRepository.getOne(idDemande)

        val imagePath = File( imagesLocation+ file.originalFilename + demande.id + "_" + UUID.randomUUID() + Date().time + "." + file.extension)
                .apply {
                    writeBytes(file.bytes)
                }.absolutePath

        documentRepository.save(Document(type = file.extension, name = file.originalFilename, content = imagePath, documentDemande = demande))

        return "upload image success"

    }

    val MultipartFile.extension: String
        get() = originalFilename!!.substringAfterLast('.')


    @PostMapping("statistique/{startDate}/{endDate}/{hasSignedContrat}")
    fun statistiqueDemande(@PathVariable startDate:String?, @PathVariable endDate:String?,
                           @PathVariable hasSignedContrat: Boolean,@RequestBody typeContrat: TypeContrat?):Long{

        val formatter = SimpleDateFormat("yyy-MM-dd")

        println(typeContrat)

        val response:Long = if (startDate == "0000-00-00"  && endDate == "0000-00-00" && typeContrat?.id  == null) {
            demandeRepository.findTreatedDemande(hasSignedContrat)
        }

        else if (startDate == "0000-00-00" && endDate == "0000-00-00" && typeContrat != null){

            demandeRepository.findTreatedDemandeByTypeContrat(hasSignedContrat, "%${typeContrat.libelle!!}%")

        }

        else{

            val dteone:Date = formatter.parse(startDate)

            val dtetwo:Date = formatter.parse(endDate)

            demandeRepository.findTreatedDemandeBetweenTwoDates(dteone, dtetwo,hasSignedContrat,"%${typeContrat?.libelle!!}%")
        }
        return response
    }

    @PostMapping("treatedAtTime/{startDate}/{endDate}")
    fun getDemandeTreatedAtTime(@PathVariable startDate:String?, @PathVariable endDate:String?,
                                @RequestBody typeContrat: TypeContrat?):Long {

        val response:Long = if (startDate == "0000-00-00" && endDate == "0000-00-00" && typeContrat  == null) {

            demandeRepository.getDemandeTreatedAtTime()
        }

        else if (startDate != "0000-00-00" || endDate != "0000-00-00" && typeContrat == null){

            demandeRepository.getDemandeTreatedAtTimeBeetwen(startDate!!, endDate!!)
        }

        else if (startDate == "0000-00-00" || endDate == "0000-00-00" && typeContrat != null){
            demandeRepository.getDemandeTreatedAtTimeByTypeContrat("%${typeContrat!!.libelle!!}%")
        }

        else{

            demandeRepository.getDemandeTreatedAtTimeBeetwenAndTypeContrat(startDate, endDate!!, "%${typeContrat!!.libelle!!}")
        }
        return response

    }

    @PostMapping("treatedInLate/{startDate}/{endDate}")
    fun getDemandeTreatedInLate(@PathVariable startDate:String?, @PathVariable endDate:String?,
                                @RequestBody typeContrat: TypeContrat?):Long {

        val response:Long = if (startDate == "0000-00-00" && endDate == "0000-00-00" && typeContrat  == null) {

            demandeRepository.getDemandeNotTreatedAtTime()
        }

        else if (startDate == "0000-00-00" || endDate == "0000-00-00" && typeContrat != null){

            demandeRepository.getDemandeTreatedInLateByTypeContrat("$${typeContrat!!.libelle!!}$")
        }

        else if (startDate != "0000-00-00" || endDate != "0000-00-00" && typeContrat == null){

            demandeRepository.getDemandeTreatedInLateBeetwen(startDate!!, endDate!!)
        }

        else{

            demandeRepository.getDemandeTreatedInLateBeetwenAndTypePret(startDate, endDate!!, "${typeContrat!!.libelle !!}%")
        }

        return response
    }

    @PostMapping("/searchDemandeByTypeContratAndDirection")
    fun findDemandeByTypeContratAndDirection(@RequestBody demande:Demande):List<Demande>{

        return demandeRepository.findByTypeContratAndDirection(
                "%${demande.typeContrat!!.libelle}%",
                "%${demande.user!!.direction}%")

    }

    /*@GetMapping("/long")
    fun long():Demande{

        val demande:Demande
        demande = demandeRepository.getOne(51)
        demande.contratExpired = true
        demande.contratResilier = true
        return demandeRepository.save(demande)
    }*/

    @PostMapping("/renouvelementContrat")
    fun renouvelementContrat(@RequestBody demande: Demande):Demande{

        /*val newDateEntrer = historiqueRepository.findByDemandeRenouvellementOrderByIdDesc(demande)[0].dateFinContrat

        historiqueRepository.save(HistoriqueRenouvellement(dateEntreeEnVigueur = newDateEntrer,
                dateFinContrat = demande.dateFinContrat,
                dateRenouvellement = Date(),
                demandeRenouvellement = demande))*/

        //demandeRepository.save(demandeRepository.getOne(demande.id!!).copy(isRenewalStatus = true))

        //val demandeSave = demande.copy(id= null,isRenewalStatus = false)

        val listMail:ArrayList<String> =  ArrayList()

        val demandeSave = demande.copy(id= null, statusPrisEnCharge = false, statusDemandeComplement = false,
                                       niveauValidation = null, statutHasContrat = false,contratApproved = false,
                                       hasSignedContrat = false, contratExpired = false, contratResilier = false,
                                       dateSouscription = Date(), dateEntreeEnVigueur = null, dateUploadSignedContrat = null,
                                       datePrisEnchargeDemande = null, dateFinContrat = null, dateUploadFirstContrat = null, parent = demande)

        val user = userRepository.findByTypeContrat(demande.typeContrat!!)

        user!!.forEach  {
            listMail.add(it.email!!)
        }

        val mail= Mail("Demande de Renouvellement","Bonjour,<br> La divison "+demande.user!!.division!!.libelle+" à envoyer une demande de renouvellement du contrat "+demande.typeContrat!!.libelle+" <br> Merci de prendre en charge",listMail)

        Sender.send(mail)

        return demandeRepository.save(demandeSave)
    }


    @GetMapping("historique/{idDemande}")
    fun getHistoriqueDemande(@PathVariable idDemande:Long):List<Demande>{

        val listDemande = ArrayList<Demande>()
        var demande:Demande

        var etat = true

        demande = demandeRepository.getOne(idDemande)

        listDemande.add(demande)

        while (etat){

            if(demande.parent != null){

                demande = demandeRepository.getOne(demande.parent?.id!!)
                listDemande.add(demande)
            }

            else{

                etat = false
            }

        }

        return listDemande
    }

    @GetMapping("/demandeResiliationContrat/{idDemande}")
    fun demandeResiliationContrat(@PathVariable idDemande: Long):Demande{

        val listMail:ArrayList<String> =  ArrayList()
        val demand = demandeRepository.getOne(idDemande)

        demand.confirmRenewalStatus = true

        val user = userRepository.findByTypeContrat(demand.typeContrat!!)

        user!!.forEach  {
            listMail.add(it.email!!)
        }

        val mail= Mail("Demande de Résialiation Contrat","Bonjour,<br> La division "+demand.user!!.division!!.libelle+" à envoyer une demande de résiliation du contrat <h5>"+demand.typeContrat!!.libelle+"</h5> avec "+demand.raisonSocial+" merci de prendre en charge",listMail)

        Sender.send(mail)

        return demandeRepository.save(demand)
    }


    @PostMapping("/resilierContrat")
    fun resilierContrat(@RequestBody demande: Demande):Demande {

        val listMail:ArrayList<String> =  ArrayList()

        demande.dateResiliationContrat = Date()

        demande.confirmRenewalStatus = false

        listMail.add(demande.user!!.email!!)

        val mail= Mail("Demande de Résialiation Contrat","Bonjour,<br> Vôtre contrat de "+demande.typeContrat!!.libelle+" a été résilier <br> Merci de vérifier",listMail)

        Sender.send(mail)
        return demandeRepository.save(demande)
    }

    @PostMapping("/reject/resiliationContrat")
    fun rejectResiliationContrat(@RequestBody demande: Demande):Demande {

        val listMail:ArrayList<String> =  ArrayList()

        demande.dateResiliationContrat = null

        demande.confirmRenewalStatus = false

        listMail.add(demande.user!!.email!!)

        val mail= Mail("Demande de Résialiation Contrat","Bonjour,<br>Vôtre demande de résiliation du contrat de "+demande.typeContrat!!.libelle+" a été rejeté",listMail)

        Sender.send(mail)
        return demandeRepository.save(demande)
    }

    @GetMapping("/demandeEnCours/{userLogin}")
    fun findDemandeContratEnCours(@PathVariable userLogin: String):List<Demande>{

        return if (userLogin =="nan"){
            demandeRepository.findDemandeEnCours(Date())
        }

        else{

            val user = userRepository.findByUsername(userLogin)!!
            var role:String = ""

            user.roles.forEach {

                when (it == "ROLE_GESTIONNAIRE"){
                    true -> role = it
                }
            }

            if (role == "ROLE_GESTIONNAIRE"){

                val typeContrat:TypeContrat = user.typeContrat!!

                if (typeContrat.id != null){
                    demandeRepository.findDemandeEnCoursByTypeContrat(Date(),typeContrat)
                }

                else{
                    listOf()
                }

            }

            else{

                val division = user.division

                demandeRepository.findDemandeEnCoursByDivision(Date(), division!!)
            }

        }

    }

    @PostMapping("/searchDemandeEnCours")
    fun findSeachDemandeContratEnCours(@RequestBody demande: Demande):List<Demande>{

         return if (demande.user!!.username == "nan"){

            demandeRepository.findSearchDemandeEnCours(
                    Date(),
                    "%${demande.typeContrat!!.libelle}%",
                    "%${demande.user!!.direction}%")
        }

        else{

             val user = userRepository.findByUsername(demande.user?.username!!)!!
             var role:String = ""

             user.roles.forEach {

                 when (it == "ROLE_GESTIONNAIRE"){
                     true -> role = it
                 }
             }

             if (role == "ROLE_GESTIONNAIRE"){

                 val typeContrat:TypeContrat = user.typeContrat!!

                 if (typeContrat.id != null){

                     demandeRepository.findSearchDemandeEnCours(
                             Date(),
                             "%${typeContrat.libelle}%",
                             "%${demande.user!!.direction}%")
                 }

                 else{
                     listOf()
                 }

             }

             else{

                 val division = user.division

                 demandeRepository.findSearchDemandeEnCoursByDivision(
                         Date(),
                         "%${demande.typeContrat!!.libelle}%",
                         "%${demande.user!!.direction}%",
                         division!!)
             }

         }

    }

    @GetMapping("/demandeResilier/{username}")
    fun findDemandeContratResilier(@PathVariable username:String):List<Demande>{

        return if (username == "nan"){

            demandeRepository.findDemandeByContratResilierOrderByIdDesc(true)
        }

        else{

            val division = userRepository.findByUsername(username)!!.division
            demandeRepository.findDemandeByContratResilierAndDivision(true,division!!)
        }

    }

    @GetMapping("/demandeExpirer/{username}")
    fun findDemandeContratExpirer(@PathVariable username:String):List<Demande>{

        return if (username == "nan"){

            demandeRepository.findExpiredDemande(Date())
        }

        else{

            val user = userRepository.findByUsername(username)!!
            var role:String = ""

            user.roles.forEach {

                when (it == "ROLE_GESTIONNAIRE"){
                    true -> role = it
                }
            }

            if (role == "ROLE_GESTIONNAIRE"){

                val typeContrat:TypeContrat = user.typeContrat!!

                if (typeContrat.id != null){

                    demandeRepository.findExpiredDemandeByTypeContrat(Date(),typeContrat)
                }

                else{
                    listOf()
                }

            }

            else{

                val division = user.division
                demandeRepository.findExpiredDemandeByDivision(Date(),division!!)
            }

        }

    }

    @PostMapping("/chargeDemande")
    fun chargementContrat(@RequestBody demande: Demande):Demande?{

        //statutHasContrat = false hasSignedContrat

        demande.user = userRepository.findByDivision(demande.user!!.division!!).first()

        return if (demande.user != null){

            val demandeSave = demande.copy(statusPrisEnCharge = true,
                                           niveauValidation = null, contratApproved = true,
                                           contratExpired = false, contratResilier = false,
                                           dateSouscription = Date(),
                                           datePrisEnchargeDemande = Date(), dateUploadFirstContrat = null)

             demandeRepository.save(demandeSave)

        }

        else{
            return null
        }
    }

}

