declare const Compressor:Compressor;


interface Compressor {
    compressImage(options:{
        path:string,
        /**
         * A number in range 0..100.
         * 
         * The compressed image quality. The lower the more the image is compressed.
         * @default 50
         */
        quality?:number,
        /**
         * If true, the original image is the one compressed
         * @default false
         */
        overwrite?:boolean,
        onCompress:(data:{
            /**
             * The compress image file path.
             */
            path:String,
            base64:String,
        })=>void,
        onFail:(error:Error)=>void,
    },
    ):void,
}
