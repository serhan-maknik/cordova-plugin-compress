# cordova-plugin-compress

## javascript

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

```js
ImageCompress.setFile(
{
    imageFile : imageData,
    fSize : 70,
    fQuality: 80,
    base64Size: 70,
    base64Quality:80 
},
function(base64){
var image = document.getElementById('myImage');
image.src = "data:image/jpeg;base64," + base64;
},
function(error){
console.log(error)
});
```
