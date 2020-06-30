package contracts.customerService

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/withAccounts/1'
    }
    response {
        status 200
        body(
                id: $(regex('[0-9]{1}')),
                name: $(regex('[a-zA-Z0-9]{5}')),
                type: $(regex('REGULAR'))
        )
        headers {
            contentType(applicationJson())
        }
    }
}