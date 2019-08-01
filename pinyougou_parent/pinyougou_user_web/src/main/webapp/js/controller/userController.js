 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
     $scope.reg=function () {
     	if (!$scope.smscode){
     		alert("请输入验证码")
		}
		 if ($scope.entity.password!=$scope.password){
		 	alert("两次密码输入不一致，请重新输入");
			 return ;
		 }
		 userService.add($scope.entity,$scope.smscode).success(
		 	   function (response) {
				      alert(response.message)
			   }
		 )
	 }
	 $scope.sendCode=function () {
     	    if (!$scope.entity.phone){
     	    	alert("手机号不能为空");
     	    	return ;
			}
		   userService.sendCode($scope.entity.phone).success(
		   	   function (response) {
                    alert(response.message);
			   }
		   )
	 }


});	
