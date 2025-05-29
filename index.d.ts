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
             * If overwrite is false, it points to the compressed image file path 
             * else it's the same as the path option.
             */
            path:String,
            base64:String,
        })=>void,
        onFail?:(error:Error)=>void,
    },
    ):void,
}
