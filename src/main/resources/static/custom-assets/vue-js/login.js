document.addEventListener("DOMContentLoaded", () => {
    // here is the Vue code

    var loginInstance = new Vue({
        el: '#login-form',
        data: {
            username: "",
            password: "",
            userInfo: {},
            showMsg: false,
            message: "",
        },
        methods: {
            loginButtonClickEvent() {
                if (this.username == null || this.username.length == 0) {
                    this.showMsg = true
                    this.message = "Vui lòng nhập tên đăng nhập"
                }else if(this.password == null || this.password.length == 0){
                    this.showMsg = true
                    this.message = "Vui lòng nhập mật khẩu"
                }else {
                    let userInfo = {
                        "username": this.username,
                        "password": this.password
                    }
                    loadingInstance.isHidden = false
                    document.body.setAttribute("class", "loading-hidden-screen")
                    fetch("/api-login", {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify(userInfo)
                    }).then(response => response.json())
                        .then((data) => {
                            if (data.msgCode === "login000") {
                                this.showMsg = false
                                sessionStorage.setItem("userInfo", JSON.stringify(data.userInfo))
                                this.$cookies.set("access_token", data.accessToken)
                                window.location.href = "/"
                            } else {
                                this.message = data.message
                                this.showMsg = true
                                loadingInstance.isHidden = true
                                document.body.removeAttribute("class")
                            }

                        }).catch(error => {
                        console.log(error);
                    })
                }

            },
        },
        computed: {
            fillRegisteredUsername() {
                let registeredUsername = sessionStorage.getItem("registeredUsername");
                if (typeof (registeredUsername) !== 'undefined' && registeredUsername != null && registeredUsername.length > 0) {
                    this.username = registeredUsername;
                    return registeredUsername;
                }else{
                    return "";
                }
            }
        },
        mounted(){
            authenticationInstance.hidePreloader()
        }
    })
    var loadingInstance = new Vue({
        el: '#loading-wrapper',
        data: {
            isHidden: true
        },

    })
});

