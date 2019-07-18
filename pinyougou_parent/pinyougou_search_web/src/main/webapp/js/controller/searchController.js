app.controller("searchController",function ($scope,searchService,$location) {


    $scope.searchMap={'keywords':'','category':'', 'brand':'','spec':{},'price':'','pageNo':'1','pageSize':'40','sort':'','sortFiled':''};
	$scope.search=function () {
        $scope.searchMap.pageNo=parseInt( $scope.searchMap.pageNo);
        $scope.searchMap.pageSize=parseInt( $scope.searchMap.pageSize);
		searchService.search($scope.searchMap).success(
			function (response) {
				$scope.resultMap=response;
                buildPageLabel();
			}

		)
	}
	$scope.addSearchItem=function (key,value) {
          if (key=='category'||key=='brand'||key=='price'){
              $scope.searchMap[key]=value;
          }else {
              $scope.searchMap.spec[key]=value;
          }
        $scope.search();
    }
    $scope.removeSearchItem=function (key) {
        if (key=='category'||key=='brand'||key=='price'){
            $scope.searchMap[key]='';
        }else {
             delete $scope.searchMap.spec[key];
        }
        $scope.search();
    }
    buildPageLabel=function () {
          $scope.pageLabel=[];
         var firstPage=1;
         var lastPage=$scope.resultMap.totalPage;
       $scope.ifHasFirstDot=true;
       $scope.ifHasLastDot=true;

         if ($scope.resultMap.totalPage>5){
             if ($scope.searchMap.pageNo<=3){
                 lastPage=5;
                 $scope.ifHasFirstDot=false;
             }else if ($scope.searchMap.pageNo>=$scope.resultMap.totalPage-2) {
                 firstPage=$scope.resultMap.totalPage-4;
                 $scope.ifHasLastDot=false;
             }else {
                 firstPage=$scope.searchMap.pageNo-2;
                 lastPage=$scope.searchMap.pageNo+2;
             }
         }else {
             $scope.ifHasFirstDot=false;
             $scope.ifHasLastDot=false;
         }
        for (var i = firstPage; i <=lastPage ; i++) {
            $scope.pageLabel.push(i);
        }

    }
    $scope.findByPage=function (pageNo) {
           if (pageNo<1||pageNo>$scope.resultMap.totalPage){
               return ;
           }

           $scope.searchMap.pageNo=pageNo;
          $scope.search();
    }
    // $scope.isTopPage=function () {
    //     if ($scope.searchMap.pageNo==1){
    //         return true;
    //     }else {
    //         return false;
    //     }
    // };
    // $scope.isLastPage=function () {
    //     if ($scope.searchMap.pageNo==$scope.resultMap.totalPage) {
    //         return true
    //     }else {
    //         return false;
    //     }
    // }
    
    $scope.sortSearch=function (sort,sortFiled) {
        $scope.searchMap.sort=sort;
        $scope.searchMap.sortFiled=sortFiled;
        $scope.search();
    }
    // $scope.isActive=function (pageNo) {
    //   return $scope.searchMap.pageNo==pageNo;
    //
    // }
    $scope.keyWordsIsBrand=function () {
        for (var i = 0; i <$scope.resultMap.brandList.length ; i++) {
                 if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>0){
                        return true;
            }
        }
        return false;
    }

    $scope.loadKeywords=function () {

	    $scope.searchMap.keywords=$location.search()['keywords'];
	    $scope.search();
    }


});



