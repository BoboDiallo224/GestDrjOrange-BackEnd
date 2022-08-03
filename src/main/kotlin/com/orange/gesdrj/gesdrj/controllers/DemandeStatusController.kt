package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.models.DemandeStatus
import com.orange.gesdrj.gesdrj.repositories.DemandeStatusRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/demandeStatus")
@CrossOrigin("*")
class DocumetsStatusController {
    @Autowired
    lateinit var demandeStatusRepository: DemandeStatusRepository

    @GetMapping("/{id}")
    fun findDemandeStatus(@PathVariable id: Long): Optional<DemandeStatus> {
        return demandeStatusRepository.findById(id)
    }

    @GetMapping
    fun findAllDemandeStatus(): List<DemandeStatus> = demandeStatusRepository.findAll()


    @PostMapping
    fun saveDemandeStatus(@RequestBody DemandeStatus: DemandeStatus): DemandeStatus {
        return  demandeStatusRepository.save(DemandeStatus)
    }

    @PutMapping
    fun updateDemandeStatus(@RequestBody DemandeStatus: DemandeStatus): DemandeStatus = demandeStatusRepository.save(DemandeStatus)
    
}