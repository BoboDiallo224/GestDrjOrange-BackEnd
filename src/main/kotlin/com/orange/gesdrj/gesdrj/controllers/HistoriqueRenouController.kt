package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.models.HistoriqueRenouvellement
import com.orange.gesdrj.gesdrj.repositories.DemandeRepository
import com.orange.gesdrj.gesdrj.repositories.HistoriqueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/historiques")
class HistoriqueRenouController {

    @Autowired
    lateinit var historiqueRepository: HistoriqueRepository

    @Autowired
    lateinit var demandeRepository: DemandeRepository

    @GetMapping("/{idDemande}")
    fun getHistoriqueDemande(@PathVariable idDemande:Long):List<HistoriqueRenouvellement>{

        return historiqueRepository.findByDemandeRenouvellementOrderByIdDesc(demandeRepository.getOne(idDemande))

    }

}