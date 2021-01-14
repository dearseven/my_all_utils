function logout() {
	var url = "/Hydrogen/mobile/logout.action";
	window.$helper._post(function(txt) {
		var json = JSON.parse(txt);
		if (json.msgCode != "0") {
			alert(json.errMsg);
			return;
		}
		// alert("退出成功");
		window.location.href = "first.jsp";
	}, url, "");
}
function getList() {
	var url = "/Hydrogen/mobile/deviceList.action";
	var param = "lang=1";
	window.$helper
			._post(
					function(txt) {
						var json = JSON.parse(txt);
						if (json.msgCode != "0") {
							alert(json.errMsg);
							return;
						}
						console.log(json);
						//
						var listView = $("#listView")[0];
						var datas = json.equipment;
						var html = "";
						for (var i = 0; i < datas.length; i++) {
							html += "<li class='list-group-item'>";
							//处理点击，我是故意和下面的右侧状态分开写的2个if else
							if (datas[i].isOnline == 0) {
								html += "<a class='cy_list_item_a cy_pull-left' href='javascript:alert(\"设备离线，无法查看.\")'>"
										+ datas[i].equipmentName + "</a>";
							}else{
								html += "<a class='cy_list_item_a cy_pull-left' href='device_info.jsp?uid="
									+ datas[i].equipmentId
									+ "'>"
									+ datas[i].equipmentName + "</a>";
							}
							//处理右侧状态
							if (datas[i].isOnline == 0) {
								html += "<span class='cy_badge_off cy_pull-right'>?</span>";
							} else if (datas[i].isOnline == 1
									&& datas[i].redPoint == 1) {
								html += "<span class='cy_badge_danger cy_pull-right'>!</span>";
							} else {
								// 没有东西
							}
							html += "</li>";
						}
						listView.innerHTML += html;
					}, url, param);
}

getList();