package com.mwcode.server.config

import org.mindrot.jbcrypt.BCrypt

object PasswordConfig {

    /***
     * [attempt] - password received from user
     * [password] - password hashed to check
     */
    fun validatePassword(attempt: String, password: String): Boolean = BCrypt.checkpw(attempt, password)

    /**
     * Must be called before adding password to db
     */
    fun encryptPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
}
