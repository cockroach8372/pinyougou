app.controller("brandController", function ($scope, $controller,brandService) {
    $controller('baseController',{$scope:$scope});//继承
    $scope.findAll = function () {
        brandService.findAll().success(
            function (data) {
                $scope.list = data;
            }
        )
    };

    $scope.findPage = function (page, size) {
        brandService.findPage(page,size).success(
            function (data) {
                $scope.list = data.rows;
                $scope.paginationConf.totalItems = data.total;
            }
        );
    };


    $scope.save = function () {
        var object=null;
        if ($scope.entity.id) {
            object=brandService.update($scope.entity);
        } else {
            object=brandService.add($scope.entity);
        }
        object.success(
            function (data) {
                if (data.success) {
                    $scope.reloadList();
                } else {
                    alert(data.message);
                }
            }
        )
    };
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (data) {
                $scope.entity = data;
            }
        )
    };

    //删除方法
    $scope.dele=function () {
        if (confirm("你确定要删除么？")) {
            brandService.dele($scope.selectIds).success(
                function (data) {
                    if (data.success) {
                        $scope.reloadList();
                    }else {
                        alert(data.message);
                    }
                }
            )
        }else {
            $scope.reloadList();
        }
    };

    $scope.searchEntity={};
    //条件查询
    $scope.search=function(page,size){
        brandService.search(page,size,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;
            }
        );
    }
    //分页数据回显逻辑
    $scope.ischecked=function (id) {
        for (var i = 0; i <$scope.selectIds.length ; i++) {
            if ($scope.selectIds[i]==id){
                return true;
            }
        }
        return false;
    }


});
