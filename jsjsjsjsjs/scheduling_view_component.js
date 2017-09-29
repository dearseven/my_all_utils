/**
 * 这个js是排班视图的控件
 */

/**
 * 
 */
function clickDayTd(id) {
	var layer = document.getElementById("schedule_table_conver");
	layer.style.display = "none";

	var userId = id.split(",")[0].split("_")[1];
	var classId = id.split(",")[1].split("_")[1];
	var date = id.split(",")[2]
	var week = id.split(",")[3];
	var y = date.split("-")[0];
	var m = parseInt(date.split("-")[1]) - 1;
	var d = date.split("-")[2];

	// alert(userId+" "+classId+" "+date+" "+week+" "+y+"-"+m+"-"+d );
	var today = new Date();
	today.setHours(0);
	today.setMinutes(0);
	today.setSeconds(0);
	var clickDay = new Date();
	clickDay.setFullYear(y);
	clickDay.setMonth(m);
	clickDay.setDate(d);
	clickDay.setHours(0);
	clickDay.setMinutes(0);
	clickDay.setSeconds(0);
	if (parseInt(clickDay.getTime() / 1000) <= parseInt(today.getTime() / 1000)) {
		alert("只能修改今日之后的排班");
		return;
	}

	ScheduleTable.itemClick(userId,classId,date,week,y,m,d);
    //alert(event.pageX+","+event.pageY+" "+id);

    var w = (parseInt(layer.style.width.replace("px", "")));
    var h = (parseInt(layer.style.height.replace("px", "")));
    //layer.style.top = event.pageY + "px";

	var calendar = new Date();
	calendar.setFullYear(y);
	calendar.setMonth(m);
	// 获得当月的最大天数
	var curMonthDays = new Date(calendar.getFullYear(),
			(calendar.getMonth() + 1), 0).getDate();
//	layer.style.top = (event.pageY - h / 2) + "px";
//	if (d < curMonthDays - 2) {
//		layer.style.left = (event.pageX) + "px";
//	} else {
//		layer.style.left = (event.pageX - w) + "px";
//	}
	layer.style.display = "block";
}

function ScheduleTable(container) {
	this.data = undefined;
	var date = new Date();
	this.y = date.getFullYear();
	this.m = date.getMonth();
	this.d = date.getDate();
	this.container = document.getElementById(container);
	this.containerWidth = this.container.clientWidth;

	this.table = document.createElement("table");
	this.table.className = "table schedule-table";
}

ScheduleTable.itemClick=undefined;

ScheduleTable.prototype.setYMD = function(y, m, d) {
	if (y != -1) {
		this.y = y;
	}
	if (m != -1) {
		this.m = m;
	}
	if (d != -1) {
		this.d = d;
	}
}

ScheduleTable.prototype.getY=function(){
	return this.y;
}
ScheduleTable.prototype.getM=function(){
	return this.m;
}
ScheduleTable.prototype.getD=function(){
	return this.d;
}

ScheduleTable.prototype.reset = function() {
	this.makeTableNav(this.y, this.m);
	this.makeTableData();
}

ScheduleTable.prototype.removeTr = function(id) {

	for ( var i in this.table.childNodes) {
		if ((this.table.childNodes[i].tagName == "TR" || this.table.childNodes[i].tagName == "tr")
				&& this.table.childNodes[i].id == ("userId_" + id)) {
			//console.log("removeTR:" + this.table.childNodes[i].id);
			this.table.removeChild(this.table.childNodes[i]);
			break;
		}
	}

}

ScheduleTable.prototype.makeTableDataOneByOne = function(data) {
	var today = new Date();
	today.setHours(0);
	today.setMinutes(0);
	today.setSeconds(0);

	var trDays = document.createElement("tr");
	trDays.className = "schedule-table-data-tr";
	for (var i = -1; i < data.everydayScheduling.length; i++) {
		var tdDays = document.createElement("td");
		if (i != -1) {
			tdDays.className = "schedule-table-data-day";
			var daySch = data.everydayScheduling[i];

			var y = daySch.date.split("-")[0];
			var m = parseInt(daySch.date.split("-")[1]) - 1;
			var d = daySch.date.split("-")[2];
			var clickDay = new Date();
			clickDay.setFullYear(y);
			clickDay.setMonth(m);
			clickDay.setDate(d);
			clickDay.setHours(0);
			clickDay.setMinutes(0);
			clickDay.setSeconds(0);

			trDays.id = "userId_" + data.userId;
			tdDays.id = "classesId_" + daySch.classesId;
			var clickId = trDays.id + "," + tdDays.id + "," + daySch.date + ","
					+ daySch.week;
			/*
			 * tdDays.innerHTML = "<a href='javascript:clickDayTd( \"" +
			 * clickId + "\");'>" + daySch.abbreviation + "</a>";
			 */

			if (parseInt(clickDay.getTime() / 1000) < parseInt(today.getTime() / 1000)) {
				/*
				 * console.log((parseInt(clickDay.getTime() / 1000) + "," +
				 * parseInt(today.getTime() / 1000)));
				 */
				tdDays.innerHTML = "<a href='javascript:void(0);'><span  style='text-decoration:line-through;' onclick='clickDayTd(\""
						+ clickId
						+ "\")'>"
						+ daySch.abbreviation
						+ "<span></a>";
			} else {
				tdDays.innerHTML = "<a href='javascript:void(0);'><span onclick='clickDayTd(\""
						+ clickId
						+ "\")'>"
						+ daySch.abbreviation
						+ "<span></a>";
			}
			tdDays.style.backgroundColor = daySch.showColour;
		} else {
			tdDays.className = "schedule-table-data-employee";
			tdDays.innerHTML = "<a href='javascript:void(0);' onclick='clickRow(\"userId_" + data.userId+"\")'>"+data.userName+"</a>";
		}
		trDays.appendChild(tdDays);
	}
	this.table.appendChild(trDays)
}

/**
 * @!!!!!这个方法不用了，因为要一行一行生成，有的还被修改中,坑爹! 按数据生成表格
 */
ScheduleTable.prototype.makeTableData = function() {
	var today = new Date();
	today.setHours(0);
	today.setMinutes(0);
	today.setSeconds(0);

	for ( var j in this.data) {
		var data = this.data[j]; // 这个data就是一个人的数据
		var trDays = document.createElement("tr");
		trDays.className = "schedule-table-data-tr";
		for (var i = -1; i < data.everydayScheduling.length; i++) {
			var tdDays = document.createElement("td");
			if (i != -1) {
				tdDays.className = "schedule-table-data-day";
				var daySch = data.everydayScheduling[i];

				var y = daySch.date.split("-")[0];
				var m = parseInt(daySch.date.split("-")[1]) - 1;
				var d = daySch.date.split("-")[2];
				var clickDay = new Date();
				clickDay.setFullYear(y);
				clickDay.setMonth(m);
				clickDay.setDate(d);
				clickDay.setHours(0);
				clickDay.setMinutes(0);
				clickDay.setSeconds(0);

				trDays.id = "userId_" + data.userId;
				tdDays.id = "classesId_" + daySch.classesId;
				var clickId = trDays.id + "," + tdDays.id + "," + daySch.date
						+ "," + daySch.week;
				/*
				 * tdDays.innerHTML = "<a href='javascript:clickDayTd( \"" +
				 * clickId + "\");'>" + daySch.abbreviation + "</a>";
				 */

				if (parseInt(clickDay.getTime() / 1000) < parseInt(today
						.getTime() / 1000)) {
					/*
					 * console.log((parseInt(clickDay.getTime() / 1000) + "," +
					 * parseInt(today.getTime() / 1000)));
					 */
					tdDays.innerHTML = "<a href='javascript:void(0);'><span  style='text-decoration:line-through;' onclick='clickDayTd(\""
							+ clickId
							+ "\")'>"
							+ daySch.abbreviation
							+ "<span></a>";
				} else {
					tdDays.innerHTML = "<a href='javascript:void(0);'><span onclick='clickDayTd(\""
							+ clickId
							+ "\")'>"
							+ daySch.abbreviation
							+ "<span></a>";
				}
				tdDays.style.backgroundColor = daySch.showColour;
			} else {
				tdDays.className = "schedule-table-data-employee";
				tdDays.innerHTML = data.userName;
			}
			trDays.appendChild(tdDays);
		}
		this.table.appendChild(trDays);
	}
}
/**
 * 生成表格头
 */
ScheduleTable.prototype.makeTableNav = function(y, m) {
	this.y = y;
	this.m = m;
	var calendar = new Date();
	calendar.setFullYear(y);
	calendar.setMonth(m);
	// 获得当月的最大天数
	var curMonthDays = new Date(calendar.getFullYear(),
			(calendar.getMonth() + 1), 0).getDate();

	var trDays = document.createElement("tr");
	trDays.className = "schedule-table-nav-tr";

	for (var i = -1; i < curMonthDays; i++) {
		var tdDays = document.createElement("td");
		if (i == -1) {
			tdDays.style.width = (this.containerWidth / curMonthDays + curMonthDays * 2.25)
					+ "px";
			tdDays.className = "schedule-table-nav-employee";
			tdDays.innerHTML = "员工"
		} else {
			calendar.setDate((i + 1));

			tdDays.style.width = (this.containerWidth / curMonthDays - 1)
					+ "px";
			var dayNumber = calendar.getDate();
			var week = calendar.getDay();
			var weekDay = '';
			if (week == 0) {
				weekDay = "日";
			} else if (week == 1) {
				weekDay = "一";
			} else if (week == 2) {
				weekDay = "二";
			} else if (week == 3) {
				weekDay = "三";
			} else if (week == 4) {
				weekDay = "四";
			} else if (week == 5) {
				weekDay = "五";
			} else if (week == 6) {
				weekDay = "六";
			}
			if (weekDay == "日" || weekDay == "六") {
				tdDays.className = "schedule-table-nav-day-weekend";
				tdDays.innerHTML = "<a href='javascript:void(0);' onclick='clickColumn(\""+dayNumber+"\")'><span class='schedule-table-nav-day-weekend-day'>"
						+ dayNumber
						+ "</span><br/><span class='schedule-table-nav-day-weekend-week'>"
						+ weekDay + "</span></a>"
			} else {
				tdDays.className = "schedule-table-nav-day-common";
				tdDays.innerHTML = "<a href='javascript:void(0);' onclick='clickColumn(\""+dayNumber+"\")'><span class='schedule-table-nav-day-common-day'>"
						+ dayNumber
						+ "</span><br/><span class='schedule-table-nav-day-common-week'>"
						+ weekDay + "</span></a>"
			}
		}
		trDays.appendChild(tdDays);
	}
	this.table.appendChild(trDays);
	this.container.appendChild(this.table);
}

ScheduleTable.prototype.clean = function() {
	this.table.innerHTML = "";
}