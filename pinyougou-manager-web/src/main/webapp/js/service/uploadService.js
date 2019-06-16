app.service('uploadService',function ($http) {
    //文件上传
    this.uploadFile=function () {
        var formData=new FormData();//代表表单数据 上传专用
        formData.append('file',file.files[0]);//file 文件上传矿的name
        return $http({
            method:'post',
            url:'../upload.do',
            data:formData,
            headers:{'Content-Type':undefined},
            transformRequest:angular.identity
        });
    }
});