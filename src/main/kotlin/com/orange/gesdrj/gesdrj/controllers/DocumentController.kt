

package com.orange.gesdrj.gesdrj.controllers



import com.orange.gesdrj.gesdrj.models.Document
import com.orange.gesdrj.gesdrj.repositories.DemandeRepository
import com.orange.gesdrj.gesdrj.repositories.DocumentRepository
import com.orange.gesdrj.gesdrj.vendor.docServerAddressDocument
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus




@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping("/documents")
class DocumentController {

    @Autowired
    lateinit var documentRepository: DocumentRepository

    @Autowired
    lateinit var demandeRepository: DemandeRepository

    //val docServerAddress = "http://10.173.36.23:8000/"

    val docServerAddress = docServerAddressDocument

    @GetMapping("/{idDemande}")
    fun getDocument(@PathVariable idDemande: Long):List<Document>{
        return documentRepository.getDocumentByIdDemande(demandeRepository.getOne(idDemande))
                .map { it.copy(content = "$docServerAddress${it.content?.substringAfterLast("/")}") }
    }

    @PutMapping()
    fun updateDocument(@RequestBody document: Document): Document {

        return documentRepository.save(document)
    }

    fun convertMultiPartToFile(file: MultipartFile): File {
        val convFile: File = File(file.originalFilename)
        val fos: FileOutputStream = FileOutputStream(convFile)
        fos.write(file.bytes)
        fos.close()
        return convFile
    }

    @DeleteMapping("/removeUpload/{idDemande}")
    fun deleteDocument(@PathVariable("idDemande")idDemande:Long):ResponseEntity<Long>{

        //var document = documentRepository.getOne(idDemande)

        /*var imagePath = documentRepository.getDocumentByIdDemande(demandeRepository.getOne(idDemande))
                .map { it.copy(content = "$docServerAddress${it.content?.substringAfterLast("/")}") }

         var file:File = imagePath)*/

        documentRepository.deleteById(idDemande)

        return ResponseEntity<Long>(idDemande, HttpStatus.OK)

    }

}