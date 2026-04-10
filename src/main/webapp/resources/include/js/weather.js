/*const API_KEY = "84ae5aece71b825be75be49039838207";
const weather = document.querySelector("#weather span:first-child");
const city = document.querySelector("#weather span:last-child");

function onGeoOk(position){
    const lat = position.coords.latitude;
    const lon = position.coords.longitude;
    const url = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;
    fetch(url)
        .then(response => response.json())
        .then((data) =>{
            city.innerText = `location : ${data.name}`;
            weather.innerText = `weather : ${data.weather[0].main}\ntemperature : ${data.main.temp}℃`;
        });
}
function onGeoError(){
    alert("Can't find you. No weather for you.")
}
navigator.geolocation.getCurrentPosition(onGeoOk, onGeoError);*/
const API_KEY = "84ae5aece71b825be75be49039838207";

function getWeatherCurrent(url){
	fetch(url)
		.then((res)=>res.json())
		.then((data) => {
			const city = document.querySelector(".weather-text span");
			const weather = document.querySelector(".weather-main");
			const weatherTemp = document.querySelector(".weather-temp span");
			const weatherIconImg = document.querySelector(".weather-icon img");
			
			const weatherIcon = data.weather[0].icon;
      		const weatherIconUrl = `http://openweathermap.org/img/wn/${weatherIcon}@2x.png`;
			
			city.innerText = data.name;
      		weatherIconImg.setAttribute("src", weatherIconUrl);
      		weather.innerText = data.weather[0].main;
      		weatherTemp.innerText = `${Math.floor(data.main.temp)}°`;
		});
}
const onGeoSuccess = (position) => {
  const lat = position.coords.latitude;
  const lon = position.coords.longitude;
  const currentUrl = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;
  

  getWeatherCurrent(currentUrl);

};

const onGeoError = () => {
  console.warn("위치 정보를 가져올 수 없습니다.");
  // 기본 위치(서울)로 대체
  const defaultUrl = `https://api.openweathermap.org/data/2.5/weather?lat=37.5665&lon=126.9780&appid=${API_KEY}&units=metric`;
  getWeatherCurrent(defaultUrl);
};

navigator.geolocation.getCurrentPosition(onGeoSuccess, onGeoError);