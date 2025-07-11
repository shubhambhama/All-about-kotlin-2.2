package org.kotlin.nestedtypealiases

class ApiClient {
    typealias HttpHeaders = Map<String, String>
    typealias HttpResponse = Triple<Int, HttpHeaders, String>
    typealias RequestInterceptor = (String, HttpHeaders) -> HttpHeaders
    typealias ResponseTransformer = (HttpResponse) -> Any?

    private val interceptors = mutableListOf<RequestInterceptor>()
    private val transformers = mutableMapOf<String, ResponseTransformer>()

    fun addInterceptor(interceptor: RequestInterceptor) {
        interceptors.add(interceptor)
    }

    fun addTransformer(contentType: String, transformer: ResponseTransformer) {
        transformers[contentType] = transformer
    }

    fun makeRequest(url: String, headers: HttpHeaders = emptyMap()): HttpResponse {
        var finalHeaders = headers
        interceptors.forEach { interceptor ->
            finalHeaders = interceptor(url, finalHeaders)
        }

        val statusCode = if (url.contains("error")) 500 else 200
        val responseHeaders: HttpHeaders = mapOf(
            "Content-Type" to "application/json",
            "Server" to "Kotlin-Server/2.2.0"
        )
        val body = if (statusCode == 200) {
            """{"message": "Success", "url": "$url"}"""
        } else {
            """{"error": "Internal Server Error"}"""
        }

        return HttpResponse(statusCode, responseHeaders, body)
    }

    fun processResponse(response: HttpResponse): Any? {
        val (statusCode, headers, body) = response
        val contentType = headers["Content-Type"] ?: "text/plain"

        val transformer = transformers[contentType]
        return transformer?.invoke(response) ?: body
    }
}