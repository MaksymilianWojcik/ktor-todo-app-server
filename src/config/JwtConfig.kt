package com.mwcode.server.config

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.mwcode.server.domain.model.Account
import java.util.*

object JwtConfig {

    const val JWT_CLAIM_ID = "id"
    const val JWT_CLAIM_USERNAME = "username"
    const val JWT_REALM = "mwcode todo app"
    const val AUDIENCE = "com.mwcode.todo"

    private const val SECRET = "zAP5MBA4B4Ijz0MZaS48"
    private const val ISSUER = "https://jwt-provider-domain/"
    private const val VALIDITY_MS = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(SECRET)

    val verifier: JWTVerifier = JWT
        .require(algorithm) // verify the token signature using defined algorithm
        .withAudience(AUDIENCE) // verify the token was issued to be used with this protected resource
        .withIssuer(ISSUER) // verify the token was issued by the expected authority
        .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(account: Account): String = JWT.create()
        .withIssuedAt(Date())
        .withSubject("Authentication")
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(JWT_CLAIM_ID, account.id)
        .withClaim(JWT_CLAIM_USERNAME, account.username)
        // .withExpiresAt(getExpiration()) // TODO: Expirtion
        .sign(algorithm)

    fun decodeToken(token: String): DecodedJWT = JWT.require(algorithm).build().verify(token)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + VALIDITY_MS)
}
