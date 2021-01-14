function ___selectorPageClick(currpage) {
	__selectorPageClick(currpage);
}

/**
 * 生成一个翻页区块的代码，翻页区块的名字恒定为__page_selector_area_div<br/>
 * 回调函数为__selectorPageClick(pageNum)<br/>
 * 记得把这个代码放在要使用他的js的前面，然后在js里写上__selectorPageClick<br/> 先调用第一次调用init,给出总页数<br/>
 * init的时候会选中第一页<br>
 */
(function() {
	var currpage = 1;
	var maxpage = 0;
	var dis = 3;
	var needQueckPick = true;
	var needNextOrLastBtn = true;
	function PageSelector() {

	}

	PageSelector.prototype.init = function(totalPage, _dis, _needQueckPick,
			_needNextOrLastBtn) {
		maxpage = totalPage;
		needQueckPick = _needQueckPick;
		needNextOrLastBtn = _needNextOrLastBtn;
		dis = _dis;
		currpage=1;
		pageCreate(true);
	}

	PageSelector.prototype.pagePick = function(i) {
		currpage = i;
		pageCreate();
	}

	PageSelector.prototype.nextPage = function() {
		currpage = parseInt(currpage + "");
		maxpage = parseInt(maxpage + "");
		if (currpage == maxpage) {
			return;
		}
		if (currpage + 1 > maxpage) {
			currpage = maxpage;
		} else {
			currpage = currpage + 1;
		}
		pageCreate();
	}

	PageSelector.prototype.next10Page = function() {
		currpage = parseInt(currpage + "");
		maxpage = parseInt(maxpage + "");
		if (currpage == maxpage) {
			return;
		}
		if (currpage + 10 > maxpage) {
			currpage = maxpage;
		} else {
			currpage = currpage + 10;
		}
		pageCreate();
	}

	PageSelector.prototype.lastPage = function() {
		currpage = parseInt(currpage + "");
		maxpage = parseInt(maxpage + "");
		if (currpage - 1 < 1) {
			currpage = 1;
		} else {
			currpage = currpage - 1;
		}
		pageCreate();
	}

	PageSelector.prototype.last10Page = function() {
		currpage = parseInt(currpage + "");
		maxpage = parseInt(maxpage + "");
		if (currpage - 10 < 1) {
			currpage = 1;
		} else {
			currpage = currpage - 10;
		}
		pageCreate();
	}

	PageSelector.prototype.specificPage = function() {
		var inputValue = document
				.getElementById("cyan_pages_selector_go_page_input").value
				.trim();
		if (inputValue == "") {
			inputValue = ".";
		}
		if (isNaN(inputValue.trim()) == false) {
			inputValue = parseInt(inputValue);
			if (inputValue < 1 || inputValue > maxpage) {
				alert("输入页数超过范围");
				return;
			}
		} else {
			alert("请输入数字");
			return;
		}
		currpage = inputValue;
		pageCreate();
	}

	function pageCreate(isFromInit) {
		if (isFromInit == undefined) {
			isFromInit = false;
		}
		var start = 0, end = 0;
		if (parseInt(currpage) - dis < 1) {
			start = 1;
		} else {
			start = parseInt(currpage) - dis;
		}
	//	console.log(currpage);
		if (parseInt(currpage) + dis > parseInt(maxpage)) {
			end = parseInt(maxpage);
		} else {
			end = parseInt(currpage) + dis;
		}
		// console.log(start + " " + end);
		var pages = "<ul style='list-style: none;'>";
		if (needNextOrLastBtn) {
			pages += "<li onclick='$_ps.last10Page();' style='list-style: none;float: left;'><a class='btn' href='#' role='tab' data-toggle='tab'>«</a></li>";
			pages += "<li onclick='$_ps.lastPage();' style='list-style: none;float: left;'><a class='btn' href='#' role='tab' data-toggle='tab'>‹</a></li>";
		}
		for (var i = start; i <= end; i++) {
			if (i == currpage) {
				pages += "<li onclick='$_ps.pagePick("
						+ i
						+ ");' style=' list-style: none;float: left;'><a style='background-color:#5577aa;' class='btn' href='#' role='tab' data-toggle='tab'><b style='color:#aaffee'>"
						+ i + "</b></a></li>";
			} else {
				pages += "<li onclick='$_ps.pagePick("
						+ i
						+ ");' style='list-style: none;float: left;'><a class='btn' href='#' role='tab' data-toggle='tab'>"
						+ i + "</a></li>";

			}
		}
		if (needNextOrLastBtn) {
			pages += "<li onclick='$_ps.nextPage();' style='list-style: none;float: left;'><a class='btn' href='#' role='tab' data-toggle='tab'>›</a></li>";
			pages += "<li onclick='$_ps.next10Page();' style='list-style: none;float: left;'><a class='btn' href='#' role='tab' data-toggle='tab'>»</a></li>";
		}
		var html = "";
		// if (needQueckPick) {
		// // 生成直接跳转区
		// html = "<div style='color:#555588;'>&nbsp;跳转到第<input typy='text'
		// style='display:inline-block;
		// width:2em;'id='cyan_pages_selector_go_page_input'/>页，总页数<span>"
		// + maxpage
		// + "</span>&nbsp;<input type='button' style='display:inline-block;'
		// onclick='$_ps.specificPage();' value='跳转' ></div>";
		// }
		if (needQueckPick) {
			// 生成直接跳转区
			html = "<li  style='list-style: none;float: left;'>&nbsp;<input  class='form-control'  typy='text' style='display:inline-block; width:3em;'id='cyan_pages_selector_go_page_input'  /><span>"
					+ maxpage
					+ "页</span>&nbsp;<a class='btn' type='button' style='display:inline-block;' onclick='$_ps.specificPage();' value='跳转' >跳转</a></li></ul>";
		}
		pages += html;
		var ctn = document.getElementById("__page_selector_area_div");
		ctn.innerHTML = pages;
		if (isFromInit == false) {
			__selectorPageClick(currpage);
		}
	}
	window.$_ps = new PageSelector();
})();

/**
 * 里面这个注释掉这个方法要在具体调用的js里做才对
 */
(function() {
	// window.$_ps.init(10,3, true,true);
})()

// a.onclick = (function(p) {
// return function(e) {
// alert(p + " " + e);
// }
// })("a");
