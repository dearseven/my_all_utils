function login() {
	var un = $("#un")[0].value.trim();
	var up = $("#up")[0].value.trim();

	var url = "/Hydrogen/mobile/userLogin.action";
	var param = "userName=" + un + "&password=" + up + "&lang=1";
	window.$helper._post(function(txt) {
		var json=JSON.parse(txt);
		if(json.msgCode=="0"){
			//进入设备列表
			window.location.href="device.jsp";
		}else{
			alert(json.errMsg);
		}
	}, url, param);
}