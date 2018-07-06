/**
 * 这个js放在要做逻辑js的下方！ Created by wx on 2016/11/11.
 */
document.write("<script src='../js/basejs.js?ts=" + new Date().getTime()
		+ "' ></script>");
document.write("<script src='../js/jquery-3.3.1.slim.min.js?ts=" + new Date().getTime()
		+ "' ></script>");
document.write("<script src='../js/vue.js?ts=" + new Date().getTime()
		+ "' ></script>");
document.write("<script src='../js/header.js?ts=" + new Date().getTime()
		+ "' ></script>");
document.write("<script src='../js/server_api.js?ts=" + new Date().getTime()
		+ "' ></script>");


String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");
}
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");
}

/**
 * yyyy-MM-dd HH:mm:ss
 */
function strToDate(str) {
	str = str.replace(/-/g, "/");
	var date = new Date(str);
	return date;
}

/**
 * 日期的加减法，传入正负的数值即可
 * 
 * @param date
 * @param days
 * @returns {String}
 */
function addDate(date, days) {
	var d = new Date(date);
	d.setDate(d.getDate() + days);
	var month = d.getMonth() + 1;
	var day = d.getDate();
	if (month < 10) {
		month = "0" + month;
	}
	if (day < 10) {
		day = "0" + day;
	}
	var val = d.getFullYear() + "" + month + "" + day;
	return val;
}

// /*
// * 方法:Array.remove(dx) 功能:根据元素值删除数组元素. 参数:元素值 返回:在原数组上修改数组 作者：pxp
// */
// Array.prototype.indexOf = function(val) {
// for (var i = 0; i < this.length; i++) {
// if (this[i] == val) {
// return i;
// }
// }
// return -1;
// };
// Array.prototype.removevalue = function(val) {
// var index = this.indexOf(val);
// if (index > -1) {
// this.splice(index, 1);
// }
// };
//
// /*
// * 方法:Array.remove(dx) 功能:根据元素位置值删除数组元素. 参数:元素值 返回:在原数组上修改数组 作者：pxp
// */
// Array.prototype.remove = function(dx) {
// if (isNaN(dx) || dx > this.length) {
// return false;
// }
// for (var i = 0, n = 0; i < this.length; i++) {
// if (this[i] != this[dx]) {
// this[n++] = this[i];
// }
// }
// this.length -= 1;
// };

/**
 * 使用方法：_cyRegBox.regName.test(_regName)返回false就是不正确
 */
var _cyRegBox = {
	regEmail : /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,// 邮箱
	regName : /^[a-zA-Z][a-zA-Z0-9_-]{0,30}$/,// 用户名
	regMobile : /^0?1[3|4|5|8][0-9]\d{8}$/,// 手机
	regTel : /^0[\d]{2,3}-[\d]{7,8}$/
};

/**
 * 过滤特殊字符
 * 
 * @returns {Boolean}
 */
String.prototype.testUnexpectChar = function() {
	var pattern = new RegExp(
			"[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？%+_ ]");
	var specialStr = "";
	for (var i = 0; i < this.length; i++) {
		specialStr += this.substr(i, 1).replace(pattern, '');
	}
	if (this == specialStr) {// 没有特殊字符
		return true;
	} else {
		return false;
	}
}

function _isContentChinese(str) {
	var reg = /^[u4E00-u9FA5]+$/;
	var flag = false;
	for (var i = 0; i < str.length; i++) {
		var c = str.charAt(i);
		if (reg.test(c) || c == " " || c == "" || c == "'" || c == "$"
				|| ("\\" + c) == "\\\"" || c == "," || c == "." || c == "<"
				|| c == ">" || c == ";" || c == "(" || c == ")" || c == "!"
				|| c == "?" || c == "[" || c == "]" || c == "%" || c == "@"
				|| c == "&" || c == "u" || c == "v" || c == "w" || c == "x"
				|| c == "y" || c == "z") {
			// return false;
			continue;
		} else {
			alert("错误字符:" + c + ".");
			return true;
		}
	}
	return flag;
}

/**
 * 替换引号
 * 
 * @param str
 * @returns
 */
function replaceQuotes(str) {
	return str.replace(/'/g, "\\\'").replace(/"/g, "\\\"").replace(/“/g, "\\“")
			.replace(/”/g, "\\”");
}

// function _$_CheckChinese(obj, val) {
// var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
// if (reg.test(val)) {
// alert("不能输入汉字！");
// var strObj = document.getElementById(obj);
// strObj.value = "";
// strObj.focus();
// }
// }

function _$_CheckChinese(val) {
	var reg = /^[\u4E00-\u9FA5]+$/;
	if (!reg.test(val)) {
		alert("不是中文");
		return false;
	}
	return true;
}

function _$_CheckIlegallChar(val) {
	for (var i = 0; i < val.length; i++) {
		var c = val.charAt(i);
		if (c == "'" || c == "$" || ("\\" + c) == "\\\"" || c == ","
				|| c == "." || c == "<" || c == ">" || c == ";" || c == "("
				|| c == ")" || c == "!" || c == "?" || c == "[" || c == "]"
				|| c == "%" || c == "@" || c == "&" || c == "{" || c == "}"
				|| c == "+" || c == "-" || c == "#" || c == "/" || c == "*"
				|| c == "~" || c == "。" || c == "^" || c == "~" || c == "！"
				|| c == "@" || c == "#" || c == "￥" || c == "%" || c == "……"
				|| c == "&" || c == "*" || c == "（" || c == "）" || c == "——"
				|| c == "+" || c == "<" || c == ">" || c == "?" || c == ":"
				|| c == "`" || c == "+") {
			return true;
		}
	}
	return false;

}

//
// document.write("<script src='./cyans/js/jquery.min.js?ts=" + new
// Date().getTime() + "' ></script>");
// last
//

/*
 * document.write("<script src='./cyans/js/alarm.js?ts=" + new Date().getTime() + "' ></script>");
 
document.write("<script src='./cyans/js/cyans_load.js?ts="
		+ new Date().getTime() + "' ></script>");*/
//
// if (e && e.stopPropagation) {// 阻止非IE事件冒泡
// e.stopPropagation();
// } else if (window.event) {// 阻止IE事件冒泡
// window.event.cancelBubble = true;
// }
//
// var a = _$.mknd("a");
// a.id = "org_folder," + bean.orgId;
// a.href = "javascript:void(0)";
// a.onclick = (function (elmt) {
// return function (e) {
// if (e && e.stopPropagation) { //阻止非IE事件冒泡
// e.stopPropagation();
// } else if (window.event) { //阻止IE事件冒泡
// window.event.cancelBubble = true;
// }
// userClick(elmt);
// }
// })(a);
//
