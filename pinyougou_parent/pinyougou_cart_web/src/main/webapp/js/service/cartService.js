app.service('cartService',function($http){
    //购物车列表
    this.findCartList=function(){
        return $http.get('cart/findCartList.do');
    }

    //添加商品到购物车
    this.addGoodsToCartList=function(itemId,num){
        return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
    }
    //合计
    this.sum=function (carList) {
          var totalValue={totalNum:0,totalMoney:0};
          for(var i=0;i<carList.length;i++){
                    var cart=carList[i];
              for (var j = 0; j <cart.orderItemList.length ; j++) {
                       var orderItem=cart.orderItemList[j];
                       totalValue.totalNum+=orderItem.num;
                       totalValue.totalMoney+=orderItem.totalFee;
              }
          }
          return totalValue;
    }

    this.showName=function () {
        return $http.get("cart/name.do");
    }

    this.findAddressList=function () {
          return $http.get("address/findListByLoginUser.do");
    }
    this.submitOrder=function (order) {
           return $http.post("order/add.do",order)
    }

});