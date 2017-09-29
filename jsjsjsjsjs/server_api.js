/**
 * 这个是api接口的管理类，以前写js的时候都没有把api接口统一管理，导致难以维护。
 */

function ServerAPIs() {
	/*
	 * $.ajax({ type : '', url : url, data : data, success : success, dataType :
	 * dataType });
	 */
}

/**
 * 返回api?param，用于get,json转成kv对模式，不过没用上，项目里都是直接json
 * 
 * @param apiName
 *            除了ServerBaseURL的其他api地址
 * @param params
 *            json的对象和数据，都是string
 * @returns {string}
 */
// ServerAPIs.prototype.getAPI = function (apiName) {
ServerAPIs.prototype.gApiPathAndParam = function(apiName, params) {
	var appendParams = "?";
	for ( var p in params) {
		appendParams += (p + "=" + params[p] + "&");
	}
	appendParams += ("ts=" + new Date().getTime());
	return this.ServerBaseURL + apiName + appendParams;
}

/**
 * 单单返回param，json转成kv对模式，不过没用上，项目里都是直接json
 * 
 * @param params
 *            json的对象和数据，都是string
 */
ServerAPIs.prototype.pApiParam = function(params) {
	var appendParams = "";
	for ( var p in params) {
		appendParams += (p + "=" + params[p] + "&");
	}
	appendParams += ("ts=" + new Date().getTime());
	return appendParams;
}

/**
 * 返回api
 * 
 * @param apiName
 * 
 * @returns {string}
 */
// ServerAPIs.prototype.getAPI = function (apiName) {
ServerAPIs.prototype.apiPath = function(apiName) {
	return this.ServerBaseURL + apiName;
}

/**
 * 
 * @type {string}
 */
ServerAPIs.prototype.ServerBaseURL = "/attendance/";

/**
 * 这个api可以获取组织结构比如[公司-(部门1<员工1，员工2>, 部门2)]这样的结构
 * 
 * @type {string}
 */
ServerAPIs.prototype.GetOrgStruct = "getOrgStruct";

/**
 * 这个api可以获得当前管理员可以分配的班次信息
 * 
 * @type {string}
 */
ServerAPIs.prototype.classes4CurrentAdmin = "getAccountClassesList.action";

/**
 * 创建班次的接口
 * 
 */
ServerAPIs.prototype.createClasses = "createClasses.action";

/**
 * 删除班次
 */
ServerAPIs.prototype.delClasses = "delClasses.action";

/**
 * 排班接口
 */
ServerAPIs.prototype.setscheduling = "scheduling.action";

/**
 * 按年月获取指定用户列表
 */
ServerAPIs.prototype.userScheduleByYearAndMonth = "getUserListMonthScheduling.action";

/**
 * 调班接口
 */
ServerAPIs.prototype.adjustmentScheduling = "adjustmentScheduling.action";

/**
 * 获取管理员已经拍好的审批流程的列表
 */
ServerAPIs.prototype.approvalProcessList = "getAuditorList.action";

/**
 * 获取某一个已经审批流程的详细信息
 */
ServerAPIs.prototype.approvalDetails = "getAuditorSet.action";

/**
 * 提交/更新一个审批流程
 */
ServerAPIs.prototype.submitApprovalProcess = "setAuditor.action";

/**
 * 删除一个审批流程
 */
ServerAPIs.prototype.delApprovalProcess = "delAuditor.action";

/**
 * 获取考勤统计数据,显示的数据是公司分组
 */
ServerAPIs.prototype.getReport = "getReport.action";

/**
 * 获取公司历史考勤信息，按部门分组
 */
ServerAPIs.prototype.getUnitsDepReport = "getUnitReport.action";

/**
 * 部门的历史考勤信息，按照人员显示
 */
ServerAPIs.prototype.getDepEmpReport = "getDepReport.action";

/**
 * 查看个人明细
 */
ServerAPIs.prototype.getUserDetailReport = "getUserReport.action";

/**
 * 获取用户的考勤状态，按照公司或者部门进行查询
 */
ServerAPIs.prototype.getUserNeedCheckinStatusGroupByComorDep = "getUserList.action";

/**
 * 设置一个员工为无需考勤人员
 */
ServerAPIs.prototype.addNoAttendanceUser = "addNoAttendanceUser.action";

/**
 * 讲一个员工从无需考勤人员中删除
 */
ServerAPIs.prototype.removeNoAttendanceUser = "removeNoAttendanceUser.action";

/**
 * 获取一个用户某一天的轨迹
 */
ServerAPIs.prototype.getUserOneDayTrack = "getUserTrack.action";

/**
 * 获取蔚蓝中当前的所有用户
 */
ServerAPIs.prototype.getEmpInFence = "getInPolygonUser.action";

/**
 * 显示异常和报警的列表，这是页面上的 ，不是websocket的
 */
ServerAPIs.prototype.getAlarmList = "getAlarmList.action";

/**
 * 解除警报
 */
ServerAPIs.prototype.updateAlarmStatus = "updateAlarmStatus.action";

/**
 * 按公司获取历史报表
 */
ServerAPIs.prototype.exportExeclByUnit = "exportExeclByUnit.action";

/**
 * 按公司获取实时报表
 */
ServerAPIs.prototype.exportExeclByUnitOfRealtime = "exportExeclByUnitOfRealtime.action";
/**
 * 获取所有公司的历史考勤报表不需要明细
 */
ServerAPIs.prototype.exportExeclAll = "exportExeclAll.action";

/**
 * 获取所有公司的实时考勤报表不需要明细
 */
ServerAPIs.prototype.exportExeclAllRealtime = "exportExeclAllRealtime.action";

/**
 * 创建角色的时候授权
 */
ServerAPIs.prototype.createRoleAndAuthorize = "createRoleAndAuthorize.action";

/**
 * 获取已经设定的角色列表
 */
ServerAPIs.prototype.getRoleList = "getRoleList.action";

/**
 * 更新角色名和描述
 */
ServerAPIs.prototype.updateRoleDesc = "updateRoleDesc.action";

/**
 * 更新角色授权
 */
ServerAPIs.prototype.updateRoleAuthorize = "updateRoleAuthorize.action";

/**
 * 获取角色授权的详细信息
 */
ServerAPIs.prototype.getRoleAuthorize = "getRoleAuthorize.action";

/**
 * 删除角色
 */
ServerAPIs.prototype.delRole = "delRole.action";

/**
 * 获取所有已经设定的管理员账户列表
 */
ServerAPIs.prototype.getAccountList = "getAccountList.action";

/**
 * 在用户（账户）列表，修改用户授权的角色
 */
ServerAPIs.prototype.updateAccountAuthorize = "updateAccountAuthorize.action";

/**
 * 重置管理员密码
 */
ServerAPIs.prototype.editAccountPwd = "editAccountPwd.action";

/**
 * 删除一个用户(账户)
 */
ServerAPIs.prototype.delAccount = "delAccount.action";

/**
 * 修改自己的密码
 */
ServerAPIs.prototype.editMyPwd = "editMyPwd.action";

/**
 * 日志查询
 */
ServerAPIs.prototype.getSystemLog = "getSystemLog.action";

/**
 * 节日列表
 */
ServerAPIs.prototype.getHolidayList = "getHolidayList.action";

/**
 * 删除一个节假日
 */
ServerAPIs.prototype.delHoliday = "delHoliday.action";

/**
 * 这个是轮训的获取报警接口
 */
ServerAPIs.prototype.getAlarmMessage = "getAlarmMessage.action";

/**
 * 批量解除报警
 */
ServerAPIs.prototype.batchUpdateAlarmStatus = "batchUpdateAlarmStatus.action";

/**
 * 批量解除报警<br/> userName="abc"<br/> page=1<br/> pageSize=10<br/>
 */
ServerAPIs.prototype.getUserListByName = "getUserListByName.action";

/**
 * 修改管理员的昵称
 */
ServerAPIs.prototype.updateAccountInfo = "updateAccountInfo.action";

/**
 * 用户关联
 */
ServerAPIs.prototype.unionMemberAccount = "unionMemberAccount.action";

/**
 * 在员工列表列出所有无需考勤人员的名单
 */
ServerAPIs.prototype.getNoNeedAttendanceList = "getNoNeedAttendanceList.action";

/**
 * 执行一个post请求，请求的参数是json
 */
ServerAPIs.prototype._post = function(_callback, _url, _data) {
	$.ajax({
		type : 'post',
		contentType : 'application/json;charset=utf-8',
		url : _url,
		data : _data,
		success : _callback,
		dataType : 'text',
		error : function(error) {
			// alert("错误:" + error);
			// console.log(error);
		}
	});
}

/**
 * 执行一个post请求,传统的kv对方式传递参数,其实也可以传json，是因为springmvc要制定contentType，才有了上面的方法
 */
ServerAPIs.prototype._postTradition = function(_callback, _url, _data) {
	$.ajax({
		type : 'post',
		// contentType : 'application/json;charset=utf-8',
		url : _url,
		data : _data,
		success : _callback,
		dataType : 'text',
		error : function(error) {
			// alert("错误:" + error);
			// console.log(error);
		}
	});
}

/**
 * 执行一个get请求
 */
ServerAPIs.prototype._getTradition = function(_callback, _url, _data) {
	$.ajax({
		type : 'get',
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		// contentType : 'application/json;charset=utf-8',
		url : _url + "?" + _data,
		success : _callback,
		dataType : 'text',
		error : function(error) {
			// alert("错误:" + error);
			// console.log(error);
		}
	});
}
