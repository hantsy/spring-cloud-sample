import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    request {
        method HttpMethods.HttpMethod.GET
        url "/posts"
    }
    response {
        body(
            """
            [ 
            {"id": "1", "title" :"My first post", "content": "Content of my first post" },
            { "id":"2", "title" : "My second post", "content": "Content of my second post" }
            ] 
        """
        )
        status(200)
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}