//购物车控制层
app.controller('cartController',function($scope,cartService){
    //查询购物车列表
    $scope.findCartList=function(){
        cartService.findCartList().success(
            function(response){
                $scope.cartList=response;
                $scope.totalValue=cartService.sum($scope.cartList);
            }
        );
    };

    //数量加减
    $scope.addGoodsToCartList=function(itemId,num){
        cartService.addGoodsToCartList(itemId,num).success(
            function(response){
                if(response.success){//如果成功
                    $scope.findCartList();//刷新列表
                }else{
                    alert(response.message);
                }
            }
        );
    };


        $scope.showName=function(){
            cartService.showName().success(
                function (response) {
                    $scope.loginName=response.loginName;
                }
            )
        }
      $scope.findAddressList=function () {
             cartService.findAddressList().success(
                 function (response) {
                           $scope.addressList=response;
                     for (var i = 0; i <$scope.addressList.length ; i++) {
                         //默认选中
                            if ($scope.addressList[i].isDefault=='1'){
                                $scope.address=$scope.addressList[i];
                                break;
                            }
                     }
                 }
             )
      }
      $scope.selectAddress=function (address) {
            $scope.address=address;
      };
    $scope.isSelectedAddress=function (address) {
            if ($scope.address==address){
                return true;
            }
            return false;
    }
    //支付方式
    $scope.order={paymentType:'1'};
    $scope.selectPayType=function (type) {
          $scope.order.paymentType=type;
    }

    //提交
    $scope.submitOrder=function () {
        $scope.order.receiverAreaName=$scope.address.address;
        $scope.order.receiverMobile=$scope.address.mobile;
        $scope.order.receiver=$scope.address.contact;
         cartService.submitOrder($scope.order).success(
              function (response) {
                  if (response.success){
                      if ($scope.order.paymentType=='1'){
                          location.href='pay.html';
                      }else {
                          location.href='paysuccess.html';
                      }
                  } else {

                      alert(response.message)
                  }

              }
         )
    }


});