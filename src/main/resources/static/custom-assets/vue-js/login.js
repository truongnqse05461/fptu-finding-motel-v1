document.addEventListener("DOMContentLoaded", () => {
    // here is the Vue code

    var loginInstance = new Vue({
        el: '#login-form',
        data: {
            username: "",
            password: "",
            userInfo: {},
            showMsg: false,
        },
        methods: {
            handleInputUsername(value){
                this.username = value;
            },
            handleInputPwd(value){
                this.password = value;
            },
            loginButtonClickEvent() {
                let userInfo = {
                    "username": this.username,
                    "password": this.password
                }
                loadingInstance.isHidden = false
                document.body.setAttribute("class", "loading-hidden-screen")
                fetch("https://localhost:8081/api-login", {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(userInfo)
                }).then(response => response.json())
                    .then((data) => {
                        if (data.msgCode === "msg000") {
                            this.showMsg = false
                            console.log(data)
                            localStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                            this.$cookies.set("access_token", data.accessToken)
                            console.log(this.$cookies.get("access_token"))
                            window.location.href = "https://localhost:8081/"
                        } else if (data.msgCode === "msg001") {
                            this.showMsg = true
                            loadingInstance.isHidden = true
                            document.body.removeAttribute("class")
                        }

                    }).catch(error => {
                    console.log(error);
                })
            }

        },
        computed: {
            fillRegisteredUsername() {
                let registeredUsername = localStorage.getItem("registeredUsername");
                if (typeof (registeredUsername) !== 'undefined' && registeredUsername != null && registeredUsername.length > 0) {
                    this.username = registeredUsername;
                    return registeredUsername;
                }else{
                    return "";
                }
            }
        }
    })
});

var loadingInstance = new Vue({
    el: '#loading-wrapper',
    data: {
        isHidden: true
    },

})