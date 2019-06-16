app.controller('baseController', function ($scope) {
    //刷新
    $scope.reloadList = function () {
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }
    //分页
    $scope.paginationConf = {
        currentPage: 1,//当前页面
        totalItems: 10,//总条数
        itemsPerPage: 10,//每页项目
        perPageOptions: [10, 20, 30, 40, 50],//页码选项
        onChange: function () {//更该页面时执行
            $scope.reloadList();
        }
    }
    //定义存复选框选中的id
    $scope.selectIds = [];
    //定义方法吧id存入selectIds
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            $scope.selectIds.push(id);
        } else {
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index, 1);//1移除的位置  2移除的个数
        }

    }
    //获取json字符串某个属性 放回拼接字符串并用逗号隔开
    $scope.jsonToString = function (jsonString, key) {
        var json = JSON.parse(jsonString);//将json字符串转为json对象
        var value = "";
        for (var i = 0; i < json.length; i++) {
            if (i > 0) {
                value += ",";
            }
            value += json[i][key];

        }
        return value;
    }
    //判断对象中的某个属性是否存在
    $scope.searchObjectByKey=function (list,key,keyValue) {
        for(var  i=0;i<list.length;i++){
            if(list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }

});