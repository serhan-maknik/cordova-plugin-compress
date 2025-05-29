import Foundation;


class Compressor:VritraPlugin {

    @objc(compressImage:)
    func compressImage(command:CDVInvokedUrlCommand){
        let options=command.arguments[0] as? [String:Any] ?? [:];
        if var path=options["path"] as? String {
            if(path.starts(with:"file:///")){
                path=String(path[path.index(path.startIndex,offsetBy:7)...]);
            };
            let quality=(options["quality"] as? Double ?? 50 )/100;
            let overwrite=options["overwrite"] as? Bool ?? false;
            if let image=UIImage(contentsOfFile:path),
                let data=image.jpegData(compressionQuality:quality){
                var fileURL:URL?;
                if(overwrite){
                    fileURL=URL(string:"file:///"+path);
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
