/**
 * Created by wx on 2016/11/11.
 */
if (document.all) {
	if (typeof(cyansLoad)=="undefined") {

	} else
		window.attachEvent("onload", cyansLoad);
} else {
	if (typeof(cyansLoad)=="undefined") {
	} else
		window.addEventListener("load", cyansLoad, false);
}