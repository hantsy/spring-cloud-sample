import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    request {
        method HttpMethods.HttpMethod.GET
        url "/posts/1"
    }
    response {
        body(
            [
                id     : "1",
                title  : "My first post",
                content: "Content of my first post"
            ]
        )
        status(200)
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}