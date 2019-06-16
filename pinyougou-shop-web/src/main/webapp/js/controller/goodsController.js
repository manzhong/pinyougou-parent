 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,itemCatService,uploadService,goodsService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
		var id=$location.search().id;//获取参数值
		if(id==null){
			return;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//向富文本编辑框传入参数
				editor.html($scope.entity.goodsDesc.introduction)
                //显示图片信息
                $scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
				//显示扩展信息
                $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //显示规格信息
				$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems)

				//sku列表转换
				for(var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec=JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);
	}
	//根据选项名称和规格名称返回是否勾选
	$scope.checkAttributeValue=function (specName,optionName) {
        var items= $scope.entity.goodsDesc.specificationItems;
        var object= $scope.searchObjectByKey(items,'attributeName',specName);
        if(object==null){
        	return false;
		}else {
        	if(object.attributeValue.indexOf(optionName)>=0){
        		return true;
			}else {
        		return false;
			}
		}

    }
	
	//保存 
	$scope.save=function(){
        $scope.entity.goodsDesc.introduction=editor.html();

		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
                    //重新查询
                    alert(response.message);
                    //$scope.reloadList();//
                    $scope.entity={};
                    editor.html("");//清空富文本编辑器
					location.href="goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	/*//保存
	$scope.add=function () {
		$scope.entity.goodsDesc.introduction=editor.html();
		goodsService.add($scope.entity).success(
            function(response){
                if(response.success){
                    //重新查询
					alert(response.message);
                    //$scope.reloadList();//
					$scope.entity={};
					editor.html("");//清空富文本编辑器
                }else{
                    alert(response.message);
                }
            }
        )
    }*/
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
    //上传图片
	$scope.uploadFile=function () {
		uploadService.uploadFile().success(
			function (response) {
				if(response.success){
					$scope.image_entity.url=response.message;//设置文件地址
				}else{
					alert(response.message)
				}
            }
		);
    }

    //定义页面实体结构
	$scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};

	$scope.add_image_entity=function () {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //删除图片

	$scope.remove_image_entity=function (index) {
		$scope.entity.goodsDesc.itemImages.splice(index,1);
    }
    //一级下拉选框
	$scope.selectItemCat1List=function () {
		itemCatService.findByParentId(0).success(
			function (response) {
				$scope.itemCat1List=response;
            }
		)
    }
    //二级下拉选框
	$scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat2List=response;

            }
        )
    })
	//三级下拉选框
    $scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {
        itemCatService.findByParentId(newValue).success(
            function (response) {
                $scope.itemCat3List=response;
            }
        )
    })
	//三级之后读取模板ID
    $scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {
        itemCatService.findOne(newValue).success(
            function (response) {
                $scope.entity.goods.typeTemplateId=response.typeId;//更新模板ID
            }
        )
    })

	//模板选择之后读取品牌列表
    $scope.$watch('entity.goods.typeTemplateId',function (newValue,oldValue) {
        typeTemplateService.findOne(newValue).success(
            function (response) {
                $scope.typeTemplate=response;
                $scope.typeTemplate.brandIds=JSON.parse($scope.typeTemplate.brandIds);
                //扩展属性
                if($location.search()['id']==null){
                    $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.typeTemplate.customAttributeItems);
                }

            }
        )
		//获取规格选项
		typeTemplateService.findSpecList(newValue).success(
			function (response) {
				$scope.specList=response;
            }
		)
    })
	//保存规格选项
	$scope.saveSpecList=function (event,name,value) {
		var object=$scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
		if(object!=null){
			if(event.target.checked){
                object.attributeValue.push(value);
			}else {
                object.attributeValue.splice( object.attributeValue.indexOf(value),1);
                if(object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
			}
		}else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}
    }
    //深克隆 创建SKU列表
	$scope.createItemList=function () {
		debugger
		$scope.entity.itemList=[{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];
		var items=$scope.entity.goodsDesc.specificationItems;
		for(var i=0;i<items.length;i++){
			$scope.entity.itemList=addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
    }
    //添加列值
	addColumn=function (list,columnName,columnValues) {
		debugger
		var newList=[];//新的集合
		for(var i=0;i<list.length;i++){
			var oldRow=list[i];
			for(var j=0;j<columnValues.length;j++){
                var newRow=JSON.parse(JSON.stringify(oldRow));
                newRow.spec[columnName]=columnValues[j];
                newList.push(newRow);
			}
		}
		return newList;
    }
    //审核状态
    $scope.status=['未审核','已审核','审核未通过','关闭'];
	//分类列表 显示中文三级列表
	//定义数组接收三级列表
    $scope.itemCatList=[];
    //定义方法显示三级列表
	$scope.findItemCatList=function () {
		itemCatService.findAll().success(
			function (response) {
				for(var i=0;i<response.length;i++){
					$scope.itemCatList[response[i].id]=response[i].name;
				}
            }
		)
    }
});
