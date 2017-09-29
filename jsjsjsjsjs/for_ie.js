/**
 * 给表格一个，这个在ie9以及一下要用
 * 
 * @param table
 * @param html
 */
function for_ie_setTableInnerHTML(table, html) {
	var temp = table.ownerDocument.createElement('div');
	temp.innerHTML = '<table><tbody>' + html + '</tbody></table>';
	if (table.tBodies.length == 0) {
		var tbody = document.createElement("tbody");
		table.appendChild(tbody);
	}
	table.replaceChild(temp.firstChild.firstChild, table.tBodies[0]);
}

/**
 * 判断是不是ie9
 * 
 * @returns {Boolean}
 */
function for_ie_isIE9() {
	if (navigator.appName == "Microsoft Internet Explorer"
			&& navigator.appVersion.split(";")[1].replace(/[ ]/g, "") == "MSIE9.0") {
		return true;
	}
	return false;
}