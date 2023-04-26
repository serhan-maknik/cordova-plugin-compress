const exec=require("cordova/exec");


module.exports={
    setFile:(data,successCallback,errorCallback)=>{
        const obj={
            data:data
        };
        exec(successCallback,errorCallback,"ImageCompress","file",[obj]);
    },
    
}
