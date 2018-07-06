//如果是span等直接加入文本的控件,而且请把需要些文本的控件加入textview,默认的文本大小1rem,请不要用像素做单位；
///请加上display:block或者inline-block，不然没办法,有时候设置inline-block横向的居中等不生效

//建议定位置用div，写文本的时候再在里面用span

//using class resizable 用这个属性 style的width和height会根据网页显示的大小重新计算
//父控件宽高确定的情况下,设置了resizable前提下,如果再加上match_parent_w ,match_parent_h 则会跟父控件一样大，当然也可以分开用

//center_h 在父控件中横向居中
//left_h   在父控件中横向居左
//right_h  在父控件中横向居右

//center_v 在父控件中纵向居中
//bottom_v 在父控件中纵向居下
//top_v    在父控件中纵向居上

//gravity_h span文本横向居中
//gravity_h_l 文本横向居左
//gravity_h_r 文本横向居右 

//gravity_v span文本纵向居中
//gravity_v_t span文本纵向居上
//gravity_v_b span文本纵向居下
(function() {
	var sw, sh;
	var baseFontSize;

	function CyanLayout() {

	}

	//我们这个类的功能是让控件的大小是按照一个cyanroot进行计算,cyanroot要成为一个宽高比16:9的矩形(已高为准)，而不是传统css的方式
	CyanLayout.prototype.relayout = function() {
		sw = (document.documentElement.clientWidth || document.body.clientWidth)
		sh = (document.documentElement.clientHeight || document.body.clientHeight)

		var root = document.getElementsByClassName("cyanroot")[0]
		root.className += " center_h center_v"
		sw = 16.0 / 9.0 * sh; //已高为标准按照16:9重新计算宽度
		//sw=w;
		root.style.width = sw + "px";
		root.style.height = sh + "px";

		//
		/*
		if (sw > document.body.offsetWidth) {
			sw = document.body.offsetWidth;
		}
		*/
		console.log("w=" + sw + ",h=" + sh);
		/*
		var s = "";
		s += " 网页可见区域宽："+  (document.documentElement.clientWidth || document.body.clientWidth);
		s += " 网页可见区域高："+  (document.documentElement.clientHeight || document.body.clientHeight);
		s += " 网页可见区域宽："+ document.body.offsetWidth +" (包括边线和滚动条的宽)";
		s += " 网页可见区域高："+ document.body.offsetHeight +" (包括边线的宽)";
		s += " 网页正文全文宽："+ document.body.scrollWidth;
		s += " 网页正文全文高："+ document.body.scrollHeight;
		s += " 网页被卷去的高："+ document.body.scrollTop;
		s += " 网页被卷去的左："+ document.body.scrollLeft;
		s += " 网页正文部分上："+ window.screenTop;
		s += " 网页正文部分左："+ window.screenLeft;
		s += " 屏幕分辨率的高："+ window.screen.height;
		s += " 屏幕分辨率的宽："+ window.screen.width;
		s += " 屏幕可用工作区高度："+ window.screen.availHeight;
		s += " 屏幕可用工作区宽度："+ window.screen.availWidth;
		s += " 你的屏幕设置是 "+ window.screen.colorDepth +" 位彩色";
		s += " 你的屏幕设置 "+ window.screen.deviceXDPI +" 像素/英寸";
		console.log(s)
		 */
		//修改布局大小
		var el = document.getElementsByClassName("resizable");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			var w = parseFloat(e.style.width.replace("%", "")) / 100 * sw;
			var h = parseFloat(e.style.height.replace("%", "")) / 100 * sh;
			//计算出控件应该的宽高
			console.log(e.style.width + " | " + e.style.height)
			e.style.width = w + "px";
			e.style.height = h + "px";

			if(e.className.indexOf("match_parent_h") > -1) {
				e.style.height = e.parentNode.style.height;
			}
			if(e.className.indexOf("match_parent_w") > -1) {
				e.style.width = e.parentNode.style.width;
			}
			//如果有边距就设置边距
			if(e.style.marginLeft.indexOf("%") > -1) {
				var v = parseFloat(e.style.marginLeft.replace("%", ""))
				e.style.marginLeft = (v / 100 * sw) + "px"
			}
			if(e.style.marginRight.indexOf("%") > -1) {
				var v = parseFloat(e.style.marginRight.replace("%", ""))
				e.style.marginRight = (v / 100 * sw) + "px"
			}
			if(e.style.marginBottom.indexOf("%") > -1) {
				var v = parseFloat(e.style.marginBottom.replace("%", ""))
				e.style.marginBottom = (v / 100 * sh) + "px"
			}
			if(e.style.marginTop.indexOf("%") > -1) {
				var v = parseFloat(e.style.marginTop.replace("%", ""))
				e.style.marginTop = (v / 100 * sh) + "px"
			}
			
			if(e.style.paddingLeft.indexOf("%") > -1) {
				var v = parseFloat(e.style.paddingLeft.replace("%", ""))
				e.style.paddingLeft = (v / 100 * sw) + "px"
			}
			if(e.style.paddingRight.indexOf("%") > -1) {
				var v = parseFloat(e.style.paddingRight.replace("%", ""))
				e.style.paddingRight = (v / 100 * sw) + "px"
			}
			if(e.style.paddingBottom.indexOf("%") > -1) {
				var v = parseFloat(e.style.paddingBottom.replace("%", ""))
				e.style.paddingBottom = (v / 100 * sh) + "px"
			}
			if(e.style.paddingTop.indexOf("%") > -1) {
				var v = parseFloat(e.style.paddingTop.replace("%", ""))
				e.style.paddingTop = (v / 100 * sh) + "px"
			}
		}
		//对文本控件的文本赋值初始大小,1rem
		//1.计算出1rem的大小,
		baseFontSize = 10 * (sw / 640) + 'px';
		document.body.style.fontSize = baseFontSize; //这就是1rem;
		console.log("1rem=" + baseFontSize)
		//2 赋值
		el = document.getElementsByClassName("textview");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			if(e.style.fontSize == "") {
				console.log(e + "未设置fontsize，调整为1rem")
				e.style.fontSize = "1rem";
			}
		}
		//修改横向居中
		el = document.getElementsByClassName("center_h");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.marginTop = "0px";
			e.style.marginBottom = "0px";
			e.style.marginLeft = "auto";
			e.style.marginRight = "auto";
		}
		//修改横向居左
		el = document.getElementsByClassName("left_h");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.marginTop = "auto";
			e.style.marginBottom = "auto";
			e.style.marginLeft = "0px";
			e.style.marginRight = "auto";
		}
		//修改横向居右
		el = document.getElementsByClassName("right_h");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.marginTop = "auto";
			e.style.marginBottom = "auto";
			e.style.marginLeft = "auto";
			e.style.marginRight = "0px";
		}
		//修改纵向居中
		el = document.getElementsByClassName("center_v");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.position = "relative";
			var mt = (parseFloat(e.parentNode.style.height
				.replace("px", "")) - parseFloat(e.style.height
				.replace("px", ""))) / 2
			e.style.top = mt + "px"
		}
		//修改纵向居下
		el = document.getElementsByClassName("bottom_v");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.position = "relative";
			var mt = (parseFloat(e.parentNode.style.height
				.replace("px", "")) - parseFloat(e.style.height
				.replace("px", "")))
			e.style.top = mt + "px"
		}
		//修改纵向居上
		el = document.getElementsByClassName("top_v");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.position = "relative";
			//                    var mt = (parseFloat(e.parentNode.style.height
			//                        .replace("px", "")) - parseFloat(e.style.height
			//                        .replace("px", ""))) 
			e.style.top = "0px"
		}
		//处理span的文本位置,首先要是resizable textview的哦！而且大小单位为rem
		//横向居中
		el = document.getElementsByClassName("gravity_h");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.textAlign = "center";
		}
		//横向居左
		el = document.getElementsByClassName("gravity_h_l");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.textAlign = "left";
		}
		//横向居右
		el = document.getElementsByClassName("gravity_h_r");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.textAlign = "right";
		}
		//纵向居中
		el = document.getElementsByClassName("gravity_v");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.lineHeight = e.style.height;
		}
		//纵向居上
		el = document.getElementsByClassName("gravity_v_t");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.lineHeight = (0 + parseFloat(e.style.fontSize.replace("rem", "")) * parseFloat(baseFontSize.replace("px", "")) * 1.25) + "px";
		}
		//纵向居下
		el = document.getElementsByClassName("gravity_v_b");
		for(var i = 0; i < el.length; i++) {
			var e = el[i];
			e.style.lineHeight = ((parseFloat(e.style.height.replace("px", "")) * 2) - (parseFloat(e.style.fontSize.replace("rem", "")) * parseFloat(baseFontSize.replace("px", ""))) * 1.3) + "px"
		}
	}

	window.$cyanlayout = new CyanLayout();
})()

function initKeyEvent() {
	document.onkeydown = (function() {
		return function(e) {
			try {
				//页面实现这个方法就会调用
				if(onKeyClick) {
					onKeyClick(e.which)
				}
			} catch(e) {}
		}
	})();
}

if(document.all) {
	window.attachEvent("onload", initLoad)
} else {
	window.addEventListener("load", initLoad, false)
}

function initLoad() {
	$cyanlayout.relayout()
	initKeyEvent();
	//
	try {
		//页面实现这个方法就会调用
		if(onPageLoad) {
			onPageLoad()
		}
	} catch(e) {}
}
/*
//document.onkeypress = grabEvent;
//document.onsystemevent = grabEvent;
//document.onkeypress = grabEvent;
//document.onirkeypress = grabEvent;
//document.onkeydown = grabEvent;

function grabEvent() {
    alert(1+" "+event.which)
    var key_code = event.which;
    switch (key_code) {
        case 1: //up
        case 269:
            menuFocus(-1);
            return 0;
            break;
        case 2: //down
        case 270:
            menuFocus(1);

            return 0;
            break;
        case 3: //left
        case 271:
            menuFocus(-5);
            return 0;
            break;
        case 4: //right
        case 272:
            menuFocus(5);
            return 0;
            break;
        case 13:
            return 0;
            break;
        case 340: //back
        case 283:
            return 0;
            break;
        case 372:
            menuFocus(-5);
            return 0;
            break;
        case 373:
            menuFocus(5);
            return 0;
            break;
    }
}
function menuFocus(i) {
    alert(1)
}
*/