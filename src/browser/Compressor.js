

module.exports={
    compressImage:(options)=>{
        const {onCompress,onFail}=options;
        console.log("cordova-plugin-compress does not support the browser platform.");
        onCompress&&onCompress("data:image/jpeg;base64,");
    },
}

