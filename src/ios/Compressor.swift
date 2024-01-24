import Foundation;


class Compressor:CordovaPlugin {

    @objc(compressImage:)
    func compressImage(command:CDVInvokedUrlCommand){
        let options=command.arguments[0] as? [String:Any] ?? [:];
        if var path=options["path"] as? String {
            let quality=(options["quality"] as? Double ?? 50 )/100;
            let overwrite=options["overwrite"] as? Bool ?? false;
            if let image=UIImage(contentsOfFile:path),
                let data=image.jpegData(compressionQuality:quality){
                var fileURL:URL?;
                if(overwrite){
                    if(!path.starts(with:"file:///")){path="file:///"+path};
                    fileURL=URL(string:path);
                }
                else if let cacheURL=FileManager.default.urls(
                    for:.cachesDirectory,
                    in:.userDomainMask
                ).first {
                    fileURL=cacheURL.appendingPathComponent("image.jpeg");
                    
                }
                do{
                    if(fileURL != nil){
                        try data.write(to:fileURL!);
                    }
                    self.success(command,[
                        "path":fileURL == nil ? "" : fileURL!.absoluteString,
                        "base64":"data:image/jpeg;base64,"+data.base64EncodedString(),
                    ]);
                }
                catch{
                    self.error(command,["message":error.localizedDescription]);
                }
            }
            else{
                self.error(command,["message":"could not compress image at path"]);
            }
        }
        else{
            self.error(command,["message":"image path is required"]);
        }
    }

}
