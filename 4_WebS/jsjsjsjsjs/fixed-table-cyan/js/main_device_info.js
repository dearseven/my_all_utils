function getInfo() {
	var url = "/Hydrogen/mobile/deviceInfo.action";
	var param = "lang=1&equipmentId=" + _requestParam.uid;
	window.$helper._post(function(txt) {
		_do(txt);
	}, url, param);
}

function _do(txt) {
	var _html = "";
	var json = JSON.parse(txt);
	if (json.msgCode != "0") {
		alert(json.errMsg);
		return;
	}
	console.log(json);
	//
	var deviceName = $("#deviceName")[0];
	deviceName.innerHTML = json.equipmentName;
	// 市电消失（1市电消失，0市电来电）,也就是说1则是在运行，0是不在运行也就是待机
	var disappearFlag = json.disappearFlag;
	var runStatus = $("#runStatus")[0];
	if (disappearFlag == 1) {
		$("#cityPowerLabel").html("市电消失时间");
		$("#cityPowerValue").html(json.disappearTime);
		runStatus.innerHTML = "运行";
		$("#runStatus").removeClass("label-warning");
		$("#runStatus").addClass("label-success");
		$("#verticalStatusBar1")[0].src = "../images/GreenBar.png";
		$("#verticalStatusBar2")[0].src = "../images/RedBar.png";
	} else {
		$("#cityPowerLabel").html("市电来电时间");
		$("#cityPowerValue").html(json.disappearTime);
		runStatus.innerHTML = "待机";
		$("#runStatus").removeClass("label-success");
		$("#runStatus").addClass("label-warning");
		$("#verticalStatusBar1")[0].src = "../images/RedBar.png";
		$("#verticalStatusBar2")[0].src = "../images/GreenBar.png";
	}
	// 最后一次采集时间
	var lastCollectionTime = json.lastCollectionTime
	var lastGetDataTime = $("#lastGetDataTime")[0];
	lastGetDataTime.innerHTML = "最后采集时间:" + lastCollectionTime;
	// 故障警告标识
	var problemStatus = $("#problemStatus")[0];
	if (json.faultFlag == 1) {
		problemStatus.style.visibility="visible";
		problemStatus.innerHTML = "故障";
		$("#problemStatus").removeClass("label-warning");
		$("#problemStatus").addClass("label-danger");
	} else if (json.faultFlag == 0 && json.alarmFlag == 1) {
		problemStatus.style.visibility = "visible";
		problemStatus.innerHTML = "警告";
		$("#problemStatus").removeClass("label-fault");
		$("#problemStatus").addClass("label-warning");
	} else if (json.faultFlag == 1 && json.alarmFlag == 1) {
		problemStatus.style.visibility = "visible";
		problemStatus.innerHTML = "警告故障";
		$("#problemStatus").removeClass("label-fault");
		$("#problemStatus").addClass("label-fault");
	} else {
		//problemStatus.style.visibility = "hidden";
	}
	// 运行时间
	$("#deviceInterval").html(json.interval);
	// 显示氢气容量的进度
	var blv = $("#bottleListView");
	_html = "";
	var bottleList = json.bottleList;
	var step1 = json.hydrogenWarning.first;
	var step2 = json.hydrogenWarning.second;
	var step3 = json.hydrogenWarning.third;
	for (var i = 0; i < bottleList.length; i++) {
		var name = bottleList[i].name;
		var value = parseFloat(bottleList[i].value);
		var color = value < step1 ? "cy_bar_red"
				: value < step2 ? "cy_bar_orange"
						: value < step3 ? "cy_bar_blue" : "cy_bar_green";
		_html += "<li class='list-group-item cy_adjust_list_group_padding'>";
		_html += "<div class='cy_list_bar_item_div'>";
		_html += "<span class='cy_list_bar_item_txt_left'>" + name + "</span>";
		_html += "<span class='cy_list_bar_item_txt_right'>" + value
				+ "%</span></div>";
		_html += "<div class='cy_list_bar_item_div'>";
		_html += "<div class='progress'><div class='progress-bar "
				+ color
				+ "' role='progressbar'aria-valuenow='60' aria-valuemin='0' aria-valuemax='100'";
		_html += "style='width:" + value + "%;'><span class='sr-only'>" + value
				+ "%</span>";
		_html += "</div></div></div></li>";
	}
	blv.html(_html);
	// 中间部分,当前负载，预计运行时间，环境温度
	$("#mPower").html(json.power);
	$("#mTime").html(json.time + "小时");
	$("#mTemperature").html(json.temperature + "℃");
	// 燃料电池寿命
	$("#mLife").html(json.life + "小时");
	// 显示电池阀寿命的list
	var slv = $("#solenoidListView");
	_html = "";
	for (var i = 0; i < json.solenoidValveList.length; i++) {
		var name = json.solenoidValveList[i].name;
		var value = json.solenoidValveList[i].value;
		_html += "<li class='list-group-item cy_adjust_list_group_padding'>";
		_html += "<span class='cy_section5_txt1'>" + name + "</span>";
		_html += "<span class='cy_section5_txt2'>" + value + "</span></li>";
	}
	slv.html(_html);
	// 处理警报和故障在最下方的显示
	if (json.alarmFlag == 1) {
		$("#liAlarmSpan")[0].style.visibility = "visible";
		$("#liAlarmSpan").html("新");
	}
	if (json.faultFlag == 1) {
		$("#liFaultSpan")[0].style.visibility = "visible";
		$("#liFaultSpan").html("新");
	}
	$("#liAlarm")[0].onclick = (function(a) {
		return function(b) {
			window.location.href = a;
		}
	})("problems_list.jsp?uid=" + _requestParam.uid + "&type=alarm");

	$("#liFault")[0].onclick = (function(a) {
		return function(b) {
			window.location.href = a;
		}
	})("problems_list.jsp?uid=" + _requestParam.uid + "&type=fault");

}

getInfo();