//控制层
app.controller('seckillGoodsController' ,function($scope,$location,seckillGoodsService,$interval){
	//读取列表数据绑定到表单中
	$scope.findList=function(){
		seckillGoodsService.findList().success(
			function(response){
				$scope.list=response;
			}
		);
	}

	//查询商品
    $scope.findOne=function(){
		seckillGoodsService.findOne($location.search()['id']).success(
			 function (response) {
				   $scope.entity=response;
				   //定时
				 allsecond=Math.floor((new Date($scope.entity.endTime).getTime()-new Date().getTime())/1000);
				 time=$interval(function () {
					 allsecond=allsecond-1;
					 $scope.timeString= convertTimeString(allsecond);
					 if ($scope.second<=0) {
						 $interval.cancel(time);
					 }
				 },1000);

			 }
		)
	};
	
	
	//转换秒为   天小时分钟秒格式  XXX天 10:22:33
    convertTimeString=function(allsecond){
    	var days=Math.floor(allsecond/(60*60*24))//天数
		var hours=Math.floor((allsecond-days*24*60*60)/60/60);//小时数
		var minutes=Math.floor((allsecond-hours*60*60-days*60*60*24)/60);//分钟
		var seconds=allsecond-hours*60*60-days*60*60*24-minutes*60	;//miao
		var timeString="";
		if (days>0){
			timeString=days+"天";
		}
		return timeString+hours+":"+minutes+":"+seconds;
	}


	//提交订单 
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder( $scope.entity.id ).success(
			function(response){
				if(response.success){//如果下单成功
					alert("抢购成功，请在5分钟之内完成支付");
					location.href="pay.html";//跳转到支付页面				
				}else if (response.message=="当前用户未登录"){
					alert(response.message);
                        location.href="http://localhost:9100/cas/login"
				}
				else{
					alert(response.message);
				}				
			}
		);
		
	}
	
});