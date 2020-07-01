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
                type: 'REGULAR',
                accounts: [
                        [
                                id     : $(regex('[0-9]{1}')),
                                number : '123',
                                balance: 5000,
                        ], [
                                id     : $(regex('[0-9]{1}')),
                                number : '124',
                                balance: 5000,
                        ]
                ])
        headers {
            contentType(applicationJson())
        }
    }
}