function getAjaxRequest(requestUrl, onSucessFunction) {
	$.ajax({
		url: requestUrl,
		type: 'GET',
		dataType: 'json',
		cache: false,
		async: false,
		success: onSucessFunction
	});
}

function formatDate(datetime) {
	var date = new Date(Number(datetime));
	return date.toString("yyyy-MM-dd HH:mm:ss");
}