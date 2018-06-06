

// 预览图片
function imageBrowse(index) {
    zjxw.imageBrowse(index);
}

// 预览超链接图片
function imageABrowse(index) {
    zjxw.imageABrowse(index);
}

// 执行夜间模式
function applyNightTheme() {
    var ui_mode_link = document.getElementById("ui_mode_link");
    ui_mode_link.setAttribute("href", "file:///android_asset/css/night.css");
}

// 执行白间模式
function applyDayTheme() {
    var ui_mode_link = document.getElementById("ui_mode_link");
    ui_mode_link.setAttribute("href", "file:///android_asset/css/day.css");
}

//替换webview中的图片
 function replaceImage(index,url){
       var imgs = document.getElementsByTagName("img"),arr=[];
       for(var i = 0; i < imgs.length; i++) {
         if (imgs[i].parentNode.tagName !== 'A') {
            arr.push(imgs[i]);
         }
        }
        if(arr.length > 0){
            var a = arr[index];
            a.setAttribute("src",url);
        }
  }

//替换webview中的超链接图片
 function replaceAImage(index,url){
       var imgs = document.getElementsByTagName("img"),arr=[];
       for(var i = 0; i < imgs.length; i++) {
         if (imgs[i].parentNode.tagName === 'A') {
            arr.push(imgs[i]);
         }
        }
        if(arr.length > 0){
            var a = arr[index];
            a.setAttribute("src",url);
        }
  }

 function musicPause(){
    var audio = document.getElementsByTagName("audio")
    for(var i = 0; i < audio.length; i++) {
        var a = document.getElementsByTagName("audio")[i];
        a.pause();
    }

 }
