package contracts.orderService

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'PUT'
        url $(regex('/[0-9]{5}'))
    }
    response {
        status 200
        body([
                id        : fromRequest().path(0),
                status    : 'DONE',
                productIds: ['1', '4'],
                customerId: '1',
                accountId : '1',
                price     : 1000
        ])
        headers {
            contentType(applicationJson())
        }
    }
}