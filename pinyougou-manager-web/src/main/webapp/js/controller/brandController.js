//定义控制器
app.controller('brandController', function ($scope,$controller, brandService) {
    $controller('baseController',{$scope:$scope});//继承
    //分页
    $scope.findPage = function (page, rows) {
        brandService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        );
    }


    //增加
    $scope.save = function () {
        var object = null;
        if ($scope.entity.id != null) {
            object = 'update';
        }else{
            object="add";
        }
        brandService.object($scope.entity).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message)
                }
            }
        );
    }
    //修改的查询实体
    $scope.findOne = function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }




    //删除品牌

    $scope.del = function () {
        brandService.del($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();
                } else {
                    alert(response.message);
                }
            }
        );
    }
    $scope.searchEntity={};
    //$scope.sEntity={};
    //条件查询品牌findE
    $scope.search = function (page,rows) {
        brandService.search(page,rows,$scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;
            }
        )
    }
});