import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

org.springframework.cloud.contract.spec.Contract.make {
    description("""
        Get notification by id
    """)
    // input to the contract
    request {
        url "/notifications/1"
        method GET()
        headers {
            header("accept", regex("application/.*json.*"))
        }
    }
    response {
        status(200)
        body([
            body: "test message",
            type: "MESSAGE"
        ])
        headers {
            header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        }
    }
}

