package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.email.Mail
import com.orange.gesdrj.gesdrj.email.Sender
import com.orange.gesdrj.gesdrj.models.Demande
import com.orange.gesdrj.gesdrj.models.ProcessValidation
import com.orange.gesdrj.gesdrj.models.UserRepository
import com.orange.gesdrj.gesdrj.repositories.DemandeRepository
import com.orange.gesdrj.gesdrj.repositories.ProcessRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/processValidation")
@CrossOrigin("*")
class ProcessController {

    @Autowired
    lateinit var processRepository: ProcessRepository

    @Autowired
    lateinit var demandeRepository: DemandeRepository

    @Autowired
    lateinit var userRepository: UserRepository


    @PostMapping("/{idDemande}")
   fun saveProcess(@RequestBody listProcess:List<ProcessValidation>, @PathVariable idDemande:Long):Demande{
        var ordre:Int = 0
        val listMail:ArrayList<String> =  ArrayList()

       val demande:Demande = demandeRepository.getOne(idDemande)

        listProcess.forEach {
            val processValidation = ProcessValidation()
            ordre++
            processValidation.division = it.division
            processValidation.demandeProcess = demande
            //processValidation.demandeProcess = demandeRepository.getOne(idDemande)
            processValidation.ordreValidation = ordre
            processRepository.save(processValidation)
            /*val users = userRepository.findUserByDivision(it.division!!,processValidation.demandeProcess!!.typeContrat!!)

            users!!.forEach {
                listMail.add(it.email!!)
            }*/

        }

        val users = userRepository.findUserByDivision(listProcess[0].division!!,demande.typeContrat!!)

        if (users!!.isNotEmpty()){

            users.forEach {
                listMail.add(it.email!!)
            }

            val mail = Mail("Process Validation","Bonjour vous avez une validation à faire",listMail)
            Sender.send(mail)
        }

        //Update Demande
        demande.niveauValidation = 1
        return demandeRepository.save(demande)
    }

    @GetMapping("/{idDemande}")
    fun findProcessByDemande(@PathVariable idDemande: Long):List<ProcessValidation>{
        return processRepository.findProcessValidationByDemande(demandeRepository.getOne(idDemande))
    }

}