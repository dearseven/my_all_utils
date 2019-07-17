/**
 * 找个其实是可滑动表格的css
 */

var win = $(window),
	scrollAreaEl = $('.t_r_content'),
	leftFreezeEl = $('.t_l_freeze'),
	leftTableEl = leftFreezeEl.find('table'),
	rightTableEl = $('.t_r_t table');

//**cyan动态计算容器最大高度 这里有几个要注意的地方，
//1 因为不好计算表格的高度，所以高度是总高度-其他区域的高度和=表格的显示框高度（表格可能大于这个值）
//2 tableHeight = winHeight - contentHeight- 60;这个-60是内容格子的高度
function adjustHeight() {
	var winHeight = win.height();
		 //tableHeight = winHeight - 90;

	var totalHeight = 0;
	var contentHeight = 0;
	var box = $("#area_1")[0]
	contentHeight += parseFloat(window.getComputedStyle(box).height.replace("px", ""));
	//console.log("contentHeight 1：" + contentHeight)

	box = $("#area_2")[0]
	contentHeight += parseFloat(window.getComputedStyle(box).height.replace("px", ""));
	//console.log("contentHeight 2：" + contentHeight)

	box = $("#area_3")[0]
	contentHeight += parseFloat(window.getComputedStyle(box).height.replace("px", ""));
	//console.log("contentHeight 3：" + contentHeight)

	box = $("#area_4")[0]
	contentHeight += parseFloat(window.getComputedStyle(box).height.replace("px", ""));
	//console.log("contentHeight 4：" + contentHeight)

	tableHeight = winHeight - contentHeight- 60*1.2;

	leftFreezeEl.height(tableHeight);
	scrollAreaEl.height(tableHeight);
}

adjustHeight();
win.on('resize', adjustHeight);

//设置iscroll
var myScroll = new IScroll('.t_r_content', {
	scrollX: true,
	scrollY: true,
	probeType: 3
});

//阻止默认滚动
scrollAreaEl.on('touchmove mousewheel', function(e) {
	e.preventDefault();
});

//固定上左表头的滚动
myScroll.on('scroll', updatePosition);
myScroll.on('scrollEnd', updatePosition);

function updatePosition() {
	var a = this.y;
	var b = this.x;
	leftTableEl.css('transform', 'translate(0px, ' + a + 'px) translateZ(0px)');
	rightTableEl.css('transform', 'translate(' + b + 'px, 0px) translateZ(0px)');
}
