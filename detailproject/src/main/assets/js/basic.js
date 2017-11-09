

// 预览图片
function imageBrowse(index) {
    zjxw.imageBrowse(index);
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
        alert("replaceImage111111");
        var img = document.getElementsByTagName("img");
        for(var i = 0; i < img.length; i++) {
        if(i == index){
        alert("replaceImage222222");
            var a = document.getElementsByTagName("img")[i];
            a.src=url;
        }

        }
   }