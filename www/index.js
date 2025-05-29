const exec=require("cordova/exec");


module.exports={
    compressImage:(options)=>{
        const {onCompress,onFail}=options;
        delete options.onFail;
        delete options.onCompress;
        exec(onCompress,onFail,"Compressor","compressImage",[options]);
    },
}
