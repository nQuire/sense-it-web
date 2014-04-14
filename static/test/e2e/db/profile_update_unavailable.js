exports.module = function () {
    angular.module('e2eDB', []).factory('e2eDbRequests', [function () {
        return {
            requests: {
                'api/security/status': {
                    logged: true,
                    profile: {id: 1, username: 'me', authorities: []},
                    token: 'tkn'
                },
                'PUT api/security/profile': {
                    logged: true,
                    profile: {id: 1, username: 'me', authorities: []},
                    connections: {},
                    token: 'tkn',
                    responses: {'username': 'username_not_available'}
                }
            }
        };
    }]);
};
