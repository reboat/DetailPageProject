

// 预览图片
function imageBrowse(index) {
    zjxw.imageBrowse(index);
}

// 执行夜间模式
function applyNightTheme() {
//zjxw.applyNightTheme(css);
    var ui_mode_link = document.getElementById("ui_mode_link");
    ui_mode_link.setAttribute("href", "file:///android_asset/css/night.css");
}

// 执行白间模式
function applyDayTheme() {
//zjxw.applyNightTheme();
    var ui_mode_link = document.getElementById("ui_mode_link");
    ui_mode_link.setAttribute("href", "file:///android_asset/css/day.css");
}