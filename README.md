# cordova-plugin-compress

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
function(base64){
var image = document.getElementById('myImage');
image.src = "data:image/jpeg;base64," + base64;
},
function(b){
console.log(b)
});
```
