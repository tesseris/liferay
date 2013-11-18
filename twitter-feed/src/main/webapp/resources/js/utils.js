function openPopup(item) {
	var url = item.getAttribute("href");
	window.open(url, 'tweet action window', 'width=580,height=500');
	return false;
}

