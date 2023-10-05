package top.chilfish.chilpost.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jwk.RsaJsonWebKey
import org.jose4j.jwk.RsaJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.consumer.InvalidJwtException
import org.jose4j.jwt.consumer.JwtConsumerBuilder


// 30 days
val expTime = 60 * 60 * 24 * 30 + System.currentTimeMillis() / 1000
const val ALGO = AlgorithmIdentifiers.RSA_USING_SHA256


val jwk = genKeys()

private fun genKeys(): RsaJsonWebKey {
    val jwk = RsaJwkGenerator.generateJwk(2048)
    jwk.keyId = "k1"
    return jwk
}

inline fun <reified T> genPayload(subject: T): String? {
    val claims = JwtClaims()
    claims.expirationTime = NumericDate.fromSeconds(expTime)
    claims.setGeneratedJwtId()
    claims.setIssuedAtToNow()
    claims.subject = Json.encodeToString(subject)
    return claims.toJson()
}

inline fun <reified T> getToken(data: T): String {
    val jws = JsonWebSignature()

    jws.setPayload(genPayload(data))
    jws.setKey(jwk.privateKey)

    jws.keyIdHeaderValue = jwk.keyId
    jws.algorithmHeaderValue = ALGO
    return jws.getCompactSerialization()
}

inline fun <reified T> verifyToken(token: String): T? {
    val jwtConsumer = JwtConsumerBuilder()
        .setRequireExpirationTime()
        .setAllowedClockSkewInSeconds(30)
        .setRequireSubject()
        .setVerificationKey(jwk.key)
        .setJwsAlgorithmConstraints(
            AlgorithmConstraints.ConstraintType.PERMIT, ALGO
        )
        .build()

    try {
        val jwtClaims = jwtConsumer.processToClaims(token)
        return Json.decodeFromString(jwtClaims.subject.toString())
    } catch (e: InvalidJwtException) {
//        println("Invalid JWT! $e")
        if (e.hasExpired()) {
            println("JWT expired at " + e.jwtContext.jwtClaims.expirationTime)
        }
    }
    return null
}
