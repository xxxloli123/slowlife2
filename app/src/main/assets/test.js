function goBack(){
	if (navigator.userAgent.indexOf('Android')>0) {
		Android.goBack();
	}else{
		// ios   js code
	}
}

function tel(phone) {
	if (navigator.userAgent.indexOf('Android')>0) {
		Android.tel(phone);
	}else{
		// ios   js code
	}
}


