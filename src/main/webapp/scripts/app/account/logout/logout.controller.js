'use strict';

angular.module('jhip3App')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
