app.controller("indexController",function (loginService,$scope) {
    $scope.showName=function(){
        loginService.showName().success(
              function (response) {
                    $scope.loginName=response.loginName;
              }
        )
    }
});