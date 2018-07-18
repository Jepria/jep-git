// загрузка JSON с поддержкой старых браузеров
function loadJSON(file, callback) {
	var xhr = new XMLHttpRequest();
	xhr.overrideMimeType("application/json");
	xhr.open('GET', file, true); 
	xhr.onreadystatechange = function () {
		if (xhr.readyState == 4 && xhr.status == "200") {
			callback(xhr.responseText);
		}
	};
	xhr.send();
}
// асинхронно подгружаем JSON с инф о сборке
loadJSON("./actuator/version.json", function(response) {
	var versionJSON = JSON.parse(response);
	// ищем плашку для вывода текста
	if (document.getElementById('testBuildMessage')){
		var myClasses = document.getElementsByClassName("jepRia-testBuildMessageInfo");
		var library='';
		for(var key in versionJSON.library){
				library+=key + ':' + versionJSON.library[key]+' ';
		}
		myClasses[0].innerHTML='This is test '+versionJSON.svn.repo_name+'/'+versionJSON.svn.module_name+' build!<br>Deploy at: '+versionJSON.compile.time_stamp+'<br>'+
		'SVN: '+versionJSON.svn.tag_version+'/'+versionJSON.svn.revision+'<br>'+
		'Library: '+library+'<br>'+
		'Developer: '+versionJSON.compile.user_name+' '+versionJSON.compile.host_name+
		'<small>'+versionJSON.compile.UUID+'</small>';
	}
	console.info(versionJSON);
});
