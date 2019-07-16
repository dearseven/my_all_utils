function getProblems() {
	if (_requestParam.type == "alarm") {
		$("#mTitle").html("报警");
	} else {
		$("#mTitle").html("故障");
	}

	var url = "/Hydrogen/mobile/getProblems.action";
	var param = "lang=1&equipmentId=" + _requestParam.uid
			+ "&instructionsType=" + (_requestParam.type == "alarm" ? 1 : 2);
	window.$helper._post(function(txt) {
		_do(txt);
	}, url, param);
}

function _do(txt) {
	var json = JSON.parse(txt);
	var html = "";
	for (var i = 0; i < json.list.length; i++) {
		var flag = json.list[i].instructionsValue;
		var name = json.list[i].instructionsDesc;
		var src, txtClass, txt;
		if (flag == 0) {
			src = "../images/green_point.png";
			txtClass = "cy_txt_green";
			txt = "正常";
		} else {
			if (_requestParam.type == "alarm") {
				src = "../images/orange_point.png";
				txtClass = "cy_txt_orange";
				txt = "报警";
			} else {
				src = "../images/red_point.png";
				txtClass = "cy_txt_red";
				txt = "故障";
			}
		}
		html+="<li class='list-group-item'><div class='row'>";
		html+="<div class='cy_col-2'>";
		html+="<img class='cy_list_item_status' src='"+src+"' />";
		html+="</div><div class='cy_col-15'>";
		html+="<span class='cy_list_item_txt'>"+name+"</span></div>";
		html+="<div class='cy_col-3'><span class='"+txtClass+"'>"+txt+"</span>";
		html+="</div></div></li>";
	}
	$("#mUl")[0].innerHTML+=html;
}

getProblems();