app.controller("baseController",function ($scope) {
    //分页控件配置
    $scope.paginationConf = {
            currentPage: 1,
            totalItems: 10,
            itemsPerPage: 5,
            perPageOptions: [1, 2, 3, 4, 5],
            onChange: function () {
                $scope.reloadList();

            }
    };
    //抽取方法，以后其他的还可以引用
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage)

    };
    //选中的id
    $scope.selectIds=[];
    $scope.updateSelection=function ($event,id) {
        if ($event.target.checked){
            $scope.selectIds.push(id)
        } else {
            var index=   $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1)
        }
    };
    $scope.jsonToString=function (jsonString,key) {
        var json=JSON.parse(jsonString);
        var value="";
        for (var i = 0; i <json.length ; i++) {
            if (i>0){
                value+=",";
            }
           value+= json[i][key];
        }
        return value;

    }
    $scope.searchObjectKey=function (list,key,keyValue) {
        for (var i = 0; i <list.length ; i++) {
            if (list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }


})