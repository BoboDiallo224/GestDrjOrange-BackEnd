package com.orange.gesdrj.gesdrj.controllers

import com.orange.gesdrj.gesdrj.models.User
import com.orange.gesdrj.gesdrj.models.UserRepository
import com.orange.gesdrj.gesdrj.repositories.DivisionRepository
import com.orange.gesdrj.gesdrj.repositories.TypeContratRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
class UserController{

    @Autowired
    lateinit var userRepository: UserRepository

   @PostMapping("/addNewUser")
   fun addUser(@RequestBody user : User) : User? {
       return userRepository.save(user)
   }

    @GetMapping("/{userName}")
    fun findUser(@PathVariable userName : String) : User? {
        return userRepository.findUserByUserName(userName)
    }

    @GetMapping
    fun findAllUser(): List<User> = userRepository.getAllUsers()

    @PutMapping
    fun updateUser(@RequestBody user: User): User {

       return userRepository.save(user)
    }

    @PutMapping("/{userName}/upload")
    fun uploadFileSignature(@Valid @PathVariable("userName") userName: String, @RequestParam("files") file: MultipartFile) : String {

        userRepository.save(userRepository.findUserByUserName(userName)!!.copy(typeImageSignature = file.extension, contentImageSignature = file.toBase64()))

        return "upload image success"
    }

    /*@GetMapping("/{id}")
    fun getUserSignatureById(@PathVariable id: Long): User = userRepository.getOne(id)
*/
    val MultipartFile.extension: String
        get() = originalFilename!!.substringAfterLast('.')

    //Convert to Base64
    fun MultipartFile.toBase64() = Base64.getEncoder().encodeToString(this.bytes)!!

}

