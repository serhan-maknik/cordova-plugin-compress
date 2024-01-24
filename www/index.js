const exec=require("cordova/exec");


module.exports={
    compressImage:(options)=>{
        const {onCompress,onFail}=options;
        exec(onCompress,onFail,"Compressor","compressImage",[options]);
    },
}
