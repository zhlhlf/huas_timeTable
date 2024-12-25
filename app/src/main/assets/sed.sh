cd `dirname $0`
if [ "$(cat $1.html | grep '密码不能为空')" ];then
    mv $1.html ${1}_no.html
    exit 1
fi
req=<<zhlhlf
<div class="showMsg" onclick="unShow()">
	<div class="content">
	</div>
</div>
<form
zhlhlf

sed -i '/<iframe/,/<\/script>/d' ./$1.html
sed -i "s|<form|$req|g" ./$1.html

req=<<zhlhlf
</form>
<style>
	p {
		font-size: 16px !important;
		text-align: center !important;
	}
	body {
		background-color: #ffff;
	}
	td {
		text-align: center;
		padding: 0px 1px;
		border-right: 1px solid #fff;
		width: 12%;
		border-radius: 5%;
		height: 10%;
	}
	th {
		border-bottom: 1px solid #D6DFE1;
	}
	.showMsg{
		position: fixed;
		justify-content: center;
		align-items: center;
		height: 100%;
		width: 100%;
		display: none;
		background-color: rgba(207, 254, 225,0.5);
	}
	.showMsg .content{
		border-radius: 5%;
		background-color: rgb(255, 255, 255);
		font-size: 30px;
		text-align: center;
		color: red;
   		border: 1px solid red;		
	}
</style>
<script>
	var show = document.getElementsByClassName("showMsg")[0];
	function showMsg(d){
		show.children[0].innerHTML=d.children[0].title;
		show.style.display="flex";
	}
	function unShow(){
		if(event.target == show.children[0]) return;
		show.style.display="none";
	}
	var a = document.getElementsByTagName("p");
	var colors =["rgb(213, 95, 111)","rgb(177, 134, 163)","rgb(246, 211, 168)","rgb(153, 241, 184)","rgb(250, 185, 169)","rgb(154, 229, 249)"];
	for (var i = 0; i < a.length; i++) {
		var b = a[i].title.split('<br/>')[4].split('：')[1];
		a[i].innerHTML = a[i].innerHTML + '<br><br>' + b;
	}
	var tds = document.getElementsByTagName("td");
	for (var i = 0; i < tds.length; i++) {
		if(tds[i].innerHTML.match("大节")) {
			var s = tds[i].innerHTML.split(")")[1];
			tds[i].innerHTML=s;
			continue;
		}
		if (/d/.test(tds[i].innerHTML)) {
			var x = Math.floor(Math.random() * colors.length);
			tds[i].style.background = colors[x];
		}
	}

  const tdElements = document.querySelectorAll('td');
  tdElements.forEach(td => {
      td.addEventListener('click', function(event) {
          showMsg(td);
      });
  });

</script>
zhlhlf
sed -i "s|</form>|$req|g" ./$1.html

