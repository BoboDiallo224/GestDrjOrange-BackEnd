package com.orange.gesdrj.gesdrj.email

import java.util.Date
import java.util.Properties
import javax.mail.*

import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import org.apache.tomcat.jni.User.username



object Sender {

    fun send(mail: Mail) {

        // Send mail without authentication - Work only on server allow access to SMTP
        // Begin
        val props = Properties()
        props["mail.smtp.host"] = "10.100.56.56"
        props["mail.from"] = "GestionContrat@orange-sonatel.com"
        val session = Session.getInstance(props, null)
        // End

        // Send mail with authentication
        // Begin
         /*val sender:String = "mamadoubobo.diallo3@orange-sonatel.com"
        val host:String = "10.100.56.56"
        val port:String = "587"
        val username:String = "stg_diallo87"
        val password:String = "Th%35708632"

        val props =  Properties()
        props.put("mail.smtp.host", host)
        props.put("mail.from", sender)
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.port", port)

        val session = Session.getInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })*/
        // End

        // Creation and send mail
        val msg = MimeMessage(session)
        if (mail.recipientTo != null)
            for (to in mail.recipientTo!!) {
                try {
                    msg.addRecipient(Message.RecipientType.TO, InternetAddress(to))
                } catch (e: AddressException) {
                    e.printStackTrace()
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }

            }
        if (mail.recipientCc != null)
            for (cc in mail.recipientCc!!) {
                try {
                    msg.addRecipient(Message.RecipientType.CC, InternetAddress(cc))
                } catch (e: AddressException) {
                    e.printStackTrace()
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }

            }

        try {
            msg.subject = mail.subject
            msg.sentDate = Date()
            msg.setContent(mail.message, "text/html; charset=utf-8")
            Transport.send(msg)
        } catch (mex: MessagingException) {
            System.err.println(mex.message)
        }

    }

}
