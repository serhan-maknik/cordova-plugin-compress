declare const Compressor:Compressor;


interface Compressor {
    compressImage(
        path:string,
        successCallback:()=>void,
        errorCallback:()=>void,
    ):void,
}
