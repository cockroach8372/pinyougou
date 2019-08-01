app.controller("payController",function (payService,$scope,$location) {
      $scope.createNative=function () {
            payService.createNative().success(
                 function (response) {
                        $scope.total_fee=(response.total_fee/100).toFixed(2);//金额
                        $scope.out_trade_no=response.out_trade_no;//订单号
                     var qr =new QRious({
                         element:document.getElementById('qrious'),
                         size:250,
                         level:'H',
                         value:response.code_url

                     });
                     queryPayStatus();
                 }
            )
      }
      queryPayStatus=function(){
          payService.queryPayStatus( $scope.out_trade_no).success(
              function (response) {
                     if (response.success){
                         location.href="paysuccess.html#?total_fee="+  $scope.total_fee;
                     }else {
                         if (response.message=='二维码超时'){
                             $scope.createNative();
                         }else {

                             location.href="payfail.html";
                         }
                     }
              }
          )
      };
    $scope.getMoney=function(){
        return $location.search()['total_fee'];
    }

});