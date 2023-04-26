# cordova-plugin-compress



## HTML

```html
<body>
    
        <div style="display: flex;flex-direction: column;" >
       
            <button onclick="getPhoto()">Picture</button>
            <img id="myImage" style="width: 120px; height: 120px;"></img>
        </div>
  
    <script src="cordova.js"></script>
    <script src="js/index.js"></script>

    <script>

        function getPhoto() {
            navigator.camera.getPicture(onSuccess, onFail, {
                quality: 100,
                destinationType: Camera.DestinationType.FILE_URI
            });

            function onSuccess(imageData) {
                ImageCompress.setFile(
                    {
                        imageFile : imageData, // it should be file path not base64
                        fSize : 70, // Orginal file size 
                        fQuality: 80, // Orginal quality    
                        base64Size: 70, // Base64 size
                        base64Quality:80  // Base64 quality
                    },
                function(imageData){
                   var image = document.getElementById('myImage');
                   image.src = "data:image/jpeg;base64," + imageData;
                },
                function(b){
                    console.log(b)
                });
            }

            function onFail(message) {
                alert('Failed because: ' + message);
            }

            
        }

    </script>
</body>

```
## javascript
```js
ImageCompress.setFile(
{
    imageFile : imageData,
    fSize : 70,
    fQuality: 80,
    base64Size: 70,
    base64Quality:80 
},

function(imageData){
    var image = document.getElementById('myImage');
    image.src = "data:image/jpeg;base64," + imageData;
},

function(error){
    console.log(error)
});
```
