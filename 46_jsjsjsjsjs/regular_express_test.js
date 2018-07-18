/**
 * 可以用*来替代任意字符的匹配
 * @param {Object} s1 匹配符号
 * @param {Object} s2 待匹配的字符串
 */
function matchHoshiTag(s1, s2) {

	//假设在业务逻辑中我用*代表任意字符 
	//在正则表达式中.+代表是除了换行符的任意字符出现字少一次
	//所以这个表示 *abc*de*fg*，换句话所在代码里我们可以看看用户是不是在匹配一个这种模式的字符串
	//一开始我们直接把*转成.+的模式 
	//s1=(s1.replace(/\*/g,".+"));然后就可以匹配了
	var re = new RegExp(".+abc.+de.+fg.+");
	//var re=/.+abc.+de.+fg.+/;

	return re.test(s2);
}