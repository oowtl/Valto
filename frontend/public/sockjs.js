/* eslint-disable */
var sockJS = new SockJS('api/v1/ws')
var stompClient = Stomp.over(sockJS);
var roomId = 2;
var successCallback = function(){
  var subscription = stompClient.subscription('/conferences/' + roomId, function(message){
    if(message){
      var msg = JSON.parse(message.body);
      console.log('CHECK' + msg);
    }
  });

  stompClient.send('/conferences/' + roomId + '/send', {} , '메세지 전송');

  //방나가기
  subscription.unsubscribe();
};
var errorCallback = function(){

};
stompClient.connect({accessToken : 'myJWTToken '},
successCallback, errorCallback);
